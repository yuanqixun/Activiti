/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti5.engine.test.bpmn.event.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.runtime.Clock;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti5.engine.impl.test.PluggableActivitiTestCase;

/**
 * @author Joram Barrez
 */
public class BoundaryTimerEventTest extends PluggableActivitiTestCase {
  
  private static boolean listenerExecutedStartEvent = false;
  private static boolean listenerExecutedEndEvent = false;
  
  public static class MyExecutionListener implements ExecutionListener {
    private static final long serialVersionUID = 1L;

    public void notify(DelegateExecution execution) {
      if ("end".equals(execution.getEventName())) {
        listenerExecutedEndEvent = true;
      } else if ("start".equals(execution.getEventName())) {
        listenerExecutedStartEvent = true;
      }
    }    
  }
  
  /*
   * Test for when multiple boundary timer events are defined on the same user
   * task
   * 
   * Configuration: - timer 1 -> 2 hours -> secondTask - timer 2 -> 1 hour ->
   * thirdTask - timer 3 -> 3 hours -> fourthTask
   * 
   * See process image next to the process xml resource
   */
  @Deployment
  public void testMultipleTimersOnUserTask() {
    Clock clock = processEngineConfiguration.getClock();
    // Set the clock fixed
    clock.reset();
    Date startTime = clock.getCurrentTime();
    processEngineConfiguration.setClock(clock);

    // After process start, there should be 3 timers created
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("multipleTimersOnUserTask");
    JobQuery jobQuery = managementService.createJobQuery().processInstanceId(pi.getId());
    List<Job> jobs = jobQuery.list();
    assertEquals(3, jobs.size());

    // After setting the clock to time '1 hour and 5 seconds', the second timer should fire
    clock.setCurrentTime(new Date(startTime.getTime() + ((60 * 60 * 1000) + 5000)));
    processEngineConfiguration.setClock(clock);
    
    waitForJobExecutorToProcessAllJobs(5000L, 25L);
    assertEquals(0L, jobQuery.count());

    // which means that the third task is reached
    Task task = taskService.createTaskQuery().singleResult();
    assertEquals("Third Task", task.getName());
    
    processEngineConfiguration.resetClock();
  }
  
  @Deployment
  public void testTimerOnNestingOfSubprocesses() {
    
    Date testStartTime = processEngineConfiguration.getClock().getCurrentTime();
    
    runtimeService.startProcessInstanceByKey("timerOnNestedSubprocesses");
    List<Task> tasks = taskService.createTaskQuery().orderByTaskName().asc().list();
    assertEquals(2, tasks.size());
    assertEquals("Inner subprocess task 1", tasks.get(0).getName());
    assertEquals("Inner subprocess task 2", tasks.get(1).getName());
    
    // Timer will fire in 2 hours
    processEngineConfiguration.getClock().setCurrentTime(new Date(testStartTime.getTime() + ((2 * 60 * 60 * 1000) + 5000)));
    Job timer = managementService.createJobQuery().timers().singleResult();
    managementService.executeJob(timer.getId());
    
    Task task = taskService.createTaskQuery().singleResult();
    assertEquals("task outside subprocess", task.getName());
  }
  
  @Deployment
  public void testExpressionOnTimer(){
    Clock clock = processEngineConfiguration.getClock();
    // Set the clock fixed
    clock.reset();
    Date startTime = clock.getCurrentTime();
    processEngineConfiguration.setClock(clock);
    
    HashMap<String, Object> variables = new HashMap<String, Object>();
    variables.put("duration", "PT1H");
    
    // After process start, there should be a timer created
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("testExpressionOnTimer", variables);

    JobQuery jobQuery = managementService.createJobQuery().processInstanceId(pi.getId());
    List<Job> jobs = jobQuery.list();
    assertEquals(1, jobs.size());

    // After setting the clock to time '1 hour and 5 seconds', the second timer should fire
    clock.setCurrentTime(new Date(startTime.getTime() + ((60 * 60 * 1000) + 5000)));
    processEngineConfiguration.setClock(clock);
    
    waitForJobExecutorToProcessAllJobs(5000L, 25L);
    assertEquals(0L, jobQuery.count());
    
    // start execution listener is not executed
    assertFalse(listenerExecutedStartEvent);
    assertTrue(listenerExecutedEndEvent);

    // which means the process has ended
    assertProcessEnded(pi.getId());
    
    processEngineConfiguration.resetClock();
  }
  

  @Deployment
  public void testNullExpressionOnTimer(){
	  
    HashMap<String, Object> variables = new HashMap<String, Object>();
    variables.put("duration", null);
    
    // After process start, there should be a timer created
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("testNullExpressionOnTimer", variables);

    //NO job scheduled as null expression set
    JobQuery jobQuery = managementService.createJobQuery().processInstanceId(pi.getId());
    List<Job> jobs = jobQuery.list();
    assertEquals(0, jobs.size());

    // which means the process is still running waiting for human task input.
    ProcessInstance processInstance = processEngine
    	      .getRuntimeService()
    	      .createProcessInstanceQuery()
    	      .processInstanceId(pi.getId())
    	      .singleResult();
    assertNotNull(processInstance);
  }
  
  
  @Deployment
  public void testTimerInSingleTransactionProcess() {
    // make sure that if a PI completes in single transaction, JobEntities associated with the execution are deleted.
    // broken before 5.10, see ACT-1133
    runtimeService.startProcessInstanceByKey("timerOnSubprocesses"); 
    assertEquals(0, managementService.createJobQuery().count());
  }
  
  @Deployment
  public void testRepeatingTimerWithCancelActivity() {
    runtimeService.startProcessInstanceByKey("repeatingTimerAndCallActivity");
    assertEquals(1, managementService.createJobQuery().count());
    assertEquals(1, taskService.createTaskQuery().count());
    
    // Firing job should cancel the user task, destroy the scope,
    // re-enter the task and recreate the task. A new timer should also be created.
    // This didn't happen before 5.11 (new jobs kept being created). See ACT-1427
    Job job = managementService.createJobQuery().singleResult();
    managementService.executeJob(job.getId());
    assertEquals(1, managementService.createJobQuery().count());
    assertEquals(1, taskService.createTaskQuery().count());
  }

}

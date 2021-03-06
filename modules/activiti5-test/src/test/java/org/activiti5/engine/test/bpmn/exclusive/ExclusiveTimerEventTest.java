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
package org.activiti5.engine.test.bpmn.exclusive;

import java.util.Date;

import org.activiti.engine.runtime.Clock;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.activiti5.engine.impl.test.PluggableActivitiTestCase;


public class ExclusiveTimerEventTest extends PluggableActivitiTestCase {

  @Deployment
  public void testCatchingTimerEvent() throws Exception {
    Clock clock = processEngineConfiguration.getClock();
    // Set the clock fixed
    Date startTime = new Date();
    clock.setCurrentTime(startTime);
    processEngineConfiguration.setClock(clock);

    // After process start, there should be 3 timers created
    ProcessInstance pi = runtimeService.startProcessInstanceByKey("exclusiveTimers");
    JobQuery jobQuery = managementService.createJobQuery().processInstanceId(pi.getId());
    assertEquals(3, jobQuery.count());

    // After setting the clock to time '50minutes and 5 seconds', the timers should fire
    clock.setCurrentTime(new Date(startTime.getTime() + ((50 * 60 * 1000) + 5000)));
    processEngineConfiguration.setClock(clock);
    waitForJobExecutorToProcessAllJobs(5000L, 100L);

    assertEquals(0, jobQuery.count());
    assertProcessEnded(pi.getProcessInstanceId());
    
    processEngineConfiguration.resetClock();
  }
}

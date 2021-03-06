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

package org.activiti.rest.service.api.management;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ManagementService;
import org.activiti.engine.runtime.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Frederik Heremans
 */
@RestController
public class JobExceptionStacktraceResource {

  @Autowired
  protected ManagementService managementService;

  @RequestMapping(value = "/management/jobs/{jobId}/exception-stacktrace", method = RequestMethod.GET)
  public String getJobStacktrace(@PathVariable String jobId, HttpServletResponse response) {
    Job job = getJobFromResponse(jobId);

    String stackTrace = managementService.getJobExceptionStacktrace(job.getId());

    if (stackTrace == null) {
      throw new ActivitiObjectNotFoundException("Job with id '" + job.getId() + "' doesn't have an exception stacktrace.", String.class);
    }

    response.setContentType("text/plain");
    return stackTrace;
  }

  protected Job getJobFromResponse(String jobId) {
    Job job = managementService.createJobQuery().jobId(jobId).singleResult();

    if (job == null) {
      throw new ActivitiObjectNotFoundException("Could not find a job with id '" + jobId + "'.", Job.class);
    }
    return job;
  }
}

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
package org.activiti5.standalone.validation;

import org.activiti.bpmn.exceptions.XMLException;
import org.activiti5.engine.ActivitiException;
import org.activiti5.engine.ProcessEngine;
import org.activiti5.engine.ProcessEngines;
import org.activiti5.engine.RepositoryService;
import org.activiti5.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti5.engine.repository.Deployment;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Joram Barrez
 */
public class DisabledSchemaValidationTest {
	
	protected ProcessEngine processEngine;
	
	protected RepositoryService repositoryService;
	
	@Before
	public void setup() {
		this.processEngine = new StandaloneInMemProcessEngineConfiguration()
			.setJdbcUrl("jdbc:h2:mem:activiti-process-validation;DB_CLOSE_DELAY=1000")
			.buildProcessEngine();
		this.repositoryService = processEngine.getRepositoryService();
	}
	
	@After
	public void tearDown() {
		for (Deployment deployment : repositoryService.createDeploymentQuery().list()) {
			repositoryService.deleteDeployment(deployment.getId());
		}
		
		ProcessEngines.unregister(processEngine);
		processEngine = null;
		repositoryService = null;
	}
	
	@Test
	public void testDisableValidation() {
		
		// Should fail
		try {
			repositoryService.createDeployment()
				.addClasspathResource("org/activiti5/standalone/validation/invalid_process_xsd_error.bpmn20.xml")
				.deploy();
			Assert.fail();
		} catch (XMLException e) {
			// expected exception
		}
		
		// Should fail with validation errors
		try {
			repositoryService.createDeployment()
				.addClasspathResource("org/activiti5/standalone/validation/invalid_process_xsd_error.bpmn20.xml")
				.disableSchemaValidation()
				.deploy();
			Assert.fail();
		} catch (ActivitiException e) {
		  // expected exception
		}
		
	}

}

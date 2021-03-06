package org.activiti5.engine.impl.event.logger.handler;

import java.util.Map;

import org.activiti5.engine.delegate.event.ActivitiVariableEvent;
import org.activiti5.engine.impl.interceptor.CommandContext;
import org.activiti5.engine.impl.persistence.entity.EventLogEntryEntity;

/**
 * @author Joram Barrez
 */
public class VariableCreatedEventHandler extends VariableEventHandler {
	
	@Override
	public EventLogEntryEntity generateEventLogEntry(CommandContext commandContext) {
		ActivitiVariableEvent variableEvent = (ActivitiVariableEvent) event;
		Map<String, Object> data = createData(variableEvent);
		
		data.put(Fields.CREATE_TIME, timeStamp);
		
    return createEventLogEntry(variableEvent.getProcessDefinitionId(), variableEvent.getProcessInstanceId(), 
    		variableEvent.getExecutionId(), variableEvent.getTaskId(), data);
	}

}

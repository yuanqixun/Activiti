/**
 * Activiti app component part of the Activiti project
 * Copyright 2005-2015 Alfresco Software, Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.activiti.model.runtime;

import com.activiti.model.common.AbstractRepresentation;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.activiti.engine.repository.ProcessDefinition;

/**
 * REST representation of a process definition.
 * 
 * @author Tijs Rademakers
 */
public class ProcessDefinitionRepresentation extends AbstractRepresentation {

    protected String id;
    protected String name;
    protected String description;
    protected String key;
    protected String category;
    protected int version;
    protected String deploymentId;
    protected String tenantId;
    protected boolean hasStartForm;
    
    public ProcessDefinitionRepresentation(ProcessDefinition processDefinition) {
        this.id = processDefinition.getId();
        this.name = processDefinition.getName();
        this.description = processDefinition.getDescription();
        this.key = processDefinition.getKey();
        this.category = processDefinition.getCategory();
        this.version = processDefinition.getVersion();
        this.deploymentId = processDefinition.getDeploymentId();
        this.tenantId = processDefinition.getTenantId();
        this.hasStartForm = processDefinition.hasStartFormKey();
    }
    
    public ProcessDefinitionRepresentation() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    
    public void setHasStartForm(boolean hasStartForm) {
        this.hasStartForm = hasStartForm;
    }
    @JsonProperty("hasStartForm")
    public boolean getHasStartForm() {
        return hasStartForm;
    }
    
}

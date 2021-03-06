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
package com.activiti.model.idm;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkUserUpdateRepresentation {

    private String status;
    private String accountType;
    private String password;
    private Long tenantId;
    private boolean sendNotifications = true;
    
    private List<Long> users;
    
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    public String getAccountType() {
        return accountType;
    }
    
    public void setUsers(List<Long> users) {
        this.users = users;
    }
    
    @JsonDeserialize(contentAs=Long.class)
    public List<Long> getUsers() {
        return users;
    }
    
    public void setSendNotifications(boolean sendNotifictions) {
        this.sendNotifications = sendNotifictions;
    }
    
    public boolean isSendNotifications() {
        return sendNotifications;
    }
    
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getTenantId() {
		return tenantId;
	}
	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
	
}

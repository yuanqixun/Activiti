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
package com.activiti.rest.editor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.activiti.model.common.ResultListDataRepresentation;
import com.codahale.metrics.annotation.Timed;

/**
 * Rest resource for managing groups, used in the editor app.
 */
@RestController
public class EditorGroupsResource extends AbstractEditorGroupsResource {
	
    @Timed
    @RequestMapping(value = "/rest/editor-groups", method = RequestMethod.GET)
    public ResultListDataRepresentation getGroups(@RequestParam(required=false, value="filter") String filter) {
        
        return super.getGroups(filter);
    }
}

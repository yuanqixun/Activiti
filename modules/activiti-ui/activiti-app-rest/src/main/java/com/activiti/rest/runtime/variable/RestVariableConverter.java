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
package com.activiti.rest.runtime.variable;

import com.activiti.model.runtime.RestVariable;



/**
 * @author Frederik Heremans
 */
public interface RestVariableConverter {

  /**
   * Simple type-name used by this converter.
   */
  String getRestTypeName();
  
  /**
   * Type of variables this converter is able to convert.
   */
  Class<?> getVariableType();
  
  /**
   * Extract the variable value to be used in the engine from the given {@link RestVariable}. 
   */
  Object getVariableValue(RestVariable result);
  
  /**
   * Converts the given value and sets the converted value in the given {@link RestVariable}.
   */
  void convertVariableValue(Object variableValue, RestVariable result);
}

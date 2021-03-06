/*
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


if(!ORYX){ var ORYX = {} }
if(!ORYX.Plugins){ ORYX.Plugins = {} }

/**
   This abstract plugin implements the core behaviour of layout
   
   @class ORYX.Plugins.AbstractLayouter
   @constructor Creates a new instance
   @author Willi Tscheschner
*/
ORYX.Plugins.AbstractLayouter = ORYX.Plugins.AbstractPlugin.extend({
	
	/**
	 * 'layouted' defined all types of shapes which will be layouted. 
	 * It can be one value or an array of values. The value
	 * can be a Stencil ID (as String) or an class type of either 
	 * a ORYX.Core.Node or ORYX.Core.Edge
     * @type Array|String|Object
     * @memberOf ORYX.Plugins.AbstractLayouter.prototype
	 */
	layouted : [],
	
	/**
	 * Constructor
	 * @param {Object} facade
	 * @memberOf ORYX.Plugins.AbstractLayouter.prototype
	 */
	construct: function( facade ){
		arguments.callee.$.construct.apply(this, arguments);
			
		this.facade.registerOnEvent(ORYX.CONFIG.EVENT_LAYOUT, this._initLayout.bind(this));
	},
	
	/**
	 * Proofs if this shape should be layouted or not
	 * @param {Object} shape
     * @memberOf ORYX.Plugins.AbstractLayouter.prototype
	 */
	isIncludedInLayout: function(shape){
		if (!(this.layouted instanceof Array)){
			this.layouted = [this.layouted].compact();
		}
		
		// If there are no elements
		if (this.layouted.length <= 0) {
			// Return TRUE
			return true;
		}
		
		// Return TRUE if there is any correlation between 
		// the 'layouted' attribute and the shape themselve.
		return this.layouted.any(function(s){
			if (typeof s == "string") {
				return shape.getStencil().id().include(s);
			} else {
				return shape instanceof s;
			}
		})
	},
	
	/**
	 * Callback to start the layouting
	 * @param {Object} event Layout event
	 * @param {Object} shapes Given shapes
     * @memberOf ORYX.Plugins.AbstractLayouter.prototype
	 */
	_initLayout: function(event){
		
		// Get the shapes
		var shapes = [event.shapes].flatten().compact();
		
		// Find all shapes which should be layouted
		var toLayout = shapes.findAll(function(shape){
			return this.isIncludedInLayout(shape) 
		}.bind(this))
		
		// If there are shapes left 
		if (toLayout.length > 0){
			// Do layout
			this.layout(toLayout);
		}
	},
	
	/**
	 * Implementation of layouting a set on shapes
	 * @param {Object} shapes Given shapes
     * @memberOf ORYX.Plugins.AbstractLayouter.prototype
	 */
	layout: function(shapes){
		throw new Error("Layouter has to implement the layout function.")
	}
});
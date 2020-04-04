package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.geometry.Geometry;
import com.dddviewr.collada.content.geometry.LibraryGeometries;
import com.dddviewr.collada.content.lights.LibraryLights;
import com.dddviewr.collada.content.lights.Light;

public class light extends State {
	protected Light lgt;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		String id = attrs.getValue("id");
		String lgtName = attrs.getValue("name");

		this.lgt = new Light(id, lgtName);
		LibraryLights lib = ((library_lights) getParent()).getLibrary();
		lib.addElement(this.lgt);
	}

	public Light getLight() {
		return this.lgt;
	}
}
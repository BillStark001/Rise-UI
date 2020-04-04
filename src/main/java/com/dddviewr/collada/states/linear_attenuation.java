package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.lights.Color;
import com.dddviewr.collada.content.lights.Light;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.Translate;

public class linear_attenuation extends State {

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		float needed = Float.parseFloat(this.content.toString());
		Light node = ((light) (this.getParent().getParent().getParent())).getLight();
		node.linear_attenuation = needed;
		super.endElement(name);
	}


}
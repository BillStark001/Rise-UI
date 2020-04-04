package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.lights.Color;
import com.dddviewr.collada.content.lights.Light;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.Translate;

public class color extends State {
	protected Color theColor;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theColor = new Color();
		Light node = ((light) (this.getParent().getParent().getParent())).getLight();
		String type = this.getParent().getName();
		node.setColor(theColor);
		node.setType(type);
		setContentNeeded(true);
	}

	public void endElement(String name) {
		this.theColor.parse(this.content);
		super.endElement(name);
	}

	public Color getColor() {
		return this.theColor;
	}
}
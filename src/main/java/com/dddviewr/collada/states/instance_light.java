package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.InstanceLight;

public class instance_light  extends State {
	protected InstanceLight instanceLight;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceLight = new InstanceLight(attrs.getValue("url"));
		Node node = ((node) getParent()).getNode();
		node.setInstanceLight(this.instanceLight);
	}

	public InstanceLight getInstanceLight() {
		return this.instanceLight;
	}
}
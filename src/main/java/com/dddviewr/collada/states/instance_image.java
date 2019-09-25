package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.effects.InstanceImage;
import com.dddviewr.collada.content.materials.InstanceEffect;


public class instance_image extends State {
	protected InstanceImage instanceImage;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.instanceImage = new InstanceImage(attrs.getValue("url"));
		((sampler2D) getParent()).getSampler2D().setInstanceImage(
				this.instanceImage);
	}
}
package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.animation.Animation;
import com.dddviewr.collada.content.animation.Channel;

public class channel extends State {
	protected Channel theChannel;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theChannel = new Channel(attrs.getValue("source"), attrs
				.getValue("target"));
		animation parent = (animation) getParent();
		Animation anim = parent.getAnimation();
		anim = new Animation(anim, this.theChannel.getId());
		anim.setChannel(this.theChannel);
		parent.setAnimation(anim);
		parent.addToLibrary();
	}
}
package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Source;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.animation.Animation;
import com.dddviewr.collada.content.animation.LibraryAnimations;

public class animation extends State {
	protected Animation theAnimation;
	protected LibraryAnimations library = null;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.theAnimation = new Animation();

		State cache = this;
		boolean flag = false;
		while (!flag) {
			cache = cache.getParent();
			try {
				library = ((library_animations) cache).getLibrary();
				flag = true;
			} catch (ClassCastException e) {
				flag = false;
			}
		}
	}

	public void addSource(Source src) {
		this.theAnimation.addSource(src);
	}

	public Animation getAnimation() {
		return this.theAnimation;
	}
	
	public void setAnimation(Animation ani) {
		this.theAnimation = ani;
	}
	
	public void addToLibrary() {
		library.addElement(this.theAnimation);
	}
}
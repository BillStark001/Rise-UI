package com.dddviewr.collada.states;

import java.lang.reflect.Method;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.effects.InstanceImage;
import com.dddviewr.collada.content.materials.InstanceEffect;


public class ref extends State {
	
	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.setContentNeeded(true);
	}
	
	public void endElement(String name) {
		State grandparent = getParent().getParent();
		try {
			Method method = grandparent.getClass().getMethod("setInitFrom",
					new Class[] { String.class });
			method.invoke(grandparent, new Object[] { this.content.toString() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.endElement(name);
	}
}
package com.dddviewr.collada.states;

import java.lang.reflect.Method;
import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;

public class init_from extends State {
	
	private boolean ref_flag;
	
	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		//setContentNeeded(true);
		this.ref_flag = false;
	}

	public void endElement(String name) {
		if (this.ref_flag) {
			super.endElement(name);
			return;
		}
		State parent = getParent();
		try {
			Method method = parent.getClass().getMethod("setInitFrom",
					new Class[] { String.class });
			method.invoke(parent, new Object[] { this.content.toString() });
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.endElement(name);
	}
	
	public void startElement(String name, Attributes attrs) {
		if (name.equals("ref"))
			ref_flag = true;
		else
			this.setContentNeeded(true);
		super.startElement(name, attrs);
	}
}
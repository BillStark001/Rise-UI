package com.dddviewr.collada.content.controller;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Controller extends Base {
	protected String name;
	protected Skin skin;

	public Controller(String id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Skin getSkin() {
		return this.skin;
	}

	public void setSkin(Skin skin) {
		this.skin = skin;
		skin.setController(this);
	}
	
	public String toString() {
		return "Controller (id: " + this.getId() + ", name: " + this.name + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.skin != null)
			this.skin.dump(out, indent + 1);
	}
}
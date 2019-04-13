package com.dddviewr.collada.content.images;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Image extends Base {
	protected String name;
	protected String initFrom;

	public Image(String id, String name) {
		super(id);
		this.name = name;
	}

	public String getInitFrom() {
		return this.initFrom;
	}

	public void setInitFrom(String initFrom) {
		this.initFrom = initFrom;
	}
	
	public String toString() {
		return "Image (id: " + this.getId() + ", name: " + this.name + ")";
	}
	
	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.initFrom != null)
			out.println(prefix + " InitFrom: " + this.initFrom);
	}
}
package com.dddviewr.collada.content.effects;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Surface extends Base {
	protected String type;
	protected String initFrom;
	protected String format;

	public Surface(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInitFrom() {
		return this.initFrom;
	}

	public void setInitFrom(String initFrom) {
		this.initFrom = initFrom;
	}

	public String getFormat() {
		return this.format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	public String toString() {
		return "Surface (type: " + this.type + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.initFrom != null) {
			out.println(prefix + " InitFrom: " + this.initFrom);
		}
		if (this.format != null)
			out.println(prefix + " Format: " + this.format);
	}
}
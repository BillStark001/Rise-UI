package com.dddviewr.collada.content.visualscene;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class InstanceNode  extends Base {
	protected String url;

	public InstanceNode(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceNode (url: " + this.url + ")");
	}
}

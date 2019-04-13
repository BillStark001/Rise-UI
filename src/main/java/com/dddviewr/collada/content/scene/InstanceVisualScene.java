package com.dddviewr.collada.content.scene;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class InstanceVisualScene extends Base {
	
	protected int url;

	public InstanceVisualScene(int url) {this.setUrl(url);}
	public InstanceVisualScene(String url) {this.setUrl(url);}

	public int getUrl() {
		return this.url;
	}

	public void setUrl(int url) {this.url = url;}
	public void setUrl(String s) {
		if (s.startsWith("#ID")) setUrl(Integer.parseInt(s.substring(3)));
		else if (s.startsWith("ID")) setUrl(Integer.parseInt(s.substring(2)));
		else setUrl(Integer.parseInt(s));
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceVisualScene (url: " + this.url + ")");
	}
}
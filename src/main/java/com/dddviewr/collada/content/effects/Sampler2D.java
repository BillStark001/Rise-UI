package com.dddviewr.collada.content.effects;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Sampler2D extends Base {
	protected String source;
	protected String minFilter;
	protected String magFilter;
	protected InstanceImage instanceImage;

	public InstanceImage getInstanceImage() {
		return instanceImage;
	}

	public void setInstanceImage(InstanceImage instanceImage) {
		this.instanceImage = instanceImage;
	}

	public String getMagFilter() {
		return this.magFilter;
	}

	public void setMagFilter(String magFilter) {
		this.magFilter = magFilter;
	}

	public String getMinFilter() {
		return this.minFilter;
	}

	public void setMinFilter(String minFilter) {
		this.minFilter = minFilter;
	}

	public int getSource() {
		return this.instanceImage.getUrl();
	}

	public void setSource(String source) {
		//this.source = source;
		this.instanceImage = new InstanceImage(source);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Sampler2D");

		if (this.source != null) {
			out.println(prefix + " Source: " + this.source);
		}
		if (this.minFilter != null) {
			out.println(prefix + " MinFilter: " + this.minFilter);
		}
		if (this.magFilter != null)
			out.println(prefix + " MagFilter: " + this.magFilter);
	}
}
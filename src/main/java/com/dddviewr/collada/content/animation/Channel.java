package com.dddviewr.collada.content.animation;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Channel extends Base {

	protected String target;

	public Channel(String source, String target) {
		super(source);
		this.target = target;
	}

	public int getSource() {
		return this.getId();
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String toString() {
		return "Channel (source: " + this.getId() + ", target: " + this.target + ")";
	}
}
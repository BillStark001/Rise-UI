package com.dddviewr.collada;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Input extends Base {
	protected String semantic;
	protected int source;
	protected int offset = -1;

	protected int set = -1;

	public Input(String semantic, int source) {
		super(source);
		this.source = this.getId();
		this.semantic = semantic;
	}
	
	public Input(String semantic, String source) {
		super(source);
		this.source = this.getId();
		this.semantic = semantic;
	}

	public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getSemantic() {
		return this.semantic;
	}

	public void setSemantic(String semantic) {
		this.semantic = semantic;
	}

	public int getSet() {
		return this.set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public int getSource() {
		return this.source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String toString() {
		return String.format("Input (semantic: %s, source: %s, set: %d, offset: %d)",
				this.semantic, this.source, this.set, this.offset);
	}
}
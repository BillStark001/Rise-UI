package com.dddviewr.collada.content.controller;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.format.Base;

public class Joints extends Base {
	protected List<Input> inputs = new ArrayList<Input>();

	public List<Input> getInputs() {
		return this.inputs;
	}
	
	public String toString() {
		return "Joints()";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		Base.dump(out, indent, inputs);
	}

	public void addInput(Input inp) {
		this.inputs.add(inp);
	}
}
package com.dddviewr.collada.content.geometry;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.format.Base;

public class Vertices extends Base {
	protected List<Input> inputs = new ArrayList<Input>();

	public Vertices(String id) {
		super(id);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		Base.dump(out, indent, this.inputs);
	}

	public void addInput(Input i) {
		this.inputs.add(i);
	}

	public List<Input> getInputs() {
		return this.inputs;
	}
}
package com.dddviewr.collada.content.animation;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.format.Base;

public class Sampler extends Base {
	
	public Sampler(int id) {super(id);}
	public Sampler(String id) {super(id);}
	
	List<Input> inputs = new ArrayList<Input>();
	
	public List<Input> getInputs() {return this.inputs;}
	public Input getInput(int index) {return this.inputs.get(index);}
	public void addInput(Input inp) {this.inputs.add(inp);}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		for (Input inp : this.inputs)
			inp.dump(out, indent + 1);
	}
	
}
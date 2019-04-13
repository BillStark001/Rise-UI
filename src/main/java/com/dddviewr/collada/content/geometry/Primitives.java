package com.dddviewr.collada.content.geometry;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.format.Base;


public abstract class Primitives extends Base {

	protected String material;
	protected List<Input> inputs = new ArrayList<Input>();
	protected int count;
	protected int[] data;

	
	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public void addInput(Input i) {
		this.inputs.add(i);
	}

	public List<Input> getInputs() {
		return new ArrayList<Input>(this.inputs);
	}

	public int getStride() {
		int maxOffset = -1;
		for (Input i : this.inputs) {
			if (i.getOffset() > maxOffset)
				maxOffset = i.getOffset();
		}
		return (maxOffset + 1);
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}	
	
	public String toString() {
		return this.getClass().getSimpleName() + " (material: " + this.material
				+ ", count: " + this.count + ")";
	}
	
	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		Base.dump(out, indent, this.inputs);
		if (data != null) {
			out.print(prefix);
			for (int d : data) {
				out.print(" " + d);
			}
			out.println();
		}
	}
}

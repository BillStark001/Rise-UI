package com.dddviewr.collada;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.format.Base;

public class Accessor extends Base {
	protected int count;
	protected int stride;
	protected List<Param> params = new ArrayList<Param>();

	public Accessor(String source, int count, int stride) {
		super(source);
		this.count = count;
		this.stride = stride;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSource() {
		return this.getId();
	}

	public int getStride() {
		return this.stride;
	}

	public void setStride(int stride) {
		this.stride = stride;
	}

	public void addParam(Param param) {
		this.params.add(param);
	}

	public Param getParam(int index) {
		return ((Param) this.params.get(index));
	}

	public List<Param> getParams() {
		return this.params;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Accessor (source: " + this.getId() + ", count: "
				+ this.count + ", stride: " + this.stride + ")");
		for (Param p : this.params)
			p.dump(out, indent + 1);
	}
}
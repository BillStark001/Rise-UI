package com.dddviewr.collada;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class IdrefArray extends Base {
	protected int count;
	protected String[] data;

	public IdrefArray(String id, int count) {
		super(id);
		this.count = count;
		this.data = new String[count];
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getData() {
		return this.data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		for (String s : values) {
			if (index >= this.count)
				return;
			if (s.length() != 0)
				this.data[(index++)] = s;
		}
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "IdrefArray (id: " + this.getId() + ", count: "
				+ this.count + ")");
		if (this.data != null) {
			out.print(prefix);
			for (String str : this.data) {
				out.print(" " + str);
			}
			out.println();
		}
	}
}
package com.dddviewr.collada;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Vcount extends Base {
	protected int[] data, acc_data;

	public int[] getData() {
		return this.data;
	}
	
	public int[] getAccData() {
		return this.acc_data;
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		this.data = new int[values.length];
		this.acc_data = new int[values.length];
		for (String s : values)
			if (s.length() != 0){
				int stemp = Integer.parseInt(s);
				this.data[index] = stemp;
				if (index == 0) this.acc_data[0] = stemp;
				else this.acc_data[index] = stemp + this.acc_data[index - 1];
			}
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "Vcount");
		if (this.data != null) {
			out.print(prefix);
			for (int i : this.data) {
				out.print(" " + i);
			}
			out.println();
		}
	}

	public void setData(int[] values) {
		this.data = new int[values.length];
		this.acc_data = new int[values.length];
		int index = 0;
		for (int i : values) {
			this.data[index] = i;
			if (index == 0) this.acc_data[0] = i;
			else this.acc_data[index] = i + this.acc_data[index - 1];
		}
	}
}
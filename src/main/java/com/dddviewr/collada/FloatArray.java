package com.dddviewr.collada;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;
import com.dddviewr.log.Log;


public class FloatArray extends Base {
	
	protected int count;
	protected float[] data;

	public FloatArray(String id, int count) {
		super(id);
		this.count = count;
		this.data = new float[count];
	}

	public void set(int index, float value) {
		this.data[index] = value;
	}

	public float get(int index) {
		return this.data[index];
	}

	public float[] getData() {
		return this.data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		for (String s : values) {
			if (index >= count)
				return;
			if (s.length() != 0) {
				try {
					data[(index++)] = Float.parseFloat(s);
				} catch (NumberFormatException e) {
					Log.log("string: " + s);
					throw e;
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return "FloatArray (id: " + this.getId() + ", count: " + count + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this.toString());
		out.print(prefix);
		for (float f : data)
			out.print(" " + f);
		out.println();
	}
}
package com.dddviewr.collada.content.lights;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;


public class Color extends Base{
	protected float[] data = new float[3];

	public Color() {}

	public float[] getData() {
		return this.data;
	}

	public float getR() {
		return this.data[0];
	}

	public float getG() {
		return this.data[1];
	}

	public float getB() {
		return this.data[2];
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.print(prefix + "Color (data:");
		for (float f : this.data) {
			out.print(" " + f);
		}
		out.println(")");
	}

	public void parse(StringBuilder str) {
		String[] values = str.toString().split("\\s+");
		int index = 0;
		for (String s : values) {
			if (index >= 3)
				return;
			this.data[(index++)] = Float.parseFloat(s);
		}
	}
}
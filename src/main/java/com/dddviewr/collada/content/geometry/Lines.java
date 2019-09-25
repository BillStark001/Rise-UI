package com.dddviewr.collada.content.geometry;

import java.io.PrintStream;

import com.dddviewr.collada.Input;
import com.dddviewr.collada.format.Base;

public class Lines extends Primitives {
	private int pos;

	public Lines(String string, int count) {
		this.setMaterial(string);
		this.count = count;
	}

	public void addData(StringBuilder str) {
		if (this.data == null) {
			this.data = new int[this.count * 3 * getStride()];
			this.pos = 0;
		}

		String[] vals = str.toString().split("\\s+");
		for (String s : vals) {
			if (this.pos >= this.data.length)
				return;
			if (s.length() != 0)
				this.data[(this.pos++)] = Integer.parseInt(s);
		}
	}

}

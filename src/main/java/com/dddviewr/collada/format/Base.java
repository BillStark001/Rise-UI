package com.dddviewr.collada.format;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import com.dddviewr.collada.Input;

public abstract class Base {
	
	private final int id;
	
	public Base() {this.id = 0;}
	public Base(int id) {this.id = id;}
	public Base(String id) {
		if (id.startsWith("#ID")) this.id = Integer.parseInt(id.substring(3));
		else if (id.startsWith("ID")) this.id = Integer.parseInt(id.substring(2));
		else this.id = Integer.parseInt(id);
	}
	public final int getId() {return this.id;}
	
	public final String createIndent(int count) {
		String spaces = "                ";
		String indent = "";
		while (indent.length() < (count & 0xF0))
			indent = indent + spaces;
		indent = indent + spaces.substring(0, count & 0xF);
		return indent;
	}
	
	@Override
	public String toString() {
		return String.format("%s (id: %d)", 
				this.getClass().getSimpleName(), this.getId());
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
	}
	
	public static void dump(PrintStream out, int indent, Collection<? extends Base> c) {
		for (Base inp : c)
			inp.dump(out, indent + 1);
	}
	
	public static void dump(PrintStream out, int indent, List<? extends Base> c) {
		for (Base inp : c)
			inp.dump(out, indent + 1);
	}
}
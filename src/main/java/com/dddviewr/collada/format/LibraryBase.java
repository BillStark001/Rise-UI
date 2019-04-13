package com.dddviewr.collada.format;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dddviewr.collada.format.Base;

public class LibraryBase<T extends Base> extends Base {
	private Map<Integer, T> elements = new HashMap<Integer, T>();

	public void addElement(T vs) {this.elements.put(vs.getId(), vs);}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public final void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		for (T vs : this.elements.values())
			vs.dump(out, indent + 1);
	}

	public T getElement(int id) {
		return this.elements.getOrDefault(id, null);
	}
	
	public List<T> getElements() {
		return new ArrayList<T>(this.elements.values());
	}
}
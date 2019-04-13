package com.dddviewr.collada;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Source extends Base {
	protected String name;
	protected FloatArray floatArray;
	protected IdrefArray idrefArray;
	protected NameArray nameArray;
	protected Accessor accessor;

	public Source(String id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FloatArray getFloatArray() {
		return this.floatArray;
	}

	public void setFloatArray(FloatArray floatArray) {
		this.floatArray = floatArray;
	}

	public IdrefArray getIdrefArray() {
		return this.idrefArray;
	}

	public void setIdrefArray(IdrefArray idrefArray) {
		this.idrefArray = idrefArray;
	}

	public NameArray getNameArray() {
		return this.nameArray;
	}

	public void setNameArray(NameArray nameArray) {
		this.nameArray = nameArray;
	}

	public Accessor getAccessor() {
		return this.accessor;
	}

	public void setAccessor(Accessor accessor) {
		this.accessor = accessor;
	}
	
	public String toString() {
		return "Source (id: " + this.getId() + ", name: " + this.name + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.floatArray != null)
			this.floatArray.dump(out, indent + 1);
		if (this.idrefArray != null)
			this.idrefArray.dump(out, indent + 1);
		if (this.nameArray != null)
			this.nameArray.dump(out, indent + 1);
		if (this.accessor != null)
			this.accessor.dump(out, indent + 1);
	}
}
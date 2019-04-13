package com.dddviewr.collada.content.geometry;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Geometry extends Base {
	protected String id;
	protected String name;
	protected Mesh mesh;

	public Geometry(String id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
		mesh.setGeometry(this);
	}

	public Mesh getMesh() {
		return this.mesh;
	}
	
	public String toString() {
		return "Geometry (id: " + this.id + ", name: "
				+ this.name + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.mesh != null)
			this.mesh.dump(out, indent + 1);
	}
}
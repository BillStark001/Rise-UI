package com.dddviewr.collada.content.materials;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Material extends Base {
	protected String name;
	protected InstanceEffect instanceEffect;

	public Material(String id, String name) {
		super(id);
		this.name = name;
	}

	public InstanceEffect getInstanceEffect() {
		return this.instanceEffect;
	}

	public void setInstanceEffect(InstanceEffect instanceEffect) {
		this.instanceEffect = instanceEffect;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return "Material (id: " + this.getId() + ", name: "
				+ this.name + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.instanceEffect != null)
			this.instanceEffect.dump(out, indent + 1);
	}
}
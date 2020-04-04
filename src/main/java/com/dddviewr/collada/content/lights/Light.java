package com.dddviewr.collada.content.lights;

import java.io.PrintStream;

import com.dddviewr.collada.content.visualscene.InstanceLight;
import com.dddviewr.collada.format.Base;

public class Light extends Base {
	protected String name;
	protected String type;
	protected InstanceLight instanceLight;
	protected Color color;
	public float constant_attenuation = 1;
	public float linear_attenuation = 0;
	public float quadratic_attenuation = 0;
	public float falloff_angle = 180;
	public float falloff_exponent = 0;

	public Light(String id, String name) {
		super(id);
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public InstanceLight getInstanceLight() {
		return this.instanceLight;
	}

	public void setInstanceLight(InstanceLight light) {
		this.instanceLight = light;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return "Light (id: " + this.getId() + ", name: "
				+ this.name + ")";
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + this);
		if (this.instanceLight != null)
			this.instanceLight.dump(out, indent + 1);
	}
}
package com.dddviewr.collada.content.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.format.Base;

public class InstanceGeometry extends Base {
	protected List<InstanceMaterial> instanceMaterials = new ArrayList<InstanceMaterial>();

	public InstanceGeometry(String url) {
		super(url);
	}

	public int getUrl() {
		return this.getId();
	}

	public List<InstanceMaterial> getInstanceMaterials() {
		return this.instanceMaterials;
	}

	public void addInstanceMaterial(InstanceMaterial mat) {
		this.instanceMaterials.add(mat);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceGeometry (url: " + this.getUrl() + ")");

		for (InstanceMaterial mat : this.instanceMaterials)
			mat.dump(out, indent + 1);
	}
}
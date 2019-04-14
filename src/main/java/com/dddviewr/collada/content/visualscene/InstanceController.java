package com.dddviewr.collada.content.visualscene;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.format.Base;

public class InstanceController extends Base {
	protected int skeleton;
	protected List<InstanceMaterial> instanceMaterials = new ArrayList<InstanceMaterial>();

	public InstanceController(String url) {
		super(url);
	}

	public int getUrl() {
		return this.getId();
	}

	public int getSkeleton() {
		return this.skeleton;
	}

	public void setSkeleton(int skeleton) {
		this.skeleton = skeleton;
	}
	
	public void setSkeleton(String skeleton) {
		this.skeleton = new Base(skeleton).getId();
	}

	public List<InstanceMaterial> getInstanceMaterials() {
		return this.instanceMaterials;
	}

	public void addInstanceMaterial(InstanceMaterial mat) {
		this.instanceMaterials.add(mat);
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "InstanceController (url: " + this.getUrl() + ")");
		if (this.skeleton != 0) {
			out.println(prefix + " Skeleton: " + this.skeleton);
		}
		for (InstanceMaterial mat : this.instanceMaterials)
			mat.dump(out, indent + 1);
	}
}
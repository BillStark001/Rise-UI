package com.dddviewr.collada.content.materials;

import com.dddviewr.collada.format.Base;

public class InstanceEffect extends Base {
	public InstanceEffect(String url) {super(url);}
	public int getUrl() {return this.getId();}
	public String toString() {
		return "InstanceEffect (url: " + this.getId() + ")";
	}
}
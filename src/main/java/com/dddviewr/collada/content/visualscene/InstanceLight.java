package com.dddviewr.collada.content.visualscene;

import com.dddviewr.collada.format.Base;

public class InstanceLight extends Base {
	public InstanceLight(String url) {super(url);}
	public int getUrl() {return this.getId();}
	public String toString() {
		return "InstanceLight (url: " + this.getId() + ")";
	}
}
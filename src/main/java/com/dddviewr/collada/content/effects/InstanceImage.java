package com.dddviewr.collada.content.effects;

import com.dddviewr.collada.format.Base;

public class InstanceImage extends Base {
	public InstanceImage(String url) {super(url);}
	public int getUrl() {return this.getId();}
	public String toString() {
		return "InstanceImage (url: " + this.getId() + ")";
	}
}
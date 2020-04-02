package com.billstark001.riseui.base.shading.mat;

import net.minecraft.util.ResourceLocation;

public class Texture2DFromRes extends Texture2DBase {

	private ResourceLocation loc;
	public void setLocation(ResourceLocation loc) {
		if (this == Texture2DBase.MISSING || this == Texture2DBase.INEXISTENT)
			return;
		else
			this.loc = loc;
	}
	public void setLocation(String loc) {this.setLocation(new ResourceLocation(loc));}
	public ResourceLocation getLocation() {
		if (this.loc == null) return Texture2DBase.MISSING.getLocation();
		else return this.loc;
	}
	
	public Texture2DFromRes(ResourceLocation loc) {this.setLocation(loc);}
	public Texture2DFromRes(String loc) {this(new ResourceLocation(loc));}
	public Texture2DFromRes() {this(Texture2DBase.MISSING.getLocation());}
	
	@Override
	public int allocNewTexId() {
		return -1;
	}
	
	@Override
	public boolean getTextureARGB(int y_start, int y_length, int[] cache, int offset) {
		return true;
	}
	
	@Override
	public boolean render() {
		return true;
	}
	
	@Override
	public boolean equals(Object t) {
		if (this == t) {
			return true;
		} else if (!(t instanceof Texture2DFromRes)) {
			return false;
		} else {
			return this.getLocation().equals(((Texture2DFromRes) t).getLocation());
		}
	}

}

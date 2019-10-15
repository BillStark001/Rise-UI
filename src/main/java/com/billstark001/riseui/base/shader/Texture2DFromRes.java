package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Pair;

import net.minecraft.client.resources.data.TextureMetadataSection;
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
	public Texture2DFromRes() {}
	
	@Override
	public int checkAndRender() {
		return -1;
	}
	
	@Override
	public int getRenderedId() {
		return -1;
	}
	
	@Override
	public Matrix render(double time) {
		return null;
	}
	
	@Override
	public void bindTexture() {
		GlRenderHelper.getInstance().bindTexture(this.getLocation());
	}

}
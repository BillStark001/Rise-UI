package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Pair;

import net.minecraft.util.ResourceLocation;

public class Texture2DFromRes extends Texture2DBase {

	
	public Texture2DFromRes(ResourceLocation loc) {
		
	}
	public Texture2DFromRes(String loc) {this(new ResourceLocation(loc));}
	
	@Override
	public Matrix render(double time) {
		return null;
	}

}

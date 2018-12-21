package com.billstark001.riseui.material;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class BaseMaterial {

	private ResourceLocation albedo_tex;
	
	public static final BaseMaterial INEXISTENT = new BaseMaterial("riseui:inexistent_texture");
	public static final BaseMaterial MISSING = new BaseMaterial("riseui:missing_texture");
	
	public BaseMaterial() {
		
	}
	
	public BaseMaterial(String albedo_tex) {
		this.albedo_tex = new ResourceLocation(albedo_tex);
	}
	
	public BaseMaterial(ResourceLocation albedo_tex) {
		this.albedo_tex = albedo_tex;
	}

	public ResourceLocation getAlbedoTexture() {
		return albedo_tex;
	}

	public void setAlbedoTexture(ResourceLocation albedo_tex) {
		this.albedo_tex = albedo_tex;
	}
	
	public void applyOn(TextureManager M) {
		//if(!(this == INEXISTENT || this == MISSING))System.out.println(this);
		M.bindTexture(albedo_tex);
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", this.getClass().getSimpleName(), albedo_tex.toString());
	}

}

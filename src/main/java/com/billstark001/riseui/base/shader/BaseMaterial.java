package com.billstark001.riseui.base.shader;

import java.io.FileNotFoundException;

import com.billstark001.riseui.base.BaseObject;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class BaseMaterial extends BaseObject{

	private ResourceLocation diffuse_tex;
	private ResourceLocation emit_tex;
	
	public static final BaseMaterial INEXISTENT = new BaseMaterial("riseui:inexistent_texture");
	public static final BaseMaterial MISSING = new BaseMaterial("riseui:missing_texture");
	
	public BaseMaterial() {
		
	}
	
	public BaseMaterial(String diffuse_tex, String emit_tex) {
		this.emit_tex = new ResourceLocation(emit_tex);
		this.diffuse_tex = new ResourceLocation(diffuse_tex);
	}
	
	public BaseMaterial(String albedo_tex) {
		this.diffuse_tex = new ResourceLocation(albedo_tex);
	}
	
	public BaseMaterial(ResourceLocation albedo_tex) {
		this.diffuse_tex = albedo_tex;
	}

	public ResourceLocation getAlbedoTexture() {
		return diffuse_tex;
	}

	public void setAlbedoTexture(ResourceLocation albedo_tex) {
		this.diffuse_tex = albedo_tex;
	}
	
	public void applyOn(TextureManager M) {
		M.bindTexture(diffuse_tex);
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s)", this.getClass().getSimpleName(), diffuse_tex.toString());
	}

}

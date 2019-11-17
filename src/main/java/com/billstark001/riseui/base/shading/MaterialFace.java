package com.billstark001.riseui.base.shading;

import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.Layer;

public class MaterialFace extends BaseObject{
	
	public static final MaterialFace DEFAULT = new MaterialFace("Default Material");
	
	@Override
	public void setName(String name) {
		if (this != MaterialFace.DEFAULT) super.setName(name);
	}
	public boolean setLayer(Layer layer) {
		if (this != MaterialFace.DEFAULT) return super.setLayer(layer);
		else return false;
	}

	public MaterialFace(String name, Layer layer) {super(name, layer);}
	public MaterialFace(String name) {this(name, null);}
	public MaterialFace(Layer layer) {this(null, layer);}
	public MaterialFace() {this(null, null);}
	
	protected Texture2DBase albedo;
	protected Texture2DBase emission;
	protected Texture2DBase transparency;
	protected Texture2DBase specular;
	protected Texture2DBase shininess;
	protected Texture2DBase normal;
	protected Texture2DBase displacement;
	
	public Texture2DBase getAlbedo() {
		return albedo;
	}
	public MaterialFace setAlbedo(Texture2DBase albedo) {
		this.albedo = albedo;
		return this;
	}
	public Texture2DBase getEmission() {
		return emission;
	}
	public MaterialFace setEmission(Texture2DBase emission) {
		this.emission = emission;
		return this;
	}
	public Texture2DBase getTransparency() {
		return transparency;
	}
	public MaterialFace setTransparency(Texture2DBase transparency) {
		this.transparency = transparency;
		return this;
	}
	public Texture2DBase getSpecular() {
		return specular;
	}
	public MaterialFace setSpecular(Texture2DBase specular) {
		this.specular = specular;
		return this;
	}
	public Texture2DBase getShininess() {
		return shininess;
	}
	public MaterialFace setShininess(Texture2DBase shininess) {
		this.shininess = shininess;
		return this;
	}
	public Texture2DBase getNormal() {
		return normal;
	}
	public MaterialFace setNormal(Texture2DBase normal) {
		this.normal = normal;
		return this;
	}
	public Texture2DBase getDisplacement() {
		return displacement;
	}
	public MaterialFace setDisplacement(Texture2DBase displacement) {
		this.displacement = displacement;
		return this;
	}

}
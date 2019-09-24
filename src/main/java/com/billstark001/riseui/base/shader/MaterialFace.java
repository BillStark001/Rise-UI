package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.Layer;

public class MaterialFace extends BaseObject{
	
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
	public void setAlbedo(Texture2DBase albedo) {
		this.albedo = albedo;
	}
	public Texture2DBase getEmission() {
		return emission;
	}
	public void setEmission(Texture2DBase emission) {
		this.emission = emission;
	}
	public Texture2DBase getTransparency() {
		return transparency;
	}
	public void setTransparency(Texture2DBase transparency) {
		this.transparency = transparency;
	}
	public Texture2DBase getSpecular() {
		return specular;
	}
	public void setSpecular(Texture2DBase specular) {
		this.specular = specular;
	}
	public Texture2DBase getShininess() {
		return shininess;
	}
	public void setShininess(Texture2DBase shininess) {
		this.shininess = shininess;
	}
	public Texture2DBase getNormal() {
		return normal;
	}
	public void setNormal(Texture2DBase normal) {
		this.normal = normal;
	}
	public Texture2DBase getDisplacement() {
		return displacement;
	}
	public void setDisplacement(Texture2DBase displacement) {
		this.displacement = displacement;
	}

}
package com.billstark001.riseui.base.shader;

public class BaseEffect {
	protected BaseShader emission;
	protected BaseShader ambient;
	protected BaseShader diffuse;
	protected BaseShader specular;
	protected BaseShader shininess;
	protected BaseShader reflective;
	protected BaseShader reflectivity;
	protected BaseShader transparent;
	protected BaseShader transparency;
	
	protected BaseShader bump;

	public BaseShader getAmbient() {
		return this.ambient;
	}

	public void setAmbient(BaseShader ambient) {
		this.ambient = ambient;
	}

	public BaseShader getEmission() {
		return this.emission;
	}

	public void setEmission(BaseShader emission) {
		this.emission = emission;
	}

	public BaseShader getDiffuse() {
		return this.diffuse;
	}

	public void setDiffuse(BaseShader diffuse) {
		this.diffuse = diffuse;
	}

	public BaseShader getReflective() {
		return this.reflective;
	}

	public void setReflective(BaseShader reflective) {
		this.reflective = reflective;
	}

	public BaseShader getReflectivity() {
		return this.reflectivity;
	}

	public void setReflectivity(BaseShader reflectivity) {
		this.reflectivity = reflectivity;
	}

	public BaseShader getShininess() {
		return this.shininess;
	}

	public void setShininess(BaseShader shininess) {
		this.shininess = shininess;
	}

	public BaseShader getSpecular() {
		return this.specular;
	}

	public void setSpecular(BaseShader specular) {
		this.specular = specular;
	}

	public BaseShader getTransparency() {
		return this.transparency;
	}

	public void setTransparency(BaseShader transparency) {
		this.transparency = transparency;
	}

	public BaseShader getTransparent() {
		return this.transparent;
	}

	public void setTransparent(BaseShader transparent) {
		this.transparent = transparent;
	}

	public BaseShader getBump() {
		return bump;
	}

	public void setBump(BaseShader bump) {
		this.bump = bump;
	}

}
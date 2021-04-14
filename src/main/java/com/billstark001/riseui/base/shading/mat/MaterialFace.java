package com.billstark001.riseui.base.shading.mat;

import java.util.LinkedHashMap;
import java.util.Map;

import com.billstark001.riseui.base.BaseContainer;
import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.base.shading.shader.Shader;

public class MaterialFace extends BaseContainer{
	
	public static final MaterialFace DEFAULT = new MaterialFace("Default Material");
	
	public static final Shader SHADER_DIFFUSE = Shader.SHADER_DIFFUSE;
	public static final Shader SHADER_LIGHT = Shader.SHADER_LIGHT;
	public static final Shader SHADER_SPECULAR = Shader.SHADER_SPECULAR;
	public static final Shader SHADER_NORMAL = Shader.SHADER_NORMAL;
	public static final Shader SHADER_ALPHA = Shader.SHADER_ALPHA;
	public static final Shader SHADER_DISPLACEMENT = Shader.SHADER_DISPLACEMENT;
	
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
	
	protected Map<Shader, Texture2DBase> shader_tex_map = new LinkedHashMap<Shader, Texture2DBase>();
	public static final Texture2DBase TEX_NULL = null;
	
	//public Texture2DBase get
	public Texture2DBase getTexByShader(Shader shader) {
		return this.shader_tex_map.getOrDefault(shader, TEX_NULL);
	}
	
	public MaterialFace setTexByShader(Shader shader, Texture2DBase tex) {
		this.shader_tex_map.put(shader, tex);
		return this;
	}
	
	public MaterialFace delTexByShader(Shader shader) {
		this.shader_tex_map.remove(shader);
		return this;
	}
	
	public Shader[] getShaders() {
		Shader[] ans = this.shader_tex_map.keySet().toArray(new Shader[0]);
		return ans;
	}
	
	
	public Texture2DBase getDiffuse() {return getTexByShader(SHADER_DIFFUSE);}
	public MaterialFace setDiffuse(Texture2DBase tex) {return setTexByShader(SHADER_DIFFUSE, tex);}
	
	public Texture2DBase getLight() {return getTexByShader(SHADER_LIGHT);}
	public MaterialFace setLight(Texture2DBase tex) {return setTexByShader(SHADER_LIGHT, tex);}
	
	public Texture2DBase getAlpha() {return getTexByShader(SHADER_ALPHA);}
	public MaterialFace setAlpha(Texture2DBase tex) {return setTexByShader(SHADER_ALPHA, tex);}
	
	public Texture2DBase getSpecular() {return getTexByShader(SHADER_SPECULAR);}
	public MaterialFace setSpecular(Texture2DBase tex) {return setTexByShader(SHADER_SPECULAR, tex);}
	
	public Texture2DBase getNormal() {return getTexByShader(SHADER_NORMAL);}
	public MaterialFace setNormal(Texture2DBase tex) {return setTexByShader(SHADER_NORMAL, tex);}
	
	public Texture2DBase getDisplacement() {return getTexByShader(SHADER_DISPLACEMENT);}
	public MaterialFace setDisplacement(Texture2DBase tex) {return setTexByShader(SHADER_DISPLACEMENT, tex);}
	

}
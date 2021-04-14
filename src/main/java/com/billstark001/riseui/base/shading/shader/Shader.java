package com.billstark001.riseui.base.shading.shader;

import com.billstark001.riseui.base.BaseContainer;
import com.billstark001.riseui.base.Layer;

public class Shader extends BaseContainer {
	
	@FunctionalInterface
	public interface StateSetter {void operate();}
	public static final StateSetter DEFAULT_SETTER = States::face_diffuse;
	protected StateSetter setter;

	public StateSetter getSetter() {return setter;}
	
	public void setSetter(StateSetter setter) {
		if (this.isFrozen()) return;
		if (setter == null) setter = Shader.DEFAULT_SETTER;
		this.setter = setter;
	}
	
	public Shader() {super();this.setter = DEFAULT_SETTER;}	
	public Shader(String name) {super(name); this.setter = DEFAULT_SETTER;}
	public Shader(Layer layer) {super(layer); this.setter = DEFAULT_SETTER;}
	public Shader(String name, Layer layer) {super(name, layer); this.setter = DEFAULT_SETTER;}
	
	public Shader(String name, Layer layer, StateSetter setter) {
		super(name, layer);
		if (setter == null) setter = Shader.DEFAULT_SETTER;
		this.setter = setter;
	}
	
	public Shader(String name, StateSetter setter) {this(name, null, setter);}
	public Shader(StateSetter setter) {super(); if (setter == null) setter = Shader.DEFAULT_SETTER; this.setter = setter;}
	
	private Shader(String name, StateSetter setter, int mark) {
		super(name, null, mark);
		if (setter == null) setter = Shader.DEFAULT_SETTER;
		this.setter = setter;
	}
	
	public void applyState() {
		this.setter.operate();
	}
	
	// Default Shader Declaration
	
	public static final Shader SHADER_DEBUG = new Shader("Debug", States::debug, 1);
	public static final Shader SHADER_VERT = new Shader("Vertex", States::standard_vert, 1);
	public static final Shader SHADER_EDGE = new Shader("Edge", States::standard_edge, 1);
	public static final Shader SHADER_DIFFUSE = new Shader("Diffuse", States::face_diffuse, 1);
	public static final Shader SHADER_LIGHT = new Shader("Light", States::face_light, 1);
	public static final Shader SHADER_SPECULAR = new Shader("Specular", States::face_light, 1);
	public static final Shader SHADER_NORMAL = new Shader("Normal", States::face_light, 1);
	public static final Shader SHADER_ALPHA = new Shader("Alpha", States::face_light, 1);
	public static final Shader SHADER_DISPLACEMENT = new Shader("Displacement", States::face_light, 1);
}

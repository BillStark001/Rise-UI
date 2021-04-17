package com.billstark001.riseui.base;

import com.billstark001.riseui.base.fields.Field;

import it.unimi.dsi.fastutil.Arrays;

public class BaseContainer {

	private String name;
	protected Layer layer;
	private double frame_time; 
	private boolean frozen = false; // for special purpose
	
	public boolean isFrozen() {return this.frozen;}
	
	public static final String DEFAULT_NAME = "Unnamed";
	
	public BaseContainer() {this(DEFAULT_NAME, null);}
	public BaseContainer(String name) {this(name, null);}
	public BaseContainer(Layer layer) {this(DEFAULT_NAME, layer);}
	public BaseContainer(String name, Layer layer) {
		setName(name);
		setLayer(layer);
	}
	
	protected BaseContainer(String name, Layer layer, int mark) {
		this(name, layer);
		this.frozen = true;
	}
	
	public String getName() {return this.name;}
	public void setName(String name) {
		if (this.frozen) return;
		if (name == null) name = DEFAULT_NAME;
		this.name = name;
	}
	
	// Layer Operations
	
	public Layer getLayer() {return this.layer;}
	public boolean setLayer(Layer l) {
		if (this.frozen) return false;
		if (l == null) return false; return l.addMember(this);
	}
	public boolean removeFromLayer() {
		if (this.frozen) return false;
		if (this.layer == null) return false;
		return this.layer.removeMember(this);
	}
	public boolean getLayerState(int state) {
		if (layer == null) return false;
		return this.layer.getState(state);
	}
	
	// Animation Operations
	// TODO REMOVE IT!!! REFORM IT TO OPERATORS!!!
	
	public double getFrameTime() {return this.frame_time;}
	public boolean setFrameTime(double ftime) {
		this.frame_time = ftime;
		return true;
	}
	
	// Field Operations
	
	public void reflect() {
		System.out.println("REFLECT " + this.getClass().toString());
		java.lang.reflect.Field[] fields = this.getClass().getFields();
		// System.out.println(java.util.Arrays.deepToString(fields));
		for (int i = 0; i < fields.length; ++i) {
			System.out.println(fields[i]);
		}
	}
	
	// toString()
	
	public String toString() {
		return String.format("%s %s(Layer %s, Frame %f)", this.getClass().getSimpleName(), this.getName(), this.getLayer(), this.getFrameTime());
	}
	
}

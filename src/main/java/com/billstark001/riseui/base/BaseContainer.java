package com.billstark001.riseui.base;

import java.util.Map;

import com.billstark001.riseui.base.fields.Field;

public class BaseContainer {

	private String name;
	protected Layer layer;
	private double frame_time; 
	private boolean frozen = false; // for special purpose
	
	public boolean isFrozen() {return this.frozen;}
	
	public static final String DEFAULT_NAME = "Unnamed";
	
	private Map<Integer, Field> storage;
	
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
	
	public double getFrameTime() {return this.frame_time;}
	public boolean setFrameTime(double ftime) {
		this.frame_time = ftime;
		return true;
	}
	
	// Storage Operations
	
	public Class getType(int id) {
		return null;
	}
	
	public String toString() {
		return String.format("%s %s(Layer %s, Frame %f)", this.getClass().getSimpleName(), this.getName(), this.getLayer(), this.getFrameTime());
	}
	
}

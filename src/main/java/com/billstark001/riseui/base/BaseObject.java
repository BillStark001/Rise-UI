package com.billstark001.riseui.base;

public abstract class BaseObject {

	protected String name;
	protected Layer layer;
	
	public static final String DEFAULT_NAME = "Unnamed";
	
	public BaseObject() {this(DEFAULT_NAME, null);}
	public BaseObject(String name) {this(name, null);}
	public BaseObject(Layer layer) {this(DEFAULT_NAME, layer);}
	public BaseObject(String name, Layer layer) {
		setName(name);
		setLayer(layer);
	}
	
	public String getName() {return this.name;}
	public void setName(String name) {
		if (name == null) name = DEFAULT_NAME;
		this.name = name;
	}
	
	public Layer getLayer() {return this.layer;}
	public boolean setLayer(Layer l) {return l.addMember(this);}
	public boolean removeFromLayer() {
		if (this.layer == null) return false;
		return this.layer.removeMember(this);
	}
	public boolean getLayerState(int state) {
		if (layer == null) return false;
		return this.layer.getState(state);
	}
	
	public String toString() {
		return String.format("%s %s(Layer %s)", this.getClass().getSimpleName(), this.getName(), this.getLayer());
	}
	
}
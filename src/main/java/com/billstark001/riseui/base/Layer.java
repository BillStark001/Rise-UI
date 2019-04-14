package com.billstark001.riseui.base;

import java.util.ArrayList;

import scala.actors.threadpool.Arrays;

public class Layer extends BaseObject{

	protected String name;
	
	public static final String DEFAULT_NAME = "Unnamed Layer";
	
	protected static final int STATE_COUNT = 8;
	protected boolean[] state;
	
	public static final int 
	STATE_ALL = -1,
	STATE_RENDER = 0,
	STATE_UNIQUE_RENDER = 1,
	STATE_MODIFY = 2,
	STATE_TAG = 3,
	STATE_POLYGON = 4,
	STATE_DEFORMER = 5,
	STATE_GENERATOR = 6,
	STATE_EFFECTOR = 7;
	
	private ArrayList<BaseObject> objs;
	
	public Layer() {this(DEFAULT_NAME);}
	public Layer(String name) {
		setName(name);
		this.objs = new ArrayList<BaseObject>();
	}
	
	public String getName() {return this.name;}
	public void setName(String name) {
		if (name == null) name = DEFAULT_NAME;
		this.name = name;
	}
	
	public void setState(int state, boolean value) {
		if (state == STATE_ALL) {Arrays.fill(this.state, value);}
		else this.state[state] = value;
	}
	public boolean getState(int state) {
		if (state == STATE_ALL) {
			boolean ans = true;
			for (boolean b: this.state) if (!b) ans = false;
			return ans;
		}
		else return this.state[state];
	}
	
	// Layer
	
	// Original Function Detachment
	@Override
	public Layer getLayer() {return null;}
	@Override
	public boolean setLayer(Layer l) {return false;}
	@Override
	public boolean removeFromLayer() {return false;}
	@Override
	public boolean getLayerState(int state) {return false;}
	
	public boolean addMember(BaseObject member) {
		if (member == null || member instanceof Layer) return false;
		if (member.layer == this) return true;
		if (member.layer != null) member.layer.removeMember(member);
		member.layer = this;
		if (!objs.contains(member)) objs.add(member);
		return true;
	}
	
	public boolean removeMember(BaseObject member) {
		if (member == null || member instanceof Layer) return false;
		if (member.layer == this) {
			if (objs.contains(member)) objs.remove(member);
			member.layer = null;
			return true;
		} else return false;
	}
	
	public boolean removeAllMembers() {
		for (BaseObject obj: objs) obj.layer = null;
		objs = new ArrayList<BaseObject>();
		return true;
	}
	
	public BaseObject[] getMembers() {
		return this.objs.toArray(new BaseObject[0]);
	}

	// Display
	
	public String toString() {
		return String.format("%s %s", this.getClass().getSimpleName(), this.getName());
	}
	
	
}

package com.billstark001.riseui.base;

import org.apache.commons.lang3.ArrayUtils;

import com.billstark001.riseui.base.states.BaseState;
import com.billstark001.riseui.base.states.BaseTrack;

public class ObjectStatedBase extends NamedObject {
	
	public final int getNewStateCount() {
		try {
			this.getClass().getDeclaredMethod("getNewStateTypes");
		} catch (NoSuchMethodException e) {
			return 0;
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return getNewStateTypes().length;
	}
	protected final int getAllStateCount() {return getAllStateTypes().length;}
	protected BaseState.DataType[] getNewStateTypes() {
		return new BaseState.DataType[0];
	}
	protected final BaseState.DataType[] getAllStateTypes() {
		NamedObject sup_cl = null;
		try {
			sup_cl = (NamedObject) (this.getClass().getSuperclass().newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (!(sup_cl instanceof ObjectStatedBase)) return getNewStateTypes();
		else if (getNewStateCount() == 0) return ((ObjectStatedBase) sup_cl).getAllStateTypes();
		return ArrayUtils.addAll(((ObjectStatedBase) sup_cl).getAllStateTypes(), getNewStateTypes());
	}
	
	private final int state_count;
	private final BaseState.DataType[] state_types;
	public final int getStateCount() {return this.state_count;}
	public final BaseState.DataType getStateType(int index) {return this.state_types[index];}
	public final BaseState.DataType getNewStateType(int index) {return this.state_types[this.getStateCount() - this.getNewStateCount() + index];}
	
	private BaseState[] state_pool;
	private BaseTrack[] track_pool;
	
	private double frame_time;
	
	public ObjectStatedBase() {this(null, null);}
	public ObjectStatedBase(String name) {this(name, null);}
	public ObjectStatedBase(Layer layer) {this(null, layer);}
	public ObjectStatedBase(String name, Layer layer) {
		super(name, layer);
		this.state_count = this.getAllStateCount();
		this.state_types = this.getAllStateTypes();
		this.state_pool = new BaseState[this.getStateCount()];
		this.track_pool = new BaseTrack[this.getStateCount()];
	}
	
	public double getFrameTime() {return this.frame_time;}
	public void setFrameTime(double ftime) {
		this.frame_time = ftime;
	}
	
	public BaseState getState(int index) {
		return null;
	}
	public boolean setState(int index, BaseState state) {
		return false;
	}
	public BaseTrack getStateTrack(int index) {
		return null;
	}
	public boolean setStateTrack(int index, BaseTrack track) {
		return false;
	}
	public boolean clearStateTrack(int index) {
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s(Layer %s, State_Count %d(New: %d))", this.getClass().getSimpleName(), this.getName(), this.getLayer(), this.getAllStateCount(), this.getNewStateCount());
	}
	


}

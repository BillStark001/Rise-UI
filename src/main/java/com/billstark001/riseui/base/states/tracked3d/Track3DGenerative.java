package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.ShapeMismatchException;

public abstract class Track3DGenerative<T> extends Track3DBase {

	private StateTrackedBase<T> repr;
	
	public Track3DGenerative(Track3DGenerative<T> state) {this(state.repr);}
	public Track3DGenerative(StateTrackedBase<T> repr) {
		if (repr != null) setStateRepr(repr);
	}
	
	public void setStateRepr(StateTrackedBase<T> repr) {if (repr == null) return; this.repr = repr;}
	public StateTrackedBase<T> getStateRepr() {return this.repr;}
	public T getStateRepr(double time) {
		if (this.getStateRepr() == null) return null; 
		else return this.getStateRepr().get(time);
	}

	public abstract Matrix getMatFromRepr(double time);
	
	public Matrix get(double time) {
		return this.getMatFromRepr(time);
	}
	
	@Override
	public boolean containsFrames() {
		return this.getStateRepr().containsFrames();
	}
	@Override
	public double getStartTime() {
		if (this.getStateRepr() == null) return 0;
		else return this.getStateRepr().getStartTime();
	}
	@Override
	public double getEndTime() {
		if (this.getStateRepr() == null) return 0;
		else return this.getStateRepr().getEndTime();
	}

}

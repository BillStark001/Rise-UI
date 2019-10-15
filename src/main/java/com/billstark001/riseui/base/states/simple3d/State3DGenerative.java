package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.ShapeMismatchException;

public abstract class State3DGenerative<T> extends State3DBase {

	private T repr;
	private boolean state_dirty = true;
	
	public State3DGenerative(State3DGenerative<T> state) {this(state.repr);}
	public State3DGenerative() {resetRepr();}
	public State3DGenerative(T repr) {
		if (repr == null) resetRepr();
		else setStateRepr(repr);
	}
	
	public void setStateRepr(T repr) {if (repr == null) return; this.repr = repr; this.state_dirty = true;}
	public T getStateRepr() {return this.repr;}
	public void resetRepr() {setStateRepr(getDefaultRepr());}
	
	public abstract T getDefaultRepr();
	public abstract Matrix getMatFromRepr();
	
	public Matrix get() {
		if (this.state_dirty) {
			this.set(this.getMatFromRepr());
		}
		return super.get();
	}

}

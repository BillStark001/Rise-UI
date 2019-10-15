package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.ShapeMismatchException;

public abstract class GenerativeState<T> extends SimpleState {

	private T repr;
	private boolean state_dirty = true;
	
	public GenerativeState(GenerativeState<T> state) {this(state.repr);}
	public GenerativeState() {resetRepr();}
	public GenerativeState(T repr) {
		if (repr == null) resetRepr();
		else setStateRepr(repr);
	}
	
	public void setStateRepr(T repr) {if (repr == null) return; this.repr = repr; this.state_dirty = true;}
	public T getStateRepr() {return this.repr;}
	public void resetRepr() {setStateRepr(getDefaultRepr());}
	
	public abstract T getDefaultRepr();
	public abstract Matrix getMatFromRepr();
	
	public Matrix getState() {
		if (this.state_dirty) {
			try {
				this.changeState(getMatFromRepr());
				this.state_dirty = false;
			} catch (ShapeMismatchException e) {
				e.printStackTrace();
				this.resetState();
			}
		}
		return super.getState();
	}

}

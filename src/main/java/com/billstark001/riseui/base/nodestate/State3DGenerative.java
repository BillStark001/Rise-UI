package com.billstark001.riseui.base.nodestate;

import com.billstark001.riseui.computation.Matrix;

public abstract class State3DGenerative<T> extends State3DBase {

	private T repr;
	
	public State3DGenerative(State3DGenerative<T> state) {this(state.repr);}
	public State3DGenerative() {resetRepr();}
	public State3DGenerative(T repr) {
		if (repr == null) resetRepr();
		else setStateRepr(repr);
	}
	
	public void setStateRepr(T repr) {if (repr == null) return; this.repr = repr; this.markDirty();}
	public T getStateRepr() {return this.repr;}
	public void resetRepr() {setStateRepr(getDefaultRepr());}
	
	public abstract T getDefaultRepr();
	public abstract Matrix calcState();
}


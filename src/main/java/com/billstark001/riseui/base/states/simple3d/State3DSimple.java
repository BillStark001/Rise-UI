package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.ShapeMismatchException;

public class State3DSimple extends State3DBase {

	public State3DSimple(Matrix mat) {super(mat);}
	public State3DSimple() {}
	public State3DSimple(State3DSimple state) {super(state.get());}
	
	public static final State3DSimple DEFAULT_STATE = new State3DSimple();

}

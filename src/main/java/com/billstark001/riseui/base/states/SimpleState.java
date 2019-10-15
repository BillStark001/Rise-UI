package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.ShapeMismatchException;

public class SimpleState extends State4 {

	public SimpleState(Matrix mat) {super(mat);}
	public SimpleState() {}
	public SimpleState(SimpleState state) {super(state.getState());}
	
	public static final SimpleState STATE_STANDARD = new SimpleState();

}

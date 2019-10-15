package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class StateScl extends GenerativeState<Vector>{

	public StateScl(StateScl state) {super(state);}
	public StateScl(Vector repr) {super(repr);}
	public StateScl() {super();}
	
	@Override
	public Vector getDefaultRepr() {
		return Vector.UNIT1_D3;
	}

	@Override
	public Matrix getMatFromRepr() {
		return Utils.sclToHomoState(getStateRepr());
	}

}

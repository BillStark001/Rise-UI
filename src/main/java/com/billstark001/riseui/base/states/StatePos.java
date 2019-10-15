package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class StatePos extends GenerativeState<Vector>{

	public StatePos(StatePos state) {super(state);}
	public StatePos(Vector repr) {super(repr);}
	public StatePos() {super();}

	@Override
	public Vector getDefaultRepr() {
		return Vector.UNIT0_D3;
	}

	@Override
	public Matrix getMatFromRepr() {
		return Utils.posToHomoState(getStateRepr());
	}

}

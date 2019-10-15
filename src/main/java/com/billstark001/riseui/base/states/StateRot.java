package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Quaternion;

public class StateRot extends GenerativeState<Quaternion>{

	public StateRot(StateRot state) {super(state);}
	public StateRot(Quaternion repr) {super(repr);}
	public StateRot() {super();}
	
	@Override
	public Quaternion getDefaultRepr() {
		return Quaternion.UNIT;
	}

	@Override
	public Matrix getMatFromRepr() {
		return Utils.rotToHomoState(getStateRepr());
	}

}

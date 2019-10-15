package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Quaternion;

public class State3DRot extends State3DGenerative<Quaternion>{

	public State3DRot(State3DRot state) {super(state);}
	public State3DRot(Quaternion repr) {super(repr);}
	public State3DRot() {super();}
	
	@Override
	public Quaternion getDefaultRepr() {
		return Quaternion.UNIT;
	}

	@Override
	public Matrix getMatFromRepr() {
		return Utils.rotToHomoState(getStateRepr());
	}

}

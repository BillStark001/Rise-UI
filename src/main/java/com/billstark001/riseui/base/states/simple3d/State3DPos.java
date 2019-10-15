package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class State3DPos extends State3DGenerative<Vector>{

	public State3DPos(State3DPos state) {super(state);}
	public State3DPos(Vector repr) {super(repr);}
	public State3DPos() {super();}

	@Override
	public Vector getDefaultRepr() {
		return Vector.UNIT0_D3;
	}

	@Override
	public Matrix getMatFromRepr() {
		return Utils.posToHomoState(getStateRepr());
	}

}

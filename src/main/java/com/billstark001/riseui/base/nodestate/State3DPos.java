package com.billstark001.riseui.base.nodestate;

import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;

public class State3DPos extends State3DGenerative<Vector>{

	public State3DPos(State3DPos state) {super(state);}
	public State3DPos(Vector repr) {super(repr);}
	public State3DPos() {super();}

	@Override
	public Vector getDefaultRepr() {
		return Vector.UNIT0_D3;
	}

	@Override
	public Matrix calcState() {
		return Utils3D.posToHomoState(getStateRepr());
	}

}

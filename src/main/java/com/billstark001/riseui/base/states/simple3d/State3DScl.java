package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;

public class State3DScl extends State3DGenerative<Vector>{

	public State3DScl(State3DScl state) {super(state);}
	public State3DScl(Vector repr) {super(repr);}
	public State3DScl() {super();}
	
	@Override
	public Vector getDefaultRepr() {
		return Vector.UNIT1_D3;
	}

	@Override
	public Matrix getMatFromRepr() {
		return Utils3D.sclToHomoState(getStateRepr());
	}

}

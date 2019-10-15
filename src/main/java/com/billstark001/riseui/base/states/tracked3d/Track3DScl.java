package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class Track3DScl extends Track3DGenerative<Vector> {

	public Track3DScl(Track3DScl state) {super(state);}
	public Track3DScl(StateTrackedBase<Vector> repr) {super(repr);}

	@Override
	public Matrix getMatFromRepr(double time) {
		return Utils.sclToHomoState(getStateRepr(time));
	}
	
}

package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class Track3DPos extends Track3DGenerative<Vector> {

	public Track3DPos(Track3DPos state) {super(state);}
	public Track3DPos(StateTrackedBase<Vector> repr) {super(repr);}

	@Override
	public Matrix getMatFromRepr(double time) {
		return Utils.posToHomoState(getStateRepr(time));
	}
	
}

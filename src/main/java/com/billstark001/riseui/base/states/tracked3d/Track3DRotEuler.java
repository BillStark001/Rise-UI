package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class Track3DRotEuler extends Track3DGenerative<Vector> {

	public Track3DRotEuler(Track3DRotEuler state) {super(state);}
	public Track3DRotEuler(StateTrackedBase<Vector> repr) {super(repr);}

	@Override
	public Matrix getMatFromRepr(double time) {
		return Utils.rotToHomoState(getStateRepr(time));
	}
	
}

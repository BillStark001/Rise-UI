package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.base.states.simple3d.State3DBase;
import com.billstark001.riseui.base.states.simple3d.State3DRot;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class Track3DRotEuler extends Track3DRot<Vector> {

	public Track3DRotEuler(Track3DRotEuler state) {super(state);}
	public Track3DRotEuler(StateTrackedBase<Vector> repr) {super(repr);}

	@Override
	public Vector getStateRepr(double time) {
		return super.getStateRepr(time).mult(Math.PI / 180);
	}
	
	@Override
	public Matrix getMatFromRepr(double time) {
		return Utils.rotToHomoState(getStateRepr(time));
	}
	
	@Override
	public State3DBase getSimpleState(double time) {
		return new State3DRot(Quaternion.eulerToQuat(this.getStateRepr(time)));
	}
	
}

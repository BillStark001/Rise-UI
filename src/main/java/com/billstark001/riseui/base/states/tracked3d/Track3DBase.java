package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.base.states.simple3d.State3DBase;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.math.Matrix;

public abstract class Track3DBase extends StateTrackedBase<Matrix> {

	@Override
	public Class getDataType() {
		return Matrix.class;
	}
	
	public abstract State3DBase getSimpleState(double time);
	
	// Static Methods

	public static State3DSimple stateCompose(double time, Track3DBase A, Track3DBase B) {
		return new State3DSimple(State3DBase.stateCompose(A.get(time), B.get(time)));
	}
	
	public static State3DSimple stateDecomposeA(double time, Track3DBase C, Track3DBase B) {
		return new State3DSimple(State3DBase.stateDecomposeA(C.get(time), B.get(time)));
	}

	public static State3DSimple stateDecomposeB(double time, Track3DBase C, Track3DBase A) {
		return new State3DSimple(State3DBase.stateDecomposeB(C.get(time), A.get(time)));
	}
	
}

package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.StateTrackedBase;
import com.billstark001.riseui.base.states.simple3d.State3DBase;
import com.billstark001.riseui.base.states.simple3d.State3DRot;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public abstract class Track3DRot<T> extends Track3DGenerative<T> {

	public Track3DRot(Track3DRot<T> state) {super(state);}
	public Track3DRot(StateTrackedBase<T> repr) {super(repr);}
	
}

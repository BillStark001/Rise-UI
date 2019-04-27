package com.billstark001.riseui.base;

import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

public class StateContainer {

	public final Vector p;
	public final Quaternion r;
	public final Vector s;
	
	public StateContainer(Vector p, Quaternion r, Vector s) {
		this.p = p;
		this.r = r;
		this.s = s;
	}
	
	public StateContainer() {this(BaseNode.POS_UNIT, BaseNode.ROT_UNIT, BaseNode.SCALE_UNIT);}
	
}

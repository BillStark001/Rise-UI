package com.billstark001.riseui.math;

public class StateContainer {

	public final Vector p;
	public final Quaternion r;
	public final Vector s;
	
	public StateContainer(Vector p, Quaternion r, Vector s) {
		this.p = p;
		this.r = r;
		this.s = s;
	}
	
}

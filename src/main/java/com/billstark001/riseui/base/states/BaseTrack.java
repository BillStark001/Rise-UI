package com.billstark001.riseui.base.states;

public abstract class BaseTrack<T> {

	public abstract T get(double time);
	public abstract boolean containsFrames();
	
}

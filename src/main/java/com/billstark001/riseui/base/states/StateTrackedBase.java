package com.billstark001.riseui.base.states;

public abstract class StateTrackedBase<T> extends StateBase<T> {
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " " + this.getName() + " (ContainsFrames: " + this.containsFrames() + ")";
	}
}

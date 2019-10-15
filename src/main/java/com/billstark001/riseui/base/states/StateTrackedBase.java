package com.billstark001.riseui.base.states;

public abstract class StateTrackedBase<T> extends StateBase<T> {
	@Override
	public String toString() {
		return String.format("%s<%s> (ContainsFrames: %s)", this.getClass().getSimpleName(), this.getDataType().getSimpleName(), this.containsFrames());
	}
}

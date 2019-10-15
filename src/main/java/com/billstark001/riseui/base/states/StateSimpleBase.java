package com.billstark001.riseui.base.states;

public class StateSimpleBase<T> extends StateBase<T> {

	private T state = this.getDefault();
	
	protected boolean set(T state) {
		this.state = state;
		return true;
	}
	
	public StateSimpleBase(T t) {
		this.set(t);
	}
	
	public StateSimpleBase() {
		this.set(this.getDefault());
	}

	public T getDefault() {return null;}
	public T get() {if (state == null) return this.getDefault(); else return state;}
	
	@Override
	public T get(double time) {return this.get();}
	
	@Override
	public boolean containsFrames() {
		if (this.state != null && this.getDefault() != null) {
			return true;
		}
		else return false;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " " + this.getName() + ": " + this.get();
	}

	@Override()
	public int hashCode() {
		return this.get().hashCode();
	}

	@Override
	public double getStartTime() {
		return 0;
	}

	@Override
	public double getEndTime() {
		return 0;
	}

}

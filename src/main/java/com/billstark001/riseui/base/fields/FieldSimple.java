package com.billstark001.riseui.base.fields;

public class FieldSimple<T> extends Field<T> {

	private T state = this.getDefault();
	
	protected boolean set(T state) {
		this.state = state;
		return true;
	}
	
	public FieldSimple(T t) {
		this.set(t);
	}
	
	public FieldSimple() {
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
		return String.format("%s<%s>: %s", this.getClass().getSimpleName(), this.getDataType().getSimpleName(), this.get());
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

	@Override
	public Class getDataType() {
		return state.getClass();
	}

}

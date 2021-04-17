package com.billstark001.riseui.base.fields;

public class OprConstSimple<T> extends Operator<T> {

	private T data = this.getDefault();
	
	protected boolean set(T data) {
		this.data = data;
		return true;
	}
	
	public OprConstSimple(T t) {
		this.set(t);
	}
	
	public OprConstSimple() {
		this.set(this.getDefault());
	}

	public T getDefault() {return null;}
	public T get() {if (data == null) return this.getDefault(); else return data;}
	
	@Override
	public T get(double time) {return this.get();}
	
	@Override
	public boolean containsFrames() {
		if (this.data != null && this.getDefault() != null) {
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
		return data.getClass();
	}

}

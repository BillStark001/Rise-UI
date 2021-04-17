package com.billstark001.riseui.base.fields;

public class Field<T> {

	private Operator<T> oprraw;
	private Operator<T> oprcalc;
	
	public Operator<T> getRawOpr() {
		return oprraw;
	}

	public void setRawOpr(Operator<T> raw) {
		this.oprraw = raw;
	}

	public Field() {
		// TODO 自动生成的构造函数存根
	}
	
	public T getRaw(double time) {
		return this.oprraw.get(time);
	}
	
	public T get(double time) {
		return this.oprcalc.get(time);
	}
	
	public Operator<T> getCalcOpr() {
		return this.oprcalc;
	}
	
	public Operator<T> getOutputOpr() {
		return new OprFromField<T>(this);
	}

}

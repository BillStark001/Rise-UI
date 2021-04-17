package com.billstark001.riseui.base.fields;

public class OprFromField<T> extends OprFuncSimple<T, T> {

	public final Field<T> f;
	
	@Override
	public void setSource(Operator<T> opr) {throw new IllegalArgumentException("This Operator is NUKAGAWA!!!");}
	@Override
	public void setSource(T opr) {throw new IllegalArgumentException("This Operator is NUKAGAWA!!!");}
	
	public OprFromField(Field<T> f) {
		super(f.getRawOpr(), null);
		this.f = f;
		
	}

	@Override
	public T get(double time) {
		return f.getRaw(time);
	}

	@Override
	public boolean containsFrames() {
		return false;
	}

	@Override
	public Class getDataType() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public double getStartTime() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public double getEndTime() {
		// TODO 自动生成的方法存根
		return 0;
	}

}

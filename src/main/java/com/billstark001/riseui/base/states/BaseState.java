package com.billstark001.riseui.base.states;

public abstract class BaseState<T> {

	public static enum DataType {
		
		INT,
		PAIR,
		TRIAD,
		
		FLOAT,
		VEC,
		VEC3,
		VEC4,
		QUAT,
		
		MAT,
		MAT_ORTH,
		MAT_ORTH3,
		MAT_ORTH4,
		MAT_HOMO3,
		MAT_HOMO4,
		
		STR;
		
	}
	
	public abstract boolean checkType(DataType type);
	
	public abstract T get();
	
	public abstract T getDefault();
	
	public boolean set(T value) {
		return false;
	}
	
	public BaseState() {
		// TODO 自动生成的构造函数存根
	}

}

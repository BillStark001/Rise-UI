package com.billstark001.riseui.base.fields;

public abstract class Operator<O>{
	
	/*
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
	//public abstract boolean checkType(DataType type);
	*/
	
	public O get() {return this.get(this.getStartTime());}
	public abstract O get(double time);
	public abstract boolean containsFrames();
	public abstract Class getDataType();
	
	/*
	private String name = this.getClass().getSimpleName();
	public void setName(String name) {this.name = name;}
	public String getName() {return name;}
	public void resetName() {setName(this.getClass().getSimpleName());}
	*/
	
	public abstract double getStartTime();
	public abstract double getEndTime();
	
	public boolean isTracked() {return !(this instanceof OprConstSimple);}
}

package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.computation.Vector;

public class FieldGenVec3 extends Field<Vector> {

	private FieldFramed<Double> x;
	private FieldFramed<Double> y;
	private FieldFramed<Double> z;
	
	public FieldFramed<Double> getX() {return x;}
	public void setX(FieldFramed<Double> x) {this.x = x;}
	public FieldFramed<Double> getY() {return y;}
	public void setY(FieldFramed<Double> y) {this.y = y;}
	public FieldFramed<Double> getZ() {return z;}
	public void setZ(FieldFramed<Double> z) {this.z = z;}
	
	public FieldGenVec3(FieldFramed<Double> x, FieldFramed<Double> y, FieldFramed<Double> z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public FieldGenVec3() {this(null, null, null);}
	
	@Override
	public Vector get(double time) {
		return new Vector(x.get(time), y.get(time), z.get(time));
	}
	
	@Override
	public boolean containsFrames() {
		return x != null && x.containsFrames() 
				&& y != null && y.containsFrames() 
				&& z != null && z.containsFrames();
	}
	
	@Override
	public double getStartTime() {
		if (!this.containsFrames()) return 0;
		else return Math.min(Math.min(this.getX().getStartTime(), this.getY().getStartTime()), this.getZ().getStartTime());
	}
	
	@Override
	public double getEndTime() {
		if (!this.containsFrames()) return 0;
		else return Math.max(Math.max(this.getX().getEndTime(), this.getY().getEndTime()), this.getZ().getEndTime());
	}
	@Override
	public Class<Vector> getDataType() {
		return Vector.class;
	}

}

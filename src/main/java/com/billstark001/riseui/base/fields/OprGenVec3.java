package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.computation.Vector;

public class OprGenVec3 extends Operator<Vector> {

	private OprConstFramed<Double> x;
	private OprConstFramed<Double> y;
	private OprConstFramed<Double> z;
	
	public OprConstFramed<Double> getX() {return x;}
	public void setX(OprConstFramed<Double> x) {this.x = x;}
	public OprConstFramed<Double> getY() {return y;}
	public void setY(OprConstFramed<Double> y) {this.y = y;}
	public OprConstFramed<Double> getZ() {return z;}
	public void setZ(OprConstFramed<Double> z) {this.z = z;}
	
	public OprGenVec3(OprConstFramed<Double> x, OprConstFramed<Double> y, OprConstFramed<Double> z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public OprGenVec3() {this(null, null, null);}
	
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

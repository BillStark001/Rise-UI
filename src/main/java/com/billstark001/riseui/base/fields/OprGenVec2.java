package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.computation.Vector;

public class OprGenVec2 extends Operator<Vector> {

	private OprConstFramed<Double> x;
	private OprConstFramed<Double> y;
	
	public OprConstFramed<Double> getX() {return x;}
	public void setX(OprConstFramed<Double> x) {this.x = x;}
	public OprConstFramed<Double> getY() {return y;}
	public void setY(OprConstFramed<Double> y) {this.y = y;}
	
	public OprGenVec2(OprConstFramed<Double> x, OprConstFramed<Double> y) {
		this.setX(x);
		this.setY(y);
	}

	public OprGenVec2() {this(null, null);}
	
	@Override
	public Vector get(double time) {
		return new Vector(x.get(time), y.get(time));
	}
	
	@Override
	public boolean containsFrames() {
		return x != null && x.containsFrames() 
				&& y != null && y.containsFrames();
	}
	
	@Override
	public double getStartTime() {
		if (!this.containsFrames()) return 0;
		else return Math.min(this.getX().getStartTime(), this.getY().getStartTime());
	}
	
	@Override
	public double getEndTime() {
		if (!this.containsFrames()) return 0;
		else return Math.max(this.getX().getEndTime(), this.getY().getEndTime());
	}
	@Override
	public Class<Vector> getDataType() {
		return Vector.class;
	}

}

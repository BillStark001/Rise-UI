package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Vector;

public class StateTrackedVec3 extends StateTrackedBase<Vector> {

	private StateTrackedDouble x;
	private StateTrackedDouble y;
	private StateTrackedDouble z;
	
	public StateTrackedDouble getX() {return x;}
	public void setX(StateTrackedDouble x) {this.x = x;}
	public StateTrackedDouble getY() {return y;}
	public void setY(StateTrackedDouble y) {this.y = y;}
	public StateTrackedDouble getZ() {return z;}
	public void setZ(StateTrackedDouble z) {this.z = z;}
	
	public StateTrackedVec3(StateTrackedDouble x, StateTrackedDouble y, StateTrackedDouble z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public StateTrackedVec3() {this(null, null, null);}
	
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
	public Class getDataType() {
		return Vector.class;
	}

}

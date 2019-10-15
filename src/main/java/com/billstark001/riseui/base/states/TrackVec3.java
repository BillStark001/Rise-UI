package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Vector;

public class TrackVec3 extends BaseTrack<Vector> {

	private TrackSimpleFloat x;
	private TrackSimpleFloat y;
	private TrackSimpleFloat z;
	
	public TrackSimpleFloat getX() {return x;}
	public void setX(TrackSimpleFloat x) {this.x = x;}
	public TrackSimpleFloat getY() {return y;}
	public void setY(TrackSimpleFloat y) {this.y = y;}
	public TrackSimpleFloat getZ() {return z;}
	public void setZ(TrackSimpleFloat z) {this.z = z;}
	
	public TrackVec3(TrackSimpleFloat x, TrackSimpleFloat y, TrackSimpleFloat z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	@Override
	public Vector get(double time) {
		return new Vector(x.get(time), y.get(time), z.get(time));
	}
	
	@Override
	public boolean containsFrames() {
		return x.containsFrames() && y.containsFrames() && z.containsFrames();
	}

}

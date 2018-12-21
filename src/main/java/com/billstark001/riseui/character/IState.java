package com.billstark001.riseui.character;

import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

public interface IState {
	
	public boolean suits(TagApplyState o);
	
	public void setTime(double time);
	public void setInterpolationTime(double interpolation);
	
	public int getPriority(int objId);
	public double getStrength(int objId);
	public Vector getOffset(int objId);
	public Quaternion getRotation(int objId);
	public Vector getZoom(int objId);
	
	public boolean isDisposed();
	
}

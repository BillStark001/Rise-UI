package com.billstark001.riseui.base;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Vector;

public interface IGridable {
	
	public boolean isEdgeLooped();
	
	public int getEdgeCount();
	public int[] getSegment(int index);
	
	public Vector getVertex(int index);
	
}

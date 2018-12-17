package com.billstark001.riseui.objects;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Vector;

public interface IGridable {

	public int getSegmentCount();
	
	public boolean getSegmentLooped(int index);
	public boolean getSwitchStripLoop(int index);
	public Matrix getSegmentByIndex(int index);
	
}

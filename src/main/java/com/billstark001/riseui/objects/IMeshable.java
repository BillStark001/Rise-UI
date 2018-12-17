package com.billstark001.riseui.objects;

import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

public interface IMeshable {
	
	public int getFaceCount();
	
	public Matrix getFaceVertex(int index);
	public Matrix getFaceUVMap(int index);
	public Matrix getFaceNormal(int index);
	
	public String getMaterial(int index);
	
}

package com.billstark001.riseui.base;

import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.base.shader.BaseMaterial;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;

public interface IMeshable {
	
	public int getFaceCount();
	
	public Triad[] getFace(int index);
	public BaseMaterial getMaterial(int index);
	
	public Vector getVertex(int index);
	public Vector getUVMap(int index);
	public Vector getNormal(int index);
	
}

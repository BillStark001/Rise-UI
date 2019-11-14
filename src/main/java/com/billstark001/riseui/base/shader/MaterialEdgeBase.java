package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.computation.Vector;

public abstract class MaterialEdgeBase extends BaseObject {

	public MaterialEdgeBase(String name, Layer layer) {super(name, layer);}
	public MaterialEdgeBase(String name) {this(name, null);}
	public MaterialEdgeBase(Layer layer) {this(null, layer);}
	public MaterialEdgeBase() {this(null, null);}
	
	public abstract int[] getColor(Vector[] pos);
	public abstract double[] getWidth(Vector[] pos);
	
	public boolean needsPos() {return true;}

}

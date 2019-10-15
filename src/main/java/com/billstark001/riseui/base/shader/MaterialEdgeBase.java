package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.NamedObject;
import com.billstark001.riseui.base.Layer;

public abstract class MaterialEdgeBase extends NamedObject {

	public MaterialEdgeBase(String name, Layer layer) {super(name, layer);}
	public MaterialEdgeBase(String name) {this(name, null);}
	public MaterialEdgeBase(Layer layer) {this(null, layer);}
	public MaterialEdgeBase() {this(null, null);}
	
	public abstract int[] getColor(int pcount);
	public abstract double[] getThickness(int pcount);

}

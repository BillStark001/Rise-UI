package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.computation.Vector;

public abstract class MaterialVertBase extends BaseObject {

	public MaterialVertBase(String name, Layer layer) {super(name, layer);}
	public MaterialVertBase(String name) {this(name, null);}
	public MaterialVertBase(Layer layer) {this(null, layer);}
	public MaterialVertBase() {this(null, null);}
	
	public abstract int getColor(Vector pos);
	public abstract double getSize(Vector pos);

}

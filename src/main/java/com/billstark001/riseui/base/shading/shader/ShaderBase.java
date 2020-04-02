package com.billstark001.riseui.base.shading.shader;

import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.Layer;

public abstract class ShaderBase extends BaseObject {

	public ShaderBase() {}	
	public ShaderBase(String name) {super(name);}
	public ShaderBase(Layer layer) {super(layer);}
	public ShaderBase(String name, Layer layer) {super(name, layer);}
	
	public abstract boolean apply();
	public abstract boolean restore();

}

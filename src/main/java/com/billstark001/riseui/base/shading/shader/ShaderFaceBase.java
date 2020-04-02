package com.billstark001.riseui.base.shading.shader;

import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.render.GlHelper;

public class ShaderFaceBase extends ShaderBase {

	public ShaderFaceBase() {
		// TODO 自动生成的构造函数存根
	}

	public ShaderFaceBase(String name) {
		super(name);
		// TODO 自动生成的构造函数存根
	}

	public ShaderFaceBase(Layer layer) {
		super(layer);
		// TODO 自动生成的构造函数存根
	}

	public ShaderFaceBase(String name, Layer layer) {
		super(name, layer);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public boolean apply() {
		GlHelper.getInstance().setFaceState();
		return true;
	}

	@Override
	public boolean restore() {
		// TODO 自动生成的方法存根
		return false;
	}

}

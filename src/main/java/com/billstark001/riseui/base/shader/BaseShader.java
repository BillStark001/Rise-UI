package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Pair;

public class BaseShader {

	private final Pair matsize;
	private Matrix map;
	
	public BaseShader(int h, int w) {
		this.matsize = new Pair(h, w);
		this.map = new Matrix(matsize);
	}
	public BaseShader(int l) {this(l, l);}
	public BaseShader() {this(16, 16);}
	
	public Matrix getShaderMap(double time) {return map;}
	public Matrix getShaderMap() {return map;}
	
	public void apply() {
		
	}
	
}

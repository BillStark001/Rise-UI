package com.billstark001.riseui.base.shading;

import com.billstark001.riseui.computation.Pair;
import com.billstark001.riseui.computation.UtilsTex;

public class Texture2DPureColor extends Texture2DBase {
	
	protected int color;
	public int getColor() {return this.color;}
	public int[] getColorARGB() {return UtilsTex.colorToARGB(color);}
	public int[] getColorRGBA() {return UtilsTex.colorToRGBA(color);}
	public void setColor(int color) {this.color = color;}
	public void setColorRGBA(double r, double g, double b, double a) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorARGB(double a, double r, double g, double b) {this.color = UtilsTex.color(r, g, b, a);}
	
	public Texture2DPureColor() {this(Texture2DBase.DEFAULT_COLOR);}
	public Texture2DPureColor(double r, double g, double b, double a) {this(UtilsTex.color(r, g, b, a));}
	public Texture2DPureColor(int color) {this(Pair.PAIR_1_1, color);}
	public Texture2DPureColor(int h, int w, int color) {this(new Pair(h, w), color);}
	public Texture2DPureColor(Pair solution, int color) {
		super(solution, false, false, false);
		this.setColor(color);
	}
	
	@Override
	public boolean getTextureARGB(int y_start, int y_length, int[] cache, int offset) {
		int pixels_to_fill = y_length * this.getWidth();
		if (pixels_to_fill < 0) return false;
		for (int i = offset; i < pixels_to_fill + offset; ++i) cache[i] = this.getColor();
		return true;
	}

}

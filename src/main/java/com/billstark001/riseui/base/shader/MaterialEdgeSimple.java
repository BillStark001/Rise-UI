package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;

public class MaterialEdgeSimple extends MaterialEdgeBase {

	public static final int DEFAULT_COLOR = 0xFFFFFFFF;
	public static final double DEFAULT_WIDTH = 1;
	private int color = DEFAULT_COLOR;
	private double width = DEFAULT_WIDTH;
	
	public void setColor(int color) {this.color = color;}
	public void setColor(int r, int g, int b, int a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColor(int r, int g, int b) {setColor(UtilsTex.color(r, g, b));}
	public void setColor(double r, double g, double b, double a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColor(double r, double g, double b) {setColor(UtilsTex.color(r, g, b));}
	public void setColor(float r, float g, float b, float a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColor(float r, float g, float b) {setColor(UtilsTex.color(r, g, b));}
	public int getColor() {return this.color;}
	
	public void setWidth(double width) {if (width >= 0) this.width = width;}
	public double getWidth() {return this.width;}
	
	public MaterialEdgeSimple(int color, double width) {this(color, width, null, null);}
	public MaterialEdgeSimple(int color, double width, String name, Layer layer) {
		super(name, layer);
		this.setColor(color);
		this.setWidth(width);
	}
	
	public MaterialEdgeSimple(String name, Layer layer) {super(name, layer);}
	public MaterialEdgeSimple(String name) {super(name);}
	public MaterialEdgeSimple(Layer layer) {super(layer);}
	public MaterialEdgeSimple() {}

	@Override
	public int[] getColor(Vector[] pos) {
		int c = this.getColor();
		int[] ans = new int[pos.length];
		for (int i = 0; i < ans.length; ++i) ans[i] = c;
		return ans;
	}

	@Override
	public double[] getWidth(Vector[] pos) {
		double t = this.getWidth();
		double[] ans = new double[pos.length];
		for (int i = 0; i < ans.length; ++i) ans[i] = t;
		return ans;
	}
	
	@Override
	public boolean needsPos() {return false;}

}

package com.billstark001.riseui.base.shading.mat;

import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;

public class MaterialVertSimple extends MaterialVertBase {

	public static final int DEFAULT_COLOR = 0xFFFFFFFF;
	public static final double DEFAULT_SIZE = 2;
	private int color = DEFAULT_COLOR;
	private double size = DEFAULT_SIZE;
	
	public void setColor(int color) {this.color = color;}
	public void setColor(int r, int g, int b, int a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColor(int r, int g, int b) {setColor(UtilsTex.color(r, g, b));}
	public void setColor(double r, double g, double b, double a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColor(double r, double g, double b) {setColor(UtilsTex.color(r, g, b));}
	public void setColor(float r, float g, float b, float a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColor(float r, float g, float b) {setColor(UtilsTex.color(r, g, b));}
	public int getColor() {return this.color;}
	
	public void setSize(double size) {if (size >= 0) this.size = size;}
	public double getSize() {return this.size;}
	
	public MaterialVertSimple(int color, double size) {this(color, size, null, null);}
	public MaterialVertSimple(int color, double size, String name, Layer layer) {
		super(name, layer);
		this.setColor(color);
		this.setSize(size);
	}
	
	public MaterialVertSimple(String name, Layer layer) {super(name, layer);}
	public MaterialVertSimple(String name) {super(name);}
	public MaterialVertSimple(Layer layer) {super(layer);}
	public MaterialVertSimple() {}

	@Override
	public int getColor(Vector pos) {
		return this.getColor();
	}

	@Override
	public double getSize(Vector pos) {
		return this.getSize();
	}
	
	@Override
	public boolean needsPos() {
		return false;
	}

}

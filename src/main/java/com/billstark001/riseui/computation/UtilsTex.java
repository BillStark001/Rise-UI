package com.billstark001.riseui.computation;

public class UtilsTex {

	public static final int COLOR_CONSTANT = 255;
	public static final double MIN_ALPHA = 1;
	
	public static int color(double a) {return color(1, 1, 1, a);}
	public static int color(double r, double g, double b) {return color(r, g, b, MIN_ALPHA);}
	public static int color(double r, double g, double b, double a) {
		a = Math.max(0, Math.min(a, 1));
		r = Math.max(0, Math.min(r, 1));
		g = Math.max(0, Math.min(g, 1));
		b = Math.max(0, Math.min(b, 1));
		int ai, ri, gi, bi;
		ai = (int) (a * COLOR_CONSTANT);
		ri = (int) (r * COLOR_CONSTANT);
		gi = (int) (g * COLOR_CONSTANT);
		bi = (int) (b * COLOR_CONSTANT);
		return (ai << 24) + (ri << 16) + (gi << 8) + bi;
	}

	public static int color(float a) {return color(1, 1, 1, a);}
	public static int color(float r, float g, float b) {return color(r, g, b, MIN_ALPHA);}
	public static int color(float r, float g, float b, float a) {
		a = Math.max(0, Math.min(a, 1));
		r = Math.max(0, Math.min(r, 1));
		g = Math.max(0, Math.min(g, 1));
		b = Math.max(0, Math.min(b, 1));
		int ai, ri, gi, bi;
		ai = (int) (a * COLOR_CONSTANT);
		ri = (int) (r * COLOR_CONSTANT);
		gi = (int) (g * COLOR_CONSTANT);
		bi = (int) (b * COLOR_CONSTANT);
		return (ai << 24) + (ri << 16) + (gi << 8) + bi;
	}
	
	public static int color(int a) {return color(1, 1, 1, a);}
	public static int color(int r, int g, int b) {return color(r, g, b, (int) (MIN_ALPHA * COLOR_CONSTANT));}
	public static int color(int r, int g, int b, int a) {
		a = Math.max(0, Math.min(a, COLOR_CONSTANT));
		r = Math.max(0, Math.min(r, COLOR_CONSTANT));
		g = Math.max(0, Math.min(g, COLOR_CONSTANT));
		b = Math.max(0, Math.min(b, COLOR_CONSTANT));
		return (a << 24) + (r << 16) + (g << 8) + b;
	}

	public static int[] colorToRGBA(int c) {
		int a, r, g, b;
		a = c >> 24 & 255;
		r = c >> 16 & 255;
		g = c >> 8 & 255;
		b = c & 255;
		int[] ans = {r, g, b, a};
		return ans;
	}
	
	public static int[] colorToARGB(int c) {
		int a, r, g, b;
		a = c >> 24 & 255;
		r = c >> 16 & 255;
		g = c >> 8 & 255;
		b = c & 255;
		int[] ans = {a, r, g, b};
		return ans;
	}
	
}

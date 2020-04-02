package com.billstark001.riseui.base.shading.mat;

import com.billstark001.riseui.computation.ColorGradient;
import com.billstark001.riseui.computation.Pair;

public class Texture2DGrayGen extends Texture2DBase {
	
	// Generator related
	
	@FunctionalInterface
	public interface Generator {double gen(double x, double y);}
	public static final Generator DEFAULT_GENERATOR = Texture2DGrayGen::simpleH;
	protected Generator generator = DEFAULT_GENERATOR;
	public void setGenerator(Generator func) {
		if (func == null) this.generator = DEFAULT_GENERATOR;
		this.generator = func;
	}
	public Generator getGenerator() {return this.generator;}
	
	// Gradient related
	
	protected ColorGradient gradient = ColorGradient.getDefault();
	public void setGradient(ColorGradient grad) {
		if (grad == null) this.gradient = ColorGradient.getDefault();
		this.gradient = grad;
	}
	public ColorGradient getGradient() {return this.gradient;}
	public ColorGradient getGradientC() {return new ColorGradient(this.gradient);}
	
	// Init. Func.
	
	public static final Pair DEFAULT_SOLUTION = Pair.PAIR_32_32;
	
	public Texture2DGrayGen(Pair solution, Generator generator, ColorGradient gradient, boolean blur, boolean mipmap, boolean clamp) {
		super(solution, blur, mipmap, clamp);
		this.setGenerator(generator);
		this.setGradient(gradient);
	}
	
	public Texture2DGrayGen(int h, int w, Generator generator, ColorGradient gradient, boolean blur, boolean mipmap, boolean clamp) {
		super(h, w, blur, mipmap, clamp);
		this.setGenerator(generator);
		this.setGradient(gradient);
	}
	
	public Texture2DGrayGen(Generator generator, ColorGradient gradient) {
		super(DEFAULT_SOLUTION, false, false, false);
		this.setGenerator(generator);
		this.setGradient(gradient);
	}
	
	public Texture2DGrayGen(Pair solution, Generator generator, ColorGradient gradient) {
		super(solution, false, false, false);
		this.setGenerator(generator);
		this.setGradient(gradient);
	}
	
	public Texture2DGrayGen(Generator generator) {
		super(DEFAULT_SOLUTION, false, false, false);
		this.setGenerator(generator);
		this.setGradient(ColorGradient.getDefault());
	}
	
	public Texture2DGrayGen(ColorGradient gradient) {
		super(DEFAULT_SOLUTION, false, false, false);
		this.setGenerator(DEFAULT_GENERATOR);
		this.setGradient(gradient);
	}
	
	public Texture2DGrayGen(int h, int w, boolean blur, boolean mipmap, boolean clamp) {super(h, w, blur, mipmap, clamp);}
	public Texture2DGrayGen(Pair solution, boolean blur, boolean mipmap, boolean clamp) {super(solution, blur, mipmap, clamp);}
	public Texture2DGrayGen(boolean blur, boolean mipmap, boolean clamp) {this(DEFAULT_SOLUTION, blur, mipmap, clamp);}
	public Texture2DGrayGen(boolean blur, boolean clamp) {this(DEFAULT_SOLUTION, blur, false, clamp);}
	public Texture2DGrayGen(Pair solution) {super(solution);}
	public Texture2DGrayGen(int h, int w) {super(h, w);}
	public Texture2DGrayGen(int l) {super(l);}
	public Texture2DGrayGen() {super(DEFAULT_SOLUTION);}

	protected double getx(int x) {return (((double) x) + 0.5) / this.getWidth();}
	protected double gety(int y) {return (((double) y) + 0.5) / this.getHeight();}
	
	@Override
	public boolean getTextureARGB(int y_start, int y_length, int[] cache, int offset) {
		int cur_point;
		double x, y, cur_color;
		for (int i = 0; i < y_length; ++i) {
			for (int j = 0; j < this.getWidth(); ++j) {
				cur_point = offset + i * this.getWidth() + j;
				x = this.getx(j);
				y = this.gety(i + y_start); 
				cur_color = generator.gen(x, y);
				cache[cur_point] = this.gradient.get(cur_color);
			}
		}
		return true;
	}
	
	// Grayscale Generators
	
	public static double simpleH(double x, double y) {return x;}
	public static double simpleV(double x, double y) {return y;}
	public static double square(double x, double y) {
		double ans;
		ans = Math.max((x > 0.5? 1 - x * 2: x * 2 - 1), (y > 0.5? 1 - y * 2: y * 2 - 1));
		return ans;
	}
	public static double cross(double x, double y) {
		double ans;
		ans = Math.max((x > 0.5? x * 2: 2 - x * 2), (y > 0.5? y * 2: 2 - y * 2));
		return ans;
	}
	public static double circle(double x, double y) {
		return Math.sqrt((x - 0.5) * (x - 0.5) + (y - 0.5) * (y - 0.5)) / (Math.sqrt(2) * 0.5);
	}
	public static double polar(double x, double y) {
		if (y == 0.5) return 0.5;
		else return Math.atan2(0.5 - y, 0.5 - x) / (2 * Math.PI) + 0.5;
	}

}

package com.billstark001.riseui.computation;

import java.util.ArrayList;
import java.util.Collection;

public class ColorGradient {

	public static final int DEFAULT_COLOR = 0xFF7F7F7F;
	public static final double DEFAULT_POS = 0;
	public static final double DEFAULT_MARK_POS = 0.5;
	public static final Interpolation DEFAULT_INTERP = Interpolation.CIRCULAR;
	public static final double DEFAULT_BRIGHTNESS = 1;
	
	public enum Interpolation {
		CIRCULAR,
		//BICUBIC,
		LINEAR,
		STEP;
	}
	
	public static class Slide implements Comparable<Slide> {
		
		private double pos;
		private int color;
		private double mark_pos;
		private Interpolation interp;
		private double brightness;

		public double getPos() {return pos;}
		public void setPos(double pos) {this.pos = Math.max(0, Math.min(pos, 1));}
		
		public int getColor() {return color;}
		public int[] getColorARGB() {return UtilsTex.colorToARGB(getColor());}
		public int[] getColorRGBA() {return UtilsTex.colorToRGBA(getColor());}
		public void setColor(int color) {this.color = color;}
		
		public double getMarkPos() {return mark_pos;}
		public void setMarkPos(double mark_pos) {this.mark_pos = Math.max(0, Math.min(mark_pos, 1));}
		
		public void setInterp(Interpolation i) {if (i == null) i = Interpolation.STEP; this.interp = i;}
		public Interpolation getInterp() {return this.interp;}
		
		public double getBrightness() {return this.brightness;}
		public void setBrightness(double b) {this.brightness = Math.max(0, b);}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Slide)) return false;
			Slide o_ = (Slide) o;
			return pos == o_.pos && color == o_.color;
		}

		@Override
		public int compareTo(Slide o) {
			if (this.pos > o.pos) return 1;
			else if (this.pos < o.pos) return -1;
			else {
				if (this.color > o.color) return 1;
				else if (this.color < o.color) return -1;
				else return 0;
			}
		}
		
		public Slide(Slide s) {this(s.getPos(), s.getColor(), s.getMarkPos(), s.getBrightness(), s.getInterp());}
		public Slide() {this(ColorGradient.DEFAULT_POS, ColorGradient.DEFAULT_COLOR);}
		public Slide(int color) {this(ColorGradient.DEFAULT_POS, color);}
		public Slide(double pos) {this(pos, ColorGradient.DEFAULT_COLOR);}
		public Slide(double pos, int color) {this(pos, color, ColorGradient.DEFAULT_MARK_POS, ColorGradient.DEFAULT_BRIGHTNESS, ColorGradient.DEFAULT_INTERP);}
		public Slide(double pos, int color, Interpolation interp) {this(pos, color, ColorGradient.DEFAULT_MARK_POS, ColorGradient.DEFAULT_BRIGHTNESS, interp);}
		public Slide(double pos, int color, double mark_pos, double brightness, Interpolation interp) {
			this.setPos(pos);
			this.setColor(color);
			this.setMarkPos(mark_pos);
			this.setBrightness(brightness);
			this.setInterp(interp);
		}
	}
	
	// Slides Management
	
	private ArrayList<Slide> slides;
	public void removeAllSlides() {this.slides = new ArrayList<Slide>();}
	public void addSlide(double pos, int color) {this.slides.add(new Slide(pos, color));this.slides.sort(Slide::compareTo);}
	public void addSlide(Slide s) {this.slides.add(s); this.slides.sort(Slide::compareTo);}
	public void addSlideC(Slide s) {this.slides.add(new Slide(s)); this.slides.sort(Slide::compareTo);}
	public void addSlides(Slide...slides) {for (Slide s: slides) this.slides.add(s); this.slides.sort(Slide::compareTo);}
	public void addSlides(Collection<Slide> slides) {for (Slide s: slides) this.slides.add(s); this.slides.sort(Slide::compareTo);}
	public void addSlidesC(Slide...slides) {for (Slide s: slides) this.slides.add(new Slide(s)); this.slides.sort(Slide::compareTo);}
	public void addSlidesC(Collection<Slide> slides) {for (Slide s: slides) this.slides.add(new Slide(s)); this.slides.sort(Slide::compareTo);}
	public Slide getSlide(int index) {return this.slides.get(index);}
	public Slide getSlideC(int index) {return new Slide(this.slides.get(index));}
	public Slide[] getSlides() {return this.slides.toArray(new Slide[0]);}
	public Slide[] getSlidesC() {
		Slide[] ans = new Slide[this.slides.size()];
		for (int i = 0; i < ans.length; ++i) ans[i] = new Slide(this.slides.get(i));
		return ans;
	}
	public boolean removeSlide(Slide s) {return this.slides.remove(s);}
	public boolean removeSlides(double pstart, double pend) {
		boolean ans = false;
		for (Slide s: slides) {
			if (s.getPos() >= pstart && s.getPos() < pend) ans = ans && slides.remove(s);
		}
		return ans;
	}
	public int getSlidesCount() {return this.slides.size();}
	public boolean containsSlides() {return this.slides.isEmpty();}
	
	// Utils
	
	public static ColorGradient getDefaultTransparent() {
		return new ColorGradient(new Slide(0, 0x007F7F7F, Interpolation.LINEAR), new Slide(1, 0xFF7F7F7F, Interpolation.LINEAR));
	}
	
	public static ColorGradient getDefault() {
		return new ColorGradient(new Slide(0, 0xFF000000, Interpolation.LINEAR), new Slide(1, 0xFFFFFFFF, Interpolation.LINEAR));
	}
	
	public static ColorGradient getMonoColor(int color) {
		return new ColorGradient(color);
	}
	
	// Init. Func.
	
	public ColorGradient(Slide...slides) {
		this.removeAllSlides();
		this.addSlidesC(slides);
	}
	
	public ColorGradient() {
		this.removeAllSlides();
	}
	
	public ColorGradient(ColorGradient c) {
		this.removeAllSlides();
		this.addSlidesC(c.getSlides());
	}
	
	public ColorGradient(int color) {
		this.removeAllSlides();
		this.addSlide(new Slide(DEFAULT_POS, color));
	}
	
	// Get values by positions
	
	private boolean posFitsIndex(double pos, int index) {
		return this.getSlide(index).pos <= pos && this.getSlide(index + 1).pos > pos;
	}
	
	private int findIndexByPos(double pos) {
		if (pos < this.getSlide(0).pos) return -1;
		else if (pos >= this.getSlide(this.getSlidesCount() - 1).pos) return this.getSlidesCount() - 1;
		else {
			// left included, right excluded
			int left = 0, right = this.getSlidesCount() - 1;
			int cur = (int) Math.floor((left + right) / 2);
			while (!posFitsIndex(pos, cur)) {
				if (this.getSlide(cur).pos == pos && this.getSlide(cur + 1).pos == pos) {
					cur -= 1;
					continue;
				}
				if (this.getSlide(cur).pos > pos) right = cur;
				else if (this.getSlide(cur + 1).pos < pos) left = cur;
				cur = (int) Math.floor((left + right) / 2);
			}
			return cur;
		}
	}
	
	// Interpolation related
	
	/*
	private boolean dirty = true;
	private boolean isDirty() {return dirty;}
	private void markDirty() {this.dirty = true;}
	private void clarify() {this.dirty = false;}
	*/
	
	public int get(double pos) {
		if (this.getSlidesCount() == 0) return ColorGradient.DEFAULT_COLOR;
		if (this.getSlidesCount() == 1) return this.getSlide(0).getColor();
		// else if (this.getSlidesCount() > 1) return UtilsTex.fuseColor(0xFFFFFFFF, 0xFFFFFFFF, pos);
		int index = this.findIndexByPos(pos);
		if (index == -1) return this.getSlide(0).getColor();
		if (index == -2 || index >= this.getSlidesCount() - 1) return this.getSlide(this.getSlidesCount() - 1).getColor();
		else {
			Slide f0 = this.getSlide(index);
			Slide f1 = this.getSlide(index + 1);
			double interp_t = (pos - f0.pos) / (f1.pos - f0.pos);
			double smggh_point = f0.getMarkPos();
			if (interp_t < smggh_point) interp_t = interp_t * 0.5 / smggh_point;
			else interp_t = 0.5 * (interp_t - 2 * smggh_point + 1) / (1 - smggh_point);
			double fuse_rate = 0;
			if (f0.getInterp().equals(Interpolation.CIRCULAR))
				fuse_rate = -0.5 * (Math.cos(interp_t * Math.PI)) + 0.5;
			//else if (f0.getInterp().equals(Interpolation.BICUBIC))
			//	fuse_rate = -0.5 * (Math.cos(interp_t * Math.PI)) + 0.5;
			else if (f0.getInterp().equals(Interpolation.LINEAR)) 
				fuse_rate = interp_t;
			else
				fuse_rate = 0;
			return UtilsTex.fuseColor(f0.getColor(), f1.getColor(), f0.getBrightness(), f1.getBrightness(), fuse_rate);
		}
	}
	

}

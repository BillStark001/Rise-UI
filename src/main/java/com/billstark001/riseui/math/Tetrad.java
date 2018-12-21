package com.billstark001.riseui.math;

public final class Tetrad {
	
	private final int wi, xi, yi, zi;
	
	public Tetrad(int w, int x, int y, int z) {
		wi = w;
		xi = x;
		yi = y;
		zi = z;
	}
	
	public Tetrad(Tetrad p) {
		wi = p.wi;
		xi = p.xi;
		yi = p.yi;
		zi = p.zi;
	}
	
	public final Tetrad add(Tetrad p) {
		return new Tetrad(wi + p.wi, xi + p.xi, yi + p.yi, zi + p.zi);
	}
	
	public final Tetrad minus(Tetrad p) {
		return new Tetrad(wi - p.wi, xi - p.xi, yi - p.yi, zi - p.zi);
	}
	
	public final Tetrad reverse() {
		return new Tetrad(-wi, -xi, -yi, -zi);
	}
	
	public final Tetrad wOnly() {
		return new Tetrad(wi, 0, 0, 0);
	}
	
	public final Tetrad xOnly() {
		return new Tetrad(0, xi, 0, 0);
	}
	
	public final Tetrad yOnly() {
		return new Tetrad(0, 0, yi, 0);
	}
	
	public final Tetrad zOnly() {
		return new Tetrad(0, 0, 0, zi);
	}
	
	public final Tetrad reverseW() {return new Tetrad(-wi, xi, yi, zi);}
	public final Tetrad reverseX() {return new Tetrad(wi, -xi, yi, zi);}
	public final Tetrad reverseY() {return new Tetrad(wi, xi, -yi, zi);}
	public final Tetrad reverseZ() {return new Tetrad(wi, xi, yi, -zi);}
	
	public final boolean equals(Tetrad p) {
		return wi == p.wi && xi == p.xi && yi == p.yi && zi == p.zi;
	}
	
	public final String toString() {
		return String.format("(%d, %d, %d, %d)", wi, xi, yi, zi);
	}
	
	public final int getW() {
		return wi;
	}
	
	public final int getX() {
		return xi;
	}
	
	public final int getY() {
		return yi;
	}
	
	public final int getZ() {
		return zi;
	}
}

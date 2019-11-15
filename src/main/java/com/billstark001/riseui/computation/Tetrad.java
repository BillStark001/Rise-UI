package com.billstark001.riseui.computation;

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
	
	public Tetrad add(Tetrad p) {
		return new Tetrad(wi + p.wi, xi + p.xi, yi + p.yi, zi + p.zi);
	}
	
	public Tetrad minus(Tetrad p) {
		return new Tetrad(wi - p.wi, xi - p.xi, yi - p.yi, zi - p.zi);
	}
	
	public Tetrad reverse() {
		return new Tetrad(-wi, -xi, -yi, -zi);
	}
	
	public Tetrad wOnly() {
		return new Tetrad(wi, 0, 0, 0);
	}
	
	public Tetrad xOnly() {
		return new Tetrad(0, xi, 0, 0);
	}
	
	public Tetrad yOnly() {
		return new Tetrad(0, 0, yi, 0);
	}
	
	public Tetrad zOnly() {
		return new Tetrad(0, 0, 0, zi);
	}
	
	public Tetrad reverseW() {return new Tetrad(-wi, xi, yi, zi);}
	public Tetrad reverseX() {return new Tetrad(wi, -xi, yi, zi);}
	public Tetrad reverseY() {return new Tetrad(wi, xi, -yi, zi);}
	public Tetrad reverseZ() {return new Tetrad(wi, xi, yi, -zi);}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Tetrad)) {
			return false;
		} else {
			Tetrad p = (Tetrad) o;
			return wi == p.wi && xi == p.xi && yi == p.yi && zi == p.zi;
		}
	}
	
	public String toString() {
		return String.format("(%d, %d, %d, %d)", wi, xi, yi, zi);
	}
	
	public int getW() {
		return wi;
	}
	
	public int getX() {
		return xi;
	}
	
	public int getY() {
		return yi;
	}
	
	public int getZ() {
		return zi;
	}
}

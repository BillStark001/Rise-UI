package com.billstark001.riseui.computation;

public final class Triad {
	
	private final int xi, yi, zi;
	
	public Triad(int x, int y, int z) {
		xi = x;
		yi = y;
		zi = z;
	}
	
	public Triad(Triad p) {
		xi = p.xi;
		yi = p.yi;
		zi = p.zi;
	}
	
	public final Triad add(Triad p) {
		return new Triad(xi + p.xi, yi + p.yi, zi + p.zi);
	}
	
	public final Triad minus(Triad p) {
		return new Triad(xi - p.xi, yi - p.yi, zi - p.zi);
	}
	
	public final Triad reverse() {
		return new Triad(-xi, -yi, -zi);
	}
	
	public final Triad xOnly() {
		return new Triad(xi, 0, 0);
	}
	
	public final Triad yOnly() {
		return new Triad(0, yi, 0);
	}
	
	public final Triad zOnly() {
		return new Triad(0, 0, zi);
	}
	
	public final Triad reverseX() {return new Triad(-xi, yi, zi);}
	public final Triad reverseY() {return new Triad(xi, -yi, zi);}
	public final Triad reverseZ() {return new Triad(xi, yi, -zi);}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Triad)) {
			return false;
		} else {
			Triad p = (Triad) o;
			return xi == p.xi && yi == p.yi && zi == p.zi;
		}
	}
	
	public final String toString() {
		return String.format("(%d, %d, %d)", xi, yi, zi);
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

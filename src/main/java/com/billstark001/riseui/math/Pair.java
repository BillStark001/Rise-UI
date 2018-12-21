package com.billstark001.riseui.math;

public final class Pair {
	
	private final int xi, yi;
	
	public Pair(int x, int y) {
		xi = x;
		yi = y;
	}
	
	public Pair(Pair p) {
		xi = p.xi;
		yi = p.yi;
	}
	
	public final Pair reverse() {
		return new Pair(-xi, -yi);
	}
	
	public final Pair add(Pair p) {
		return new Pair(xi + p.xi, yi + p.yi);
	}
	
	public final Pair minus(Pair p) {
		return new Pair(xi - p.xi, yi - p.yi);
	}
	
	public final Pair xOnly() {
		return new Pair(xi, 0);
	}
	
	public final Pair yOnly() {
		return new Pair(0, yi);
	}
	
	public final Pair reverseX() {return new Pair(-xi, yi);}
	public final Pair reverseY() {return new Pair(xi, -yi);}
	
	public final boolean equals(Pair p) {
		return xi == p.xi && yi == p.yi;
	}
	
	public final String toString() {
		return String.format("(%d, %d)", xi, yi);
	}
	
	public final int[] toIntArray() {
		int[] ans = {xi, yi};
		return ans;
	}
	
	public final Integer[] toIntegerArray() {
		Integer[] ans = {xi, yi};
		return ans;
	}
	
	public final int getX() {
		return xi;
	}
	
	public final int getY() {
		return yi;
	}
	
}

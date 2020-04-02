package com.billstark001.riseui.computation;

import com.billstark001.riseui.base.shading.mat.Texture2DFromRes;

public final class Pair {
	
	private final int xi, yi;
	
	public static final Pair PAIR_0_0 = new Pair(0);
	public static final Pair PAIR_1_1 = new Pair(1);
	public static final Pair PAIR_2_2 = new Pair(2);
	public static final Pair PAIR_3_3 = new Pair(3);
	public static final Pair PAIR_4_4 = new Pair(4);
	public static final Pair PAIR_8_8 = new Pair(8);
	public static final Pair PAIR_16_16 = new Pair(16);
	public static final Pair PAIR_32_32 = new Pair(32);
	
	public Pair(int x, int y) {
		xi = x;
		yi = y;
	}
	
	public Pair(int x) {this(x, x);}
	
	public Pair(Pair p) {
		xi = p.xi;
		yi = p.yi;
	}
	
	public Pair reverse() {
		return new Pair(-xi, -yi);
	}
	
	public Pair add(Pair p) {
		return new Pair(xi + p.xi, yi + p.yi);
	}
	
	public Pair minus(Pair p) {
		return new Pair(xi - p.xi, yi - p.yi);
	}
	
	public Pair xOnly() {
		return new Pair(xi, 0);
	}
	
	public Pair yOnly() {
		return new Pair(0, yi);
	}
	
	public Pair reverseX() {return new Pair(-xi, yi);}
	public Pair reverseY() {return new Pair(xi, -yi);}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof Pair)) {
			return false;
		} else {
			Pair p = (Pair) o;
			return xi == p.xi && yi == p.yi;
		}
	}
	
	public String toString() {
		return String.format("(%d, %d)", xi, yi);
	}
	
	public int[] toIntArray() {
		int[] ans = {xi, yi};
		return ans;
	}
	
	public Integer[] toIntegerArray() {
		Integer[] ans = {xi, yi};
		return ans;
	}
	
	public int getX() {
		return xi;
	}
	
	public int getY() {
		return yi;
	}
	
}

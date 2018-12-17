package com.billstark001.riseui.math;

public final class Pair {
	
	private final boolean D;
	private final int xi, yi;
	private final double xd, yd;
	//public final Pair ZERO_I = new Pair(0, 0);
	//public final Pair ZERO_D = new Pair(0., 0.);
	
	public Pair(int x, int y) {
		xi = x;
		yi = y;
		xd = 0;
		yd = 0;
		D = false;
	}
	
	public Pair(double x, double y) {
		xi = 0;
		yi = 0;
		xd = x;
		yd = y;
		D = true;
	}
	
	public Pair(Pair p) {
		xi = p.xi;
		yi = p.yi;
		xd = p.xd;
		yd = p.yd;
		D = p.D;
	}
	
	public final Pair reverse() {
		if(D == false) return new Pair(-xi, -yi);
		else return new Pair(-xd, -yd);
	}
	
	public final Pair add(Pair p) throws Exception {
		if(D != p.D) throw new Exception("Different data formats!"); 
		if(D == false) return new Pair(xi + p.xi, yi + p.yi);
		else return new Pair(xd + p.xd, yd + p.yd);
	}
	
	public final Pair minus(Pair p) throws Exception {
		if(D != p.D) throw new Exception("Different data formats!"); 
		if(D == false) return new Pair(xi - p.xi, yi - p.yi);
		else return new Pair(xd - p.xd, yd - p.yd);
	}
	
	public final Pair xOnly() {
		if(D == false) return new Pair(xi, 0);
		else return new Pair(xd, 0);
	}
	
	public final Pair yOnly() {
		if(D == false) return new Pair(0, yi);
		else return new Pair(0, yd);
	}
	
	public final Pair reverseX() throws Exception {return this.xOnly().reverse().add(this.yOnly());}
	public final Pair reverseY() throws Exception {return this.yOnly().reverse().add(this.xOnly());}
	
	public final boolean isDouble() {
		return D;
	}
	
	public final boolean equals(Pair p) {
		if(D == false) return xi == p.xi && yi == p.yi;
		else return xd == p.xd && yd == p.yd;
	}
	
	public final String toString() {
		if(D == false) return String.format("(%d,%d)", xi, yi);
		else return String.format("(%g,%g)", xd, yd);
	}
	
	public final Object getX() {
		if(D == false) return xi;
		else return xd;
	}
	
	public final Object getY() {
		if(D == false) return yi;
		else return yd;
	}
	
}

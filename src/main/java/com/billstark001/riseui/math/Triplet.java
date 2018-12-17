package com.billstark001.riseui.math;

public final class Triplet {
	
	private final boolean D;
	private final int xi, yi, zi;
	private final double xd, yd, zd;
	
	public Triplet(int x, int y, int z) {
		xi = x;
		yi = y;
		zi = z;
		xd = 0;
		yd = 0;
		zd = 0;
		D = false;
	}
	
	public Triplet(double x, double y, double z) {
		xi = 0;
		yi = 0;
		zi = 0;
		xd = x;
		yd = y;
		zd = z;
		D = true;
	}
	
	public Triplet(Triplet p) {
		xi = p.xi;
		yi = p.yi;
		zi = p.zi;
		xd = p.xd;
		yd = p.yd;
		zd = p.zd;
		D = p.D;
	}
	
	public final Triplet add(Triplet p) throws Exception {
		if(D != p.D) throw new Exception("Different data formats!"); 
		if(D == false) return new Triplet(xi + p.xi, yi + p.yi, zi + p.zi);
		else return new Triplet(xd + p.xd, yd + p.yd, zd + p.zd);
	}
	
	public final Triplet minus(Triplet p) throws Exception {
		if(D != p.D) throw new Exception("Different data formats!"); 
		if(D == false) return new Triplet(xi - p.xi, yi - p.yi, zi - p.zi);
		else return new Triplet(xd - p.xd, yd - p.yd, zd - p.zd);
	}
	
	public final Triplet reverse() {
		if(D == false) return new Triplet(-xi, -yi, -zi);
		else return new Triplet(-xd, -yd, -zd);
	}
	
	public final boolean isDouble() {
		return D;
	}
	
	public final Triplet xOnly() {
		if(D == false) return new Triplet(xi, 0, 0);
		else return new Triplet(xd, 0, 0);
	}
	
	public final Triplet yOnly() {
		if(D == false) return new Triplet(0, yi, 0);
		else return new Triplet(0, yd, 0);
	}
	
	public final Triplet zOnly() {
		if(D == false) return new Triplet(0, 0, zi);
		else return new Triplet(0, 0, zd);
	}
	
	public final Triplet reverseX() throws Exception {return this.xOnly().reverse().add(this.yOnly()).add(this.zOnly());}
	public final Triplet reverseY() throws Exception {return this.yOnly().reverse().add(this.xOnly()).add(this.zOnly());}
	public final Triplet reverseZ() throws Exception {return this.zOnly().reverse().add(this.xOnly()).add(this.yOnly());}
	
	public final boolean equals(Triplet p) {
		if(D == false) return xi == p.xi && yi == p.yi && zi == p.zi;
		else return xd == p.xd && yd == p.yd && zd == p.zd;
	}
	
	public final String toString() {
		if(D == false) return String.format("(%d,%d,%d)", xi, yi, zi);
		else return String.format("(%f,%f,%f)", xd, yd, zd);
	}
	
	public final Object getX() {
		if(D == false) return xi;
		else return xd;
	}
	
	public final Object getY() {
		if(D == false) return yi;
		else return yd;
	}
	
	public final Object getZ() {
		if(D == false) return zi;
		else return zd;
	}
}

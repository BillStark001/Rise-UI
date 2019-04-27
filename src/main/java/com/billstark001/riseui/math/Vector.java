package com.billstark001.riseui.math;

import java.util.Arrays;

public final class Vector {

	private final int dim;
	private final double[] elements;
	private final double length;
	
	public static final Vector UNIT0_D2 = new Vector(0, 0);
	public static final Vector UNIT0_D3 = new Vector(0, 0, 0);
	public static final Vector UNIT0_D4 = new Vector(0, 4, true);
	public static final Vector UNIT1_D2 = new Vector(1, 1);
	public static final Vector UNIT1_D3 = new Vector(1, 1, 1);
	public static final Vector UNIT1_D4 = new Vector(1, 4, true);
	
	public Vector(double[] elements) {
		dim = elements.length;
		this.elements = elements.clone();
		this.length = calcLength(elements);
	}
	
	public Vector(double x) {
		dim = 1;
		double[] a = {x};
		elements = a;
		this.length = calcLength(elements);
	}
	
	public Vector(double x, double y) {
		dim = 2;
		double[] a = {x, y};
		elements = a;
		this.length = calcLength(elements);
	}
	
	public Vector(double x, double y, double z) {
		dim = 3;
		double[] a = {x, y, z};
		elements = a;
		this.length = calcLength(elements);
	}
	
	public Vector(double d, int dim, boolean demix) {
		this.dim = dim;
		double[] d_ = new double[dim];
		for (int i = 0; i < dim; ++i) d_[i] = d;
		elements = d_;
		this.length = calcLength(elements);
	}
	
	public Vector(Vector v) {
		this(v.elements);
	}
	
	public Vector(float[] elements) {
		dim = elements.length;
		this.elements = new double[elements.length];
		for (int i = 0; i < elements.length; ++i) this.elements[i] = elements[i];
		this.length = calcLength(this.elements);
	}

	public static final Vector Zeros(int length) {
		double[] a = new double[length];
		return new Vector(a);
	}
	
	//Base functions
	public int getDimension() {return dim;}
	public double getLength() {return length;}
	public String toString() {return Arrays.toString(elements);}
	public String toString(boolean b) {
		StringBuffer ans = new StringBuffer();
		ans.append('[');
		for (double d: elements) {
			if (d >= 0) ans.append(' ');
			ans.append(String.format("%g", d));
			ans.append('\t');
		}
		ans.append(']');
		return ans.toString();
	}
	public Vector clone() {return new Vector(elements);}
	public boolean equals(Vector v) {
		if(dim != v.getDimension()) return false;
		for(int i = 0; i < dim; ++i) if(elements[i] != v.elements[i]) return false;
		return true;
	}
	
	public final double get(int position) {return elements[position];}
	public final Vector get(int start, int end) {return new Vector(Arrays.copyOfRange(elements, start, end));}
	
	public final Vector concatenate(Vector v) {
		double[] temp = new double[dim + v.dim];
		for(int i = 0; i < temp.length; ++i) {
			if(i < dim) temp[i] = elements[i];
			else temp[i] = v.elements[i - dim];
		}
		return new Vector(temp);
	}
	
	public final Vector concatenate(double[] d) {
		double[] temp = new double[dim + d.length];
		for(int i = 0; i < temp.length; ++i) {
			if(i < dim) temp[i] = elements[i];
			else temp[i] = d[i - dim];
		}
		return new Vector(temp);
	}
	
	public final Vector set(int start, int end, Vector vals) {
		Vector v1 = this.get(0, start);
		Vector v3 = this.get(end, dim);
		Vector v2 = v1.concatenate(vals).concatenate(v3);
		return v2;
	}
	public final Vector set(int start, int end, double[] vals) {return set(start, end, new Vector(vals));}
	public final Vector set(int position, double val) {return set(position, position + 1, new Vector(val));}
	
	public final Vector insert(int position, Vector vals) {return set(position, position, vals);}
	public final Vector insert(int position, double[] vals) {return set(position, position, vals);}
	public final Vector insert(int position, double val) {return set(position, val);}
	
	//Math functions
	public final Vector addElementWise(Vector v) {
		double[] temp = new double[Math.max(dim, v.dim)];
		for(int i = 0; i < temp.length; ++i) {
			int res = 0;
			if(i < dim) res += elements[i];
			if(i < v.dim) res += v.get(i);
			temp[i] = res;
		}
		return new Vector(temp);
	}
	
	public final Vector add(Vector v) {
		if(dim != v.dim)
			try {
				throw new Exception(String.format("Unexpected vector dimension(expect %d, got %d)", dim, v.dim));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		double[] temp = new double[dim];
		for(int i = 0; i < dim; ++i) temp[i] = elements[i] + v.get(i);
		return new Vector(temp);
	}
	
	public final Vector mult(double x) {
		double[] temp = elements.clone();
		for(int i = 0; i < dim; ++i) temp[i] *= x;
		return new Vector(temp);
	}
	public final Vector normalize() {if (this.getLength() == 0)return this; return this.mult(1 / this.getLength());}
	
	public final Vector minusElementWise(Vector v) {return this.addElementWise(v.mult(-1));}
	public final Vector minus(Vector v) {return this.add(v.mult(-1));}
	
	public final Vector mult(Vector v) {
		int dim = Math.min(getDimension(), v.getDimension());
		double[] d = new double[dim];
		for(int i = 0; i < dim; ++i) d[i] = elements[i] * v.elements[i];
		return new Vector(d);
	}
	
	public final Vector div(Vector v) {
		int dim = Math.min(getDimension(), v.getDimension());
		double[] d = new double[dim];
		for(int i = 0; i < dim; ++i) d[i] = elements[i] / v.elements[i];
		return new Vector(d);
	}
	
	public final double dot(Vector v) {
		if(dim != v.dim)
			try {
				throw new Exception(String.format("Unexpected vector dimension(expect %d, got %d)", dim, v.dim));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		double ans = 0;
		for(int i = 0; i < dim; ++i) ans += elements[i] * v.get(i);
		return ans;
	}
	
	public final Vector cross(Vector v) {
		if(dim == v.dim && dim == 2) return new Vector(elements[0] * v.get(1), elements[1] * v.get(0));
		else if(dim == v.dim && dim == 3) {
			double x = this.get(1) * v.get(2) - this.get(2) * v.get(1);
			double y = this.get(2) * v.get(0) - this.get(0) * v.get(2);
			double z = this.get(0) * v.get(1) - this.get(1) * v.get(0);
			return new Vector(x, y, z);
		} else
			try {
				throw new Exception(String.format("No declarations of cross product on dimension %d and %d(expect 2 and 2 or 3 and 3)", dim, v.dim));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	//Utils
	public static final int maxDim(Vector[] v) {
		int temp = 0;
		for(int i = 0; i < v.length; ++i) temp = Math.max(temp, v[i].getDimension());
		return temp;
	}
	
	private static final double calcLength(double[] d) {
		double ans = 0;
		for(int i = 0; i < d.length; ++i) ans += d[i] * d[i];
		return Math.sqrt(ans);
	}

}

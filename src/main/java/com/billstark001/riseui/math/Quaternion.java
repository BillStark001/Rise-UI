package com.billstark001.riseui.math;

public final class Quaternion {
	
	private final double real;
	private final Vector imaginary;
	
	public Quaternion () {
		real = 0;
		imaginary = new Vector(0, 0, 0);
	}
	
	public Quaternion (Quaternion q) {
		real = q.getReal();
		imaginary = q.getImaginary();
	}
	
	public Quaternion (double r, Vector i) {
		real = r;
		imaginary = i;
	}
	
	public Quaternion (double r, double x, double y, double z) {
		real = r;
		imaginary = new Vector(x, y, z);
	}
	
	public Quaternion (double r) {this(r, 0, 0, 0);}
	public Quaternion (Vector i) {this(0, i);}
	
	public String toString() {
		return String.format("(%f, %s)", real, imaginary.toString());
	}
	
	public static final Quaternion UNIT = new Quaternion(1, Vector.Zeros(3));
	
	public double getReal() {return real;}
	public Vector getImaginary() {return imaginary;}
	public double get (int index) {
		if(index < 0 || index > 3) throw new IndexOutOfBoundsException("The index must be 0, 1, 2 or 3!");
		if(index == 0) return real;
		else return imaginary.get(index - 1);
	}

	public boolean isPure () {return getReal() == 0;}
	public boolean equals (Quaternion q) {return getReal() == q.getReal() && getImaginary().equals(q.getImaginary());}
	
	public Quaternion add (Quaternion q) {
		return new Quaternion(getReal() + q.getReal(), getImaginary().add(q.getImaginary()));
	}
	
	public Quaternion mult (Quaternion q) {
		double p0 = this.getReal();
		double q0 = q.getReal();
		Vector pv = this.getImaginary();
		Vector qv = q.getImaginary();
		return new Quaternion(p0 * q0 + pv.dot(qv), qv.mult(p0).add(pv.mult(q0)).add(pv.cross(qv)));
	}
	
	private Quaternion multScalar(double n) {
		return new Quaternion(real * n, imaginary.mult(n));
	}
	
	public double dot (Quaternion q) {
		return getReal() * q.getReal() + getImaginary().dot(q.getImaginary());
	}
	
	public Vector cross (Quaternion q) {
		return q.getImaginary().cross(getImaginary());
	}
	
	public Quaternion conj () {return new Quaternion(getReal(), getImaginary().mult(-1));}
	
	public double norm() {return real * real + imaginary.getLength() * imaginary.getLength();}
	
	public Quaternion inverse() {return conj().multScalar(1 / norm());}
	
	//Utils
	public static boolean isConj (Quaternion p, Quaternion q) {return p.getReal() == q.getReal() && p.getImaginary().add(q.getImaginary()).equals(Vector.Zeros(3));}
	
	public static Vector quatToEuler (Quaternion q) {
		double w, x, y, z;
		w = q.real;
		x = q.imaginary.get(0);
		y = q.imaginary.get(1);
		z = q.imaginary.get(2);
		double phi = Math.atan2((w*x+y*z)*2, 1-(x*x+y*y)*2);
		double theta = Math.asin((w*y-x*z)*2);
		double psi = Math.atan2((w*z+y*x)*2, 1-(z*z+y*y)*2);
		return new Vector(phi, theta, psi);
	}
	
	public static Quaternion eulerToQuat (Vector v) {
		double w = Math.cos(v.get(0) / 2) * Math.cos(v.get(1) / 2) * Math.cos(v.get(2) / 2) + Math.sin(v.get(0) / 2) * Math.sin(v.get(1) / 2) * Math.sin(v.get(2) / 2);
		double x = Math.sin(v.get(0) / 2) * Math.cos(v.get(1) / 2) * Math.cos(v.get(2) / 2) - Math.cos(v.get(0) / 2) * Math.sin(v.get(1) / 2) * Math.sin(v.get(2) / 2);
		double y = Math.cos(v.get(0) / 2) * Math.sin(v.get(1) / 2) * Math.cos(v.get(2) / 2) + Math.sin(v.get(0) / 2) * Math.cos(v.get(1) / 2) * Math.sin(v.get(2) / 2);
		double z = Math.cos(v.get(0) / 2) * Math.cos(v.get(1) / 2) * Math.sin(v.get(2) / 2) - Math.sin(v.get(0) / 2) * Math.sin(v.get(1) / 2) * Math.cos(v.get(2) / 2);
		return new Quaternion(w, x, y, z);
	}
	
	public static Matrix eulerToRotate (Vector v) {
		double sz, cz, sy, cy, sx, cx;
		sz = Math.sin(v.get(0)); sy = Math.sin(v.get(1)); sx = Math.sin(v.get(2));
		cz = Math.cos(v.get(0)); cy = Math.cos(v.get(1)); cx = Math.cos(v.get(2));
		/*double[][] dt = {
				{cx*cy,				cx*sy,				-sx 	},
				{sz*sx*cy-cz*sy,	sz*sx*sy+cz*cy,		sz*cx	},
				{cz*sx*cy+sz*sy,	cz*sx*sy-sz*cy,		cz*cx	}
		};*/
		double[][] dt = {
				{cy*cz,	cz*sx*sy-cx*sz,	sx*sz+cx*sy*cz 	},
				{cy*sz,	cx*cz+sx*sy*sz,	cx*sy*sz-cz*sz	},
				{-sy,	cy*sx,			cx*cy			}
		};
		return new Matrix(dt);
	}
	
	public static Matrix quatToRotate (Quaternion q) {
		double w, x, y, z;
		w = q.real;
		x = q.imaginary.get(0);
		y = q.imaginary.get(1);
		z = q.imaginary.get(2);
		double[][] dt = {
				{1-2*y*y-2*z*z,	2*x*y-2*w*z,	2*x*z+2*w*y 	},
				{2*x*y+2*w*z,	1-2*z*z-2*x*x,	2*y*z-2*w*x		},
				{2*x*z-2*w*y,	2*y*z+2*w*x,	1-2*x*x-2*y*y	}
		};
		return new Matrix(dt);
	}
	
	public static Matrix quatToRotate2 (Quaternion q) {
		Vector v = quatToEuler(q);
		return eulerToRotate(v);
	}
	
	public static Quaternion axisRotate(Vector axis, double angle) {
		axis = axis.normalize();
		return new Quaternion(Math.cos(angle), axis.mult(Math.sin(angle)));
	}
	
	public static Quaternion reverseAxisRotate(Quaternion q) {
		double angle = Math.asin(q.getImaginary().getLength());
		if (q.getReal() < 0) angle = Math.PI - angle;
		return new Quaternion(angle, q.getImaginary().normalize());
	}
	
}

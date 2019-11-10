package com.billstark001.riseui.computation;

import java.nio.FloatBuffer;
import net.minecraft.util.math.MathHelper;

public final class Quaternion {
	
	private final double real;
	private final Vector imaginary;
	private boolean isrec = false;
	
	public boolean isAARecord() {return isrec;}
	private void setRec(boolean rec) {this.isrec = rec;}

	public Quaternion() {
		real = 0;
		imaginary = new Vector(0, 0, 0);
	}
	
	public Quaternion(Quaternion q) {
		real = q.getReal();
		imaginary = q.getImaginary();
	}
	
	public Quaternion(double r, Vector i) {
		real = r;
		imaginary = i;
	}
	
	public Quaternion(double r, double x, double y, double z) {
		real = r;
		imaginary = new Vector(x, y, z);
	}
	
	public Quaternion(double r) {this(r, 0, 0, 0);}
	public Quaternion(Vector i) {this(0, i);}
	
	public String toString() {
		return String.format("(%f, %s)", real, imaginary.toString());
	}
	
	public static final Quaternion UNIT = new Quaternion(1, Vector.Zeros(3));
	
	public double getReal() {return real;}
	public Vector getImaginary() {return imaginary;}
	public double get(int index) {
		if(index < 0 || index > 3) throw new IndexOutOfBoundsException("The index must be 0, 1, 2 or 3!");
		if(index == 0) return real;
		else return imaginary.get(index - 1);
	}
	public float getF(int index) {
		if(index < 0 || index > 3) throw new IndexOutOfBoundsException("The index must be 0, 1, 2 or 3!");
		if(index == 0) return (float) real;
		else return (float) imaginary.get(index - 1);
	}

	public boolean isPure() {return getReal() == 0;}
	public boolean equals(Quaternion q) {return getReal() == q.getReal() && getImaginary().equals(q.getImaginary());}
	
	public Quaternion add(Quaternion q) {
		return new Quaternion(getReal() + q.getReal(), getImaginary().add(q.getImaginary()));
	}
	
	public Quaternion mult(Quaternion q) {
		double p0 = this.getReal();
		double q0 = q.getReal();
		Vector pv = this.getImaginary();
		Vector qv = q.getImaginary();
		return new Quaternion(p0 * q0 + pv.dot(qv), qv.mult(p0).add(pv.mult(q0)).add(pv.cross(qv)));
	}
	
	private Quaternion multScalar(double n) {
		return new Quaternion(real * n, imaginary.mult(n));
	}
	
	public double dot(Quaternion q) {
		return getReal() * q.getReal() + getImaginary().dot(q.getImaginary());
	}
	
	public Vector cross(Quaternion q) {
		return q.getImaginary().cross(getImaginary());
	}
	
	public Quaternion conj() {return new Quaternion(getReal(), getImaginary().mult(-1));}
	
	public double norm() {return real * real + imaginary.getLength() * imaginary.getLength();}
	
	public Quaternion inverse() {return conj().multScalar(1 / norm());}
	
	//Utils
	public static boolean isConj(Quaternion p, Quaternion q) {return p.getReal() == q.getReal() && p.getImaginary().add(q.getImaginary()).equals(Vector.Zeros(3));}
	
	public static Vector quatToEuler(Quaternion q) {
		double w, x, y, z;
		w = q.real;
		x = q.imaginary.get(0);
		y = q.imaginary.get(1);
		z = q.imaginary.get(2);
		double phi = MathHelper.atan2((w*z+y*x)*2, 1-(z*z+x*x)*2);
		double tht = Math.asin((w*x-y*z)*2);
		double psi = MathHelper.atan2((w*y+z*x)*2, 1-(x*x+y*y)*2);
		return new Vector(tht, psi, phi);
	}
	
	public static Quaternion eulerToQuat(Vector v) {
		double a = v.get(1) / 2, b = v.get(0) / 2, r = v.get(2) / 2;
		double sa = Math.sin(a), ca = Math.cos(a);
		double sb = Math.sin(b), cb = Math.cos(b);
		double sr = Math.sin(r), cr = Math.cos(r);
		double w = ca * cb * cr + sa * sb * sr;
		double x = ca * sb * cr + sa * cb * sr;
		double y = sa * cb * cr - ca * sb * sr;
		double z = ca * cb * sr - sa * sb * cr;
		return new Quaternion(w, x, y, z);
	}
	
	public static Quaternion eulerToQuatFast(Vector v) {
		float phi = (float) v.get(2) / 2, tht = (float) v.get(0) / 2, ksi = (float) v.get(1) / 2;
		float sp = MathHelper.sin(phi), cp = MathHelper.cos(phi);
		float st = MathHelper.sin(tht), ct = MathHelper.cos(tht);
		float sk = MathHelper.sin(ksi), ck = MathHelper.cos(ksi);
		float w = cp * ct * ck + sp * st * sk;
		float x = cp * st * ck + sp * ct * sk;
		float y = cp * ct * sk - sp * st * ck;
		float z = sp * ct * ck - cp * st * sk;
		return new Quaternion(w, x, y, z);
	}
	
	public static Matrix eulerToRotMat(Vector v) {
		double sy, cy, sx, cx, sz, cz;
		sx = Math.sin(v.get(0)); 
		sy = Math.sin(v.get(1)); 
		sz = Math.sin(v.get(2));
		cx = Math.cos(v.get(0)); 
		cy = Math.cos(v.get(1)); 
		cz = Math.cos(v.get(2));
		/*
		double[][] dt = {
				{cy*cz,	cz*sx*sy-cx*sz,	sx*sz+cx*sy*cz 	},
				{cy*sz,	cx*cz+sx*sy*sz,	cx*sy*sz-cz*sz	},
				{-sy,	cy*sx,			cx*cy			}
		};
		*/
		double[][] dt = {
				{cx*cy,	cy*sz*sx-cz*sy,	sz*sy+cz*sx*cy 	},
				{cx*sy,	cz*cy+sz*sx*sy,	cz*sx*sy-cy*sy	},
				{-sx,	cx*sz,			cz*cx			}
		};
		return new Matrix(dt);
	}
	
	public static Matrix eulerToRotMatFast(Vector v) {
		double sy, cy, sx, cx, sz, cz;
		sx = MathHelper.sin((float) v.get(0)); 
		sy = MathHelper.sin((float) v.get(1)); 
		sz = MathHelper.sin((float) v.get(2));
		cx = MathHelper.cos((float) v.get(0)); 
		cy = MathHelper.cos((float) v.get(1)); 
		cz = MathHelper.cos((float) v.get(2));
		double[][] dt = {
				{cx*cy,	cy*sz*sx-cz*sy,	sz*sy+cz*sx*cy 	},
				{cx*sy,	cz*cy+sz*sx*sy,	cz*sx*sy-cy*sy	},
				{-sx,	cx*sz,			cz*cx			}
		};
		return new Matrix(dt);
	}
	
	public static Matrix quatToRotMat(Quaternion q) {
		double w, x, y, z;
		w = -q.real;
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
	
	@Deprecated
	public static Matrix quatToRotMat2(Quaternion q) {
		Vector v = quatToEuler(q);
		return eulerToRotMat(v);
	}
	
	public static Quaternion rotMatToQuat(Matrix M) {
		double m00, m11, m22;
		m00 = M.get(0, 0);
		m11 = M.get(1, 1);
		m22 = M.get(2, 2);
		double q0, q1, q2, q3;
		q0 = Math.sqrt(1 + m00 + m11 + m22) * 0.5; // real
		q1 = Math.sqrt(1 + m00 - m11 - m22) * 0.5;
		q2 = Math.sqrt(1 - m00 + m11 - m22) * 0.5;
		q3 = Math.sqrt(1 - m00 - m11 + m22) * 0.5;
		double sq1, sq2, sq3, sq0;
		sq0 = 1;
		sq1 = sq0 * Math.signum(-M.get(2, 1)+M.get(1, 2));
		sq2 = sq0 * Math.signum(-M.get(0, 2)+M.get(2, 0));
		sq3 = sq0 * Math.signum(-M.get(1, 0)+M.get(0, 1));
		return new Quaternion(q0 * sq0, q1 * sq1, q2 * sq2, q3 * sq3);
	}
	
	public static Quaternion getAARByAA(Vector axis, double angle) {
		axis = axis.normalize();
		Quaternion ans = new Quaternion(angle, axis);
		ans.setRec(true);
		return ans;
	}
	
	public static Quaternion getQuatByAAR(Quaternion q) {
		if (!q.isAARecord()) return q;
		else return getQuatByAA(q.getImaginary(), q.getReal());
	}
	public static Quaternion getQuatByAA(Vector axis, double angle) {
		axis = axis.normalize();
		return new Quaternion(Math.cos(angle), axis.mult(Math.sin(angle)));
	}
	
	public static Quaternion getAARByQuat(Quaternion q) {
		double angle = Math.asin(q.getImaginary().getLength());
		if (q.getReal() < 0) angle = Math.PI - angle;
		Quaternion ans =  Quaternion.getAARByAA(q.getImaginary().normalize(), angle);
		return ans;
	}
	
	// Strange Utils
	
}

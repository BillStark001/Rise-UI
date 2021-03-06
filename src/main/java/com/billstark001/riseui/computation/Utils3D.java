package com.billstark001.riseui.computation;

import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;

public final class Utils3D {
	
	// Interpolation
	
	public static Vector bezier3(double t, Vector p0, Vector p1, Vector p2, Vector p3) {
		double t_ = 1 - t;
		Vector ans = p0.mult(t_ * t_ * t_);
		ans = ans.add(p1.mult(3 * t * t_ * t_));
		ans = ans.add(p2.mult(3 * t * t * t_));
		ans = ans.add(p3.mult(t * t * t));
		return ans;
	}
	
	public static Vector bezier2(double t, Vector p0, Vector p1, Vector p2) {
		double t_ = 1 - t;
		Vector ans = p0.mult(t_ * t_);
		ans = ans.add(p1.mult(2 * t * t_));
		ans = ans.add(p2.mult(t * t));
		return ans;
	}
	
	public static Vector linear(double t, Vector p0, Vector p1) {
		double t_ = 1 - t;
		Vector ans = p0.mult(t_);
		ans = ans.add(p1.mult(t));
		return ans;
	}
	
	public static Matrix linear(double t, Matrix m0, Matrix m1) {
		double t_ = 1 - t;
		Matrix ans = m0.mult(t_);
		ans.add(m1.mult(t));
		return ans;
	}

	// Composition and splitting
	
	public static Vector compOffset(Vector pos1, Vector pos2) {
		return pos1.add(pos2);
	}
	
	public static Vector splitOffset(Vector pos1, Vector pos2) {
		/*
		 * Assume returns pos3, then:
		 * pos1 = pos3 + pos2
		 */
		return pos1.minus(pos2);
	}
	
	public static Vector compZoom(Vector scale1, Vector scale2) {
		return scale1.mult(scale2);
	}
	
	public static Vector splitZoom(Vector scale1, Vector scale2) {
		/*
		 * Assume returns scale3, then:
		 * scale1 = scale3 * scale2
		 */
		return scale1.div(scale2);
	}
	
	public static Quaternion compRotate(Quaternion rot1, Quaternion rot2) {
		boolean aar_mark = rot1.isAARecord();
		if (rot1.isAARecord()) rot1 = Quaternion.getQuatByAAR(rot1);
		if (rot2.isAARecord()) rot2 = Quaternion.getQuatByAAR(rot2);
		Quaternion ans = rot1.mult(rot2);
		if (aar_mark) rot1 = Quaternion.getAARByQuat(rot1);
		return ans;
	}
	
	public static Quaternion splitRotate(Quaternion rot1, Quaternion rot2) {
		/*
		 * Assume returns rot3, then:
		 * rot1 = rot3.mult(rot2)
		 */
		boolean aar_mark = rot1.isAARecord();
		if (rot1.isAARecord()) rot1 = Quaternion.getQuatByAAR(rot1);
		if (rot2.isAARecord()) rot2 = Quaternion.getQuatByAAR(rot2);
		Quaternion ans = rot1.mult(rot2.inverse());
		if (aar_mark) rot1 = Quaternion.getAARByQuat(rot1);
		return ans;
	}
	
	public static Vector applyRotOnVec3(Vector v, Quaternion r) {
		if (r.isAARecord()) r = Quaternion.getQuatByAAR(r);
		//Quaternion p = new Quaternion(v);
		//return r.inverse().mult(p).mult(r).getImaginary();
		Matrix q = Quaternion.quatToRotMat(r);
		return new Matrix(v).mult(q).getLine(0);
	}
	
	// Homogeneous Matrix Transfer
	
	public static Matrix sclToHomoState(Vector s) {
		double[][] ds = {
				{s.get(0), 0, 0, 0},
				{0, s.get(1), 0, 0},
				{0, 0, s.get(2), 0},
				{0, 0, 0, 1}
		};
		return new Matrix(ds);
	}
	
	public static Matrix posToHomoState(Vector p) {
		double[][] dp = {
				{1, 0, 0, 0},
				{0, 1, 0, 0},
				{0, 0, 1, 0},
				{p.get(0), p.get(1), p.get(2), 1}
		};
		return new Matrix(dp);
	}
	
	public static Matrix rotToHomoState(Quaternion r) {
		return Matrix.homoExtend(Quaternion.quatToRotMat(r), 4);
	}
	
	public static Matrix compStateMat(Vector p, Quaternion r, Vector s) {
		return compStateMat(Quaternion.UNIT, s, r, p);
	}
	public static Matrix compStateMat(Quaternion r1, Vector s, Quaternion r2, Vector p) {
		Matrix ms = sclToHomoState(s);
		Matrix mp = posToHomoState(p);
		Matrix mr1 = rotToHomoState(r1);
		Matrix mr2 = rotToHomoState(r2);
		return mr1.mult(ms).mult(mr2).mult(mp);
	}
	
	public static State3DIntegrated decompStateMat(Matrix M) {
		Vector p = M.getLine(3).get(0, 3);
		Matrix[] msvd = UtilsLinalg.SVD(M.get(0, 0, 3, 3));
		Quaternion r1 = Quaternion.rotMatToQuat(msvd[0]);
		Vector s = msvd[1].getDiag();
		Quaternion r2 = Quaternion.rotMatToQuat(msvd[2]);
		return new State3DIntegrated(r1, s, r2, p);
	}
	
	public static Vector applyStateMat(Vector p, Matrix state) {return applyStateMat(new Matrix(p), state).getLine(0);}
	public static Matrix applyStateMat(Matrix pset, Matrix state) {
		if (state.getShape().equals(new Pair(3, 3))) {
			state = Matrix.homoExtend(state, 4);
		} else if (state.getShape().equals(new Pair(4, 4))) {
			
		} else {
			return null;
		}
		if (pset.getShape().getY() == 3) {
			pset = Matrix.expandColumn(pset, 4, 1);
			pset = pset.mult(state);
			pset = pset.getColumns(0, 3);
			return pset;
		} else if (pset.getShape().getY() == 4) {
			return pset.mult(state);
		} else {
			return null;
		}
	}
	
	// Offset, Rotate and Zoom
	/*
	public static final Vector getAirOrderAngle(Vector v, double roll) {
		v = v.mult(1 / v.getLength());
		double pitch = Math.asin(v.get(1) / v.getLength());
		double yaw = Math.atan(v.get(2) / v.get(0));
		if (v.get(2) == 1)
			yaw = Math.PI / 2;
		if (v.get(2) == -1)
			yaw = 3 * Math.PI / 2;
		return new Vector(yaw, pitch, roll);
	}

	public static final Matrix getRotateAffine(Vector v, double roll) {
		v = getAirOrderAngle(v, roll);
		double pitch = v.get(0);
		double yaw = v.get(1);
		double stheta, ctheta, spsi, cpsi, sphi, cphi;
		sphi = Math.sin(roll);
		spsi = Math.sin(yaw);
		stheta = Math.sin(pitch);
		cphi = Math.cos(roll);
		cpsi = Math.cos(yaw);
		ctheta = Math.cos(pitch);
		double[][] dt = {
				{ctheta*cpsi,					ctheta*spsi,				-stheta 	},
				{sphi*stheta*cpsi-cphi*spsi,	sphi*stheta*spsi+cphi*cpsi,	sphi*ctheta	},
				{cphi*stheta*cpsi+sphi*spsi,	cphi*stheta*spsi-sphi*cpsi,	cphi*ctheta	}
		};
		return new Matrix(dt).T();
	}
	*/
	public static Matrix offset(Matrix m, Vector v) {
		if (m == null)
			return null;
		Vector[] vtemp = m.toVecArray();
		for (int i = 0; i < vtemp.length; ++i) {
			vtemp[i] = vtemp[i].add(v);
		}
		return new Matrix(vtemp);
	}

	public static Matrix zoom(Matrix m, Vector v) {
		if (m == null)
			return null;
		Vector[] vtemp = m.toVecArray();
		if (m.getShape().getY() == 4) {
			for (int i = 0; i < vtemp.length; ++i) {
				vtemp[i] = new Vector(vtemp[i].get(0) * v.get(0), vtemp[i].get(1) * v.get(1), vtemp[i].get(2) * v.get(2), vtemp[i].get(3));
			}
		} else {
			for (int i = 0; i < vtemp.length; ++i) {
				vtemp[i] = new Vector(vtemp[i].get(0) * v.get(0), vtemp[i].get(1) * v.get(1), vtemp[i].get(2) * v.get(2));
			}
		}
		return new Matrix(vtemp);
	}

	public static Matrix zoom(Matrix m, double d) {
		if (m == null)
			return null;
		if (m.getShape().getY() == 4) return zoom(m, new Vector(d, d, d, 1));
		return m.mult(d);
	}

	public static Matrix rotate(Matrix m, Quaternion q) {
		if (m == null)
			return null;
		Matrix r = Quaternion.quatToRotMat(q);
		if (m.getShape().getY() == 4) r = Matrix.homoExtend(r, 4);
		return m.mult(r);
	}

	public static Matrix rotate(Matrix m, Vector v) {
		if (m == null)
			return null;
		Matrix r = Quaternion.eulerToRotMat(v);
		if (m.getShape().getY() == 4) r = Matrix.homoExtend(r, 4);
		return m.mult(r);
	}

	public static Matrix rotate(Matrix m, Matrix r) {
		if (m == null)
			return null;
		return m.mult(r);
	}

	public static Matrix rotToHomoState(Vector rot) {
		//return Matrix.homoExtend(Quaternion.eulerToRotate(rot), 4);
		return rotToHomoState(Quaternion.eulerToQuat(rot));
	}

	public static Vector genVerticalVector(Vector v) {
		if (v.getDimension() < 2) return null;
		else if (v.getDimension() == 2) {
			return new Vector(-v.get(1), v.get(0));
		}
		Vector vrest = v.get(3, v.getDimension());
		v = v.get(0, 3);
		Vector vt = Vector.ORTHO_Y3;
		double angle_sin = v.cross(vt).getLength() / (v.getLength() * vt.getLength());
		if (angle_sin < 0.01) vt = Vector.ORTHO_Z3;
		Vector vans = v.cross(vt).normalize();
		return vans.concatenate(vrest);
	}
	
	
}

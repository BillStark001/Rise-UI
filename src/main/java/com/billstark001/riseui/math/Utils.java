package com.billstark001.riseui.math;

public final class Utils {
	
	// Interpolation

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
		Quaternion ans;
		if (rot1.isAxisAndAngle()) {
			if (!rot2.isAxisAndAngle()) rot2 = Quaternion.reverseAxisRotate(rot2);
			Vector it;
			it = rot1.getImaginary().normalize().mult(rot1.getReal()).mult(rot2.getImaginary().normalize().mult(rot2.getReal()));
			ans = Quaternion.AxisAndAngle(it, it.getLength());
		} else {
			if (rot2.isAxisAndAngle()) rot2 = Quaternion.axisRotate(rot2.getImaginary(), rot2.getReal());
			ans = rot1.mult(rot2);
		}
		return ans;
	}
	
	public static Quaternion splitRotate(Quaternion rot1, Quaternion rot2) {
		/*
		 * Assume returns rot3, then:
		 * rot1 = rot3.mult(rot2)
		 */
		return rot1.mult(rot2.inverse());
	}
	
	// Offset, Rotate and Zoom
	
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
		for (int i = 0; i < vtemp.length; ++i) {
			vtemp[i] = new Vector(vtemp[i].get(0) * v.get(0), vtemp[i].get(1) * v.get(1), vtemp[i].get(2) * v.get(2));
		}
		return new Matrix(vtemp);
	}

	public static Matrix zoom(Matrix m, double d) {
		if (m == null)
			return null;
		return m.multScalar(d);
	}

	public static Matrix rotate(Matrix m, Quaternion q) {
		if (m == null)
			return null;
		Matrix r = Quaternion.quatToRotate(q);
		return m.mult(r);
	}

	public static Matrix rotate(Matrix m, Vector v) {
		if (m == null)
			return null;
		Matrix r = Quaternion.eulerToRotate(v);
		return m.mult(r);
	}

	public static Matrix rotate(Matrix m, Matrix r) {
		if (m == null)
			return null;
		return m.mult(r);
	}

}

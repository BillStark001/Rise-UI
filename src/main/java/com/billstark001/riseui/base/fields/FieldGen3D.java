package com.billstark001.riseui.base.fields;

import com.billstark001.riseui.base.nodestate.State3DBase;
import com.billstark001.riseui.base.nodestate.State3DIntegrated;
import com.billstark001.riseui.base.nodestate.State3DSimple;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Vector;

public class FieldGen3D extends Field<Matrix> {

	public static final Matrix DEFAULT_MAT = Matrix.I4;
	public final FieldGenSimple<Matrix, ?> r1, s, r2, p;
	
	public FieldGen3D(FieldGenSimple<Matrix, ?> r1, FieldGenSimple<Matrix, ?> s, FieldGenSimple<Matrix, ?> r2, FieldGenSimple<Matrix, ?> p) {
		this.r1 = r1;
		this.s = s;
		this.r2 = r2;
		this.p = p;
	}
	
	public FieldGen3D(FieldGenSimple<Matrix, ?> p, FieldGenSimple<Matrix, ?> r, FieldGenSimple<Matrix, ?> s) {this(null, s, r, p);}
	
	@Override
	public Matrix get(double time) {
		Matrix ans = DEFAULT_MAT;
		if (r1 != null) ans = ans.mult(r1.get(time));
		if (s != null) ans = ans.mult(s.get(time));
		if (r2 != null) ans = ans.mult(r2.get(time));
		if (p != null) ans = ans.mult(p.get(time));
		return ans;
	}

	@Override
	public boolean containsFrames() {
		boolean flag = true;
		if (r1 != null && !r1.containsFrames()) flag = false;
		if (s != null && !s.containsFrames()) flag = false;
		if (r2 != null && !r2.containsFrames()) flag = false;
		if (p != null && !p.containsFrames()) flag = false;
		return flag;
	}
	
	@Override
	public double getStartTime() {
		if (!this.containsFrames()) return 0;
		double ans = Double.MAX_VALUE;
		if (r1 != null) ans = Math.min(ans, r1.getStartTime());
		if (s != null) ans = Math.min(ans, s.getStartTime());
		if (r2 != null) ans = Math.min(ans, r2.getStartTime());
		if (p != null) ans = Math.min(ans, p.getStartTime());
		return ans;
	}
	
	@Override
	public double getEndTime() {
		if (!this.containsFrames()) return 0;
		double ans = Double.MIN_VALUE;
		if (r1 != null) ans = Math.max(ans, r1.getEndTime());
		if (s != null) ans = Math.max(ans, s.getEndTime());
		if (r2 != null) ans = Math.max(ans, r2.getEndTime());
		if (p != null) ans = Math.max(ans, p.getEndTime());
		return ans;
	}
	
	public State3DBase getSimpleState(double time) {
		if (r1 != null) return new State3DSimple(this.get(time));
		else {
			Vector sp = Vector.UNIT0_D3;
			Quaternion sr = Quaternion.UNIT;
			Vector ss = Vector.UNIT1_D3;
			if (s != null && s.getSource().getDataType() == Vector.class) ss = (Vector) s.getSource(time);
			if (r2 != null) {
				if (r2.getGenerator() == FieldUtils.GEN_ROT) sr = Quaternion.eulerToQuat((Vector) r2.getSource(time));
			}
			if (p != null && s.getSource().getDataType() == Vector.class) sp = (Vector) p.getSource(time);
			return new State3DIntegrated(sp, sr, ss);
		}
	}

	@Override
	public Class getDataType() {
		return Matrix.class;
	}

}

package com.billstark001.riseui.base.states.tracked3d;

import com.billstark001.riseui.base.states.simple3d.State3DBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.base.states.simple3d.State3DPos;
import com.billstark001.riseui.base.states.simple3d.State3DRot;
import com.billstark001.riseui.base.states.simple3d.State3DScl;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

public class Track3DIntegrated extends Track3DBase {

	public static final Matrix DEFAULT_MAT = Matrix.I4;
	public final Track3DRot r1, r2;
	public final Track3DPos p;
	public final Track3DScl s;
	
	public Track3DIntegrated(Track3DRot r1, Track3DScl s, Track3DRot r2, Track3DPos p) {
		this.r1 = r1;
		this.s = s;
		this.r2 = r2;
		this.p = p;
	}
	
	public Track3DIntegrated(Track3DPos p, Track3DRot r, Track3DScl s) {this(null, s, r, p);}
	public Track3DIntegrated(Track3DScl s, Track3DRot r, Track3DPos p) {this(null, s, r, p);}
	
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
		if (r1 != null) ans = Math.max(ans, r1.getStartTime());
		if (s != null) ans = Math.max(ans, s.getStartTime());
		if (r2 != null) ans = Math.max(ans, r2.getStartTime());
		if (p != null) ans = Math.max(ans, p.getStartTime());
		return ans;
	}
	
	@Override
	public State3DBase getSimpleState(double time) {
		if (r1 != null) return new State3DSimple(this.get(time));
		else {
			Vector sp = Vector.UNIT0_D3;
			Quaternion sr = Quaternion.UNIT;
			Vector ss = Vector.UNIT1_D3;
			if (s != null) ss = s.getStateRepr(time);
			if (r2 != null) {
				if (r2 instanceof Track3DRotEuler) sr = Quaternion.eulerToQuat(((Track3DRotEuler) r2).getStateRepr(time));
			}
			if (p != null) sp = p.getStateRepr(time);
			return new State3DIntegrated(sp, sr, ss);
		}
	}

}

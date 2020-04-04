package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Vector;

public class State3DIntegrated extends State3DSimple{
	
	private State3DRot r1;
	private State3DPos p;
	private State3DRot r2;
	private State3DScl s;
	
	private Matrix state_cache;
	
	private boolean dirty = true;
	
	private static final Matrix CALC_BASE = Matrix.I4;
	
	public State3DIntegrated(State3DIntegrated state) {
		this.r1 = new State3DRot(state.r1);
		this.p = new State3DPos(state.p);
		this.r2 = new State3DRot(state.r2);
		this.s = new State3DScl(state.s);
	}
	
	public State3DIntegrated(State3DRot r1, State3DScl s, State3DRot r2, State3DPos p) {
		this.r1 = new State3DRot(r1);
		this.p = new State3DPos(p);
		this.r2 = new State3DRot(r2);
		this.s = new State3DScl(s);
	}
	
	public State3DIntegrated(Quaternion r1, Vector s, Quaternion r2, Vector p) {
		this.r1 = new State3DRot(r1);
		this.p = new State3DPos(p);
		this.r2 = new State3DRot(r2);
		this.s = new State3DScl(s);
	}
	
	
	public State3DIntegrated(Vector pos, Quaternion rot, Vector scl) {this(Quaternion.UNIT, scl, rot, pos);}
	public State3DIntegrated(Vector pos, Quaternion rot, double scl) {this(pos, rot, new Vector(scl, scl, scl));}
	public State3DIntegrated(Vector pos, Quaternion rot) {this(pos, rot, null);}
	public State3DIntegrated(Vector pos) {this(pos, new State3DRot().getDefaultRepr(), null);}
	public State3DIntegrated() {this(null, new State3DRot().getDefaultRepr(), null);}
	
	public Quaternion getExtRot() {return r1.getStateRepr();}
	public Vector getPos() {return p.getStateRepr();}
	public Quaternion getRot() {return r2.getStateRepr();}
	public Vector getScl() {return s.getStateRepr();}
	
	public void setPos(Vector pos) {
		this.p.setStateRepr(pos);
		dirty = true;
	}
	
	public void setExtRot(Vector rot) {setExtRot(Quaternion.eulerToQuat(rot));}
	public void setExtRot(Quaternion rot) {
		this.r1.setStateRepr(rot);
		dirty = true;
	}
	
	public void setRot(Vector rot) {setRot(Quaternion.eulerToQuat(rot));}
	public void setRot(Quaternion rot) {
		this.r2.setStateRepr(rot);
		dirty = true;
	}
	public void setScl(double scl) {setScl(new Vector(scl, scl, scl));}
	public void setScl(Vector scl) {
		this.s.setStateRepr(scl);
		dirty = true;
	}
	public void setAll(Vector pos, Quaternion rot, Vector scl) {
		this.p.setStateRepr(pos);
		this.r2.setStateRepr(rot);
		this.s.setStateRepr(scl);
		dirty = true;
	}
	
	public Matrix calcState() {
		Matrix ans = CALC_BASE;
		State3DBase[] state_tmp = {r1, s, r2, p};
		for (State3DBase t: state_tmp) {
			if (t == null) continue;
			ans = State3DBase.stateCompose(ans, t.get());
		}
		return ans;
	}
	
	public Matrix get() {
		if (dirty) {
			state_cache = calcState();
			dirty = false;
		}
		return state_cache;
	}
	
	public State3DSimple simplify() {
		return new State3DSimple(get());
	}
}

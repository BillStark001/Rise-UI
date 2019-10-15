package com.billstark001.riseui.base.states.simple3d;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.ShapeMismatchException;
import com.billstark001.riseui.math.Vector;

public class State3DIntegrated extends State3DSimple{
	
	private State3DPos p;
	private State3DRot r;
	private State3DScl s;
	
	private Matrix state_cache;
	
	private boolean dirty = true;
	
	private static final Matrix CALC_BASE = Matrix.I4;
	
	public State3DIntegrated(State3DIntegrated state) {
		this.p = new State3DPos(state.p);
		this.r = new State3DRot(state.r);
		this.s = new State3DScl(state.s);
	}
	
	public State3DIntegrated(Vector p, Quaternion r, Vector s) {
		this.p = new State3DPos(p);
		this.r = new State3DRot(r);
		this.s = new State3DScl(s);
	}

	public State3DIntegrated(Vector pos, Quaternion rot, double scl) {this(pos, rot, new Vector(scl, scl, scl));}
	//public StateStandard3D(Vector pos, Vector rot, Vector scl) {this(pos, Quaternion.eulerToQuat(rot), scl);}
	public State3DIntegrated(Vector pos, Quaternion rot) {this(pos, rot, null);}
	public State3DIntegrated(Vector pos) {this(pos, new State3DRot().getDefaultRepr(), null);}
	public State3DIntegrated() {this(null, new State3DRot().getDefaultRepr(), null);}
	
	public Vector getPos() {return p.getStateRepr();}
	public Quaternion getRot() {return r.getStateRepr();}
	public Vector getScale() {return s.getStateRepr();}
	
	public void setPos(Vector pos) {
		this.p.setStateRepr(pos);
		dirty = true;
	}
	public void setRot(Vector rot) {setRot(Quaternion.eulerToQuat(rot));}
	public void setRot(Quaternion rot) {
		this.r.setStateRepr(rot);
		dirty = true;
	}
	public void setScl(double scl) {setScl(new Vector(scl, scl, scl));}
	public void setScl(Vector scl) {
		this.s.setStateRepr(scl);
		dirty = true;
	}
	public void setAll(Vector pos, Quaternion rot, Vector scl) {
		this.p.setStateRepr(pos);
		this.r.setStateRepr(rot);
		this.s.setStateRepr(scl);
		dirty = true;
	}
	
	public Matrix calcState() {
		Matrix ans = CALC_BASE;
		State3DSimple[] state_tmp = {s, r, p};
		for (State3DSimple t: state_tmp) {
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

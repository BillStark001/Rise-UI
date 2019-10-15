package com.billstark001.riseui.base.states;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.ShapeMismatchException;
import com.billstark001.riseui.math.Vector;

public class StateStandard3D extends SimpleState{
	
	private StatePos p;
	private StateRot r;
	private StateScl s;
	
	private Matrix state_cache;
	
	private boolean dirty = true;
	
	private static final Matrix CALC_BASE = Matrix.I4;
	
	public StateStandard3D(StateStandard3D state) {
		this.p = new StatePos(state.p);
		this.r = new StateRot(state.r);
		this.s = new StateScl(state.s);
	}
	
	public StateStandard3D(Vector p, Quaternion r, Vector s) {
		this.p = new StatePos(p);
		this.r = new StateRot(r);
		this.s = new StateScl(s);
	}

	public StateStandard3D(Vector pos, Quaternion rot, double scl) {this(pos, rot, new Vector(scl, scl, scl));}
	//public StateStandard3D(Vector pos, Vector rot, Vector scl) {this(pos, Quaternion.eulerToQuat(rot), scl);}
	public StateStandard3D(Vector pos, Quaternion rot) {this(pos, rot, null);}
	public StateStandard3D(Vector pos) {this(pos, new StateRot().getDefaultRepr(), null);}
	public StateStandard3D() {this(null, new StateRot().getDefaultRepr(), null);}
	
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
		SimpleState[] state_tmp = {s, r, p};
		for (SimpleState t: state_tmp) {
			ans = State4.stateCompose(ans, t.getState());
		}
		return ans;
	}
	
	public Matrix getState() {
		if (dirty) {
			state_cache = calcState();
			dirty = false;
		}
		return state_cache;
	}
	
	public SimpleState toSimpleState() {
		return new SimpleState(getState());
	}
}

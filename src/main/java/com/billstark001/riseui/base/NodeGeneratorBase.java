package com.billstark001.riseui.base;

import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.client.GlHelper;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Vector;

public abstract class NodeGeneratorBase extends NodeBase {

	// Extended construction functions occupying a f***in' bunch of space...
	public NodeGeneratorBase(State3DIntegrated state, String name) {super(state, name);}
	public NodeGeneratorBase(State3DSimple state, String name) {super(state, name);}
	public NodeGeneratorBase(Vector pos, Quaternion rot, Vector scl, String name) {super(pos, rot, scl, name);}
	public NodeGeneratorBase(Vector pos, Quaternion rot, Vector scl) {super(pos, rot, scl);}
	public NodeGeneratorBase(Vector pos, Quaternion rot, double scl) {super(pos, rot, scl);}
	public NodeGeneratorBase(Vector pos, Vector rot, Vector scl) {super(pos, rot, scl);}
	public NodeGeneratorBase(Vector pos, Quaternion rot) {super(pos, rot);}
	public NodeGeneratorBase(Vector pos) {super(pos);}
	public NodeGeneratorBase() {}
	public NodeGeneratorBase(Vector pos, Quaternion rot, double scl, String name) {super(pos, rot, scl, name);}
	public NodeGeneratorBase(Vector pos, Vector rot, Vector scl, String name) {super(pos, rot, scl, name);}
	public NodeGeneratorBase(Vector pos, Quaternion rot, String name) {super(pos, rot, name);}
	public NodeGeneratorBase(Vector pos, String name) {super(pos, name);}
	public NodeGeneratorBase(String name) {super(name);}
	
	// Abstract Methods
	public abstract int getVertCount();
	public abstract int getEdgeCount();
	public abstract int getFaceCount();
	
	public abstract Vector getVertPos(int index);
	public abstract Vector getVertNrm(int index);
	public abstract Vector getVertUVM(int index);
	
	public abstract boolean isEdgeLooped(int index);
	public abstract int[] getEdgeIndices(int index);
	public abstract Triad[] getFaceIndices(int index);
	

}

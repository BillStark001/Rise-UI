package com.billstark001.riseui.core.empty;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.states.StateStandard3D;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;

public class EmptyNode extends BaseNode {

	public EmptyNode(Vector pos, Quaternion rot, Vector scl, String name) {
		super(pos, rot, scl, name);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Quaternion rot, Vector scl) {
		super(pos, rot, scl);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Quaternion rot, double scl) {
		super(pos, rot, scl);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Vector rot, Vector scl) {
		super(pos, rot, scl);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Quaternion rot) {
		super(pos, rot);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos) {
		super(pos);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode() {
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Quaternion rot, double scl, String name) {
		super(pos, rot, scl, name);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Vector rot, Vector scl, String name) {
		super(pos, rot, scl, name);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, Quaternion rot, String name) {
		super(pos, rot, name);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(Vector pos, String name) {
		super(pos, name);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(String name) {
		super(name);
		// TODO 自动生成的构造函数存根
	}

	public EmptyNode(StateStandard3D c, String name) {
		super(c, name);
	}
	
	public EmptyNode(StateStandard3D c) {
		super(c, null);
	}

	@Override
	public void renderVert(double ptick) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void renderEdge(double ptick) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void renderFace(double ptick) {
		// TODO 自动生成的方法存根

	}

	@Override
	public int getVertCount() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getEdgeCount() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getFaceCount() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public Vector getVertPos(int index) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Vector getVertNrm(int index) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Vector getVertUVM(int index) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public boolean isEdgeLooped(int index) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int[] getEdgeIndices(int index) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Triad[] getFaceIndices(int index) {
		// TODO 自动生成的方法存根
		return null;
	}

}

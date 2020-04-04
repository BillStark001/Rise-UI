package com.billstark001.riseui.core.empty;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Vector;

public class EmptyNode extends NodeBase {

	public EmptyNode(Vector pos, Quaternion rot, Vector scl, String name) {
		super(pos, rot, scl, name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Quaternion rot, Vector scl) {
		super(pos, rot, scl);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Quaternion rot, double scl) {
		super(pos, rot, scl);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Vector rot, Vector scl) {
		super(pos, rot, scl);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Quaternion rot) {
		super(pos, rot);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos) {
		super(pos);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Quaternion rot, double scl, String name) {
		super(pos, rot, scl, name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Vector rot, Vector scl, String name) {
		super(pos, rot, scl, name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, Quaternion rot, String name) {
		super(pos, rot, name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(Vector pos, String name) {
		super(pos, name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(String name) {
		super(name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public EmptyNode(State3DIntegrated c, String name) {
		super(c, name);
	}
	
	public EmptyNode(State3DIntegrated c) {
		super(c, null);
	}

	@Override
	public int getVertCount() {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public int getEdgeCount() {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public int getFaceCount() {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public Vector getVertPos(int index) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Vector getVertNrm(int index) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Vector getVertUVM(int index) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public boolean isEdgeLooped(int index) {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public int[] getEdgeIndices(int index) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Triad[] getFaceIndices(int index) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public int getEdgeIndicesLength(int index) {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public int getFaceIndicesLength(int index) {
		// TODO �Զ����ɵķ������
		return 0;
	}

}

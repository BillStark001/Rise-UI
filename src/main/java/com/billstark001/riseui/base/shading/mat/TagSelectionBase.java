package com.billstark001.riseui.base.shading.mat;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.fields.Operator;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class TagSelectionBase extends TagBase {
	
	@Override
	public boolean onMat() {return false;}
	@Override
	public boolean onShader() {return false;}
	@Override
	public boolean onData() {return false;}
	@Override
	public Operator getShaderOpr() {return null;}
	@Override
	public Operator getDataOpr() {return null;}
	
	static enum Type {
		VERTEX,
		EDGE,
		FACE;
	}
	
	private final Type type;
	private NodeBase node;
	public Type getType() {return this.type;}
	public NodeBase getNode() {return this.node;}
	
	public TagSelectionBase(int hierarchy, boolean activated, Type type) {
		super(hierarchy, activated);
		this.type = type;
	}
	
	public TagSelectionBase(int hierarchy, boolean activated) {this(hierarchy, activated, Type.FACE);}
	public TagSelectionBase(boolean activated) {this(0, activated, Type.FACE);}
	public TagSelectionBase(int hierarchy) {this(hierarchy, true, Type.FACE);}
	
	public TagSelectionBase(int hierarchy, Type type) {this(hierarchy, true, type);}
	public TagSelectionBase(boolean activated, Type type) {this(0, activated, type);}
	public TagSelectionBase(Type type) {this(0, true, type);}
	
	public TagSelectionBase() {this(0, true, Type.FACE);}
	
	public TagSelectionBase(TagSelectionBase t) {
		this(t.hierarchy, t.activated, t.type);
		this.node = null;
	}

	@Override
	public void update(TickEvent e) {}
	
	@Override
	public boolean appliesOn(int phrase) {
		switch (phrase) {
		case TagBase.TAG_PHRASE_ADDED:
		case TagBase.TAG_PHRASE_REMOVED:
			return true;
		default:
			return false;
		}
	}

	@Override
	public ApplyReturn onAdded(NodeBase node) {
		if (this.node != null) return new TagBase.ApplyReturn(false);
		else {
			this.node = node;
			return new TagBase.ApplyReturn(true);
		}
	}

	@Override
	public ApplyReturn onRemoved(NodeBase node) {
		this.node = null;
		return new TagBase.ApplyReturn(true);
	}
	
	public abstract double getContainRate(int index);
	public boolean contains(int index) {return this.getContainRate(index) >= 0.5;}
	
	
	// All functions below are useless, just to materialize abstract functions.

	@Override
	public ApplyReturn onRenderPre(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderPost(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderVertsPre(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderEdgesPre(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderFacesPre(NodeBase object, double ptick, int inform) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderVert(NodeBase object, int index, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderEdge(NodeBase object, int index, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderFace(NodeBase object, int index, double ptick, boolean inform) {
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public ApplyReturn onRenderVertsPost(NodeBase object) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public ApplyReturn onRenderEdgesPost(NodeBase object) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public ApplyReturn onRenderFacesPost(NodeBase object) {
		// TODO 自动生成的方法存根
		return null;
	}


}

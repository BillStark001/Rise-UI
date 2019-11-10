package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class TagSelectionBase extends TagBase {
	
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
	public ApplicationReturn onAdded(NodeBase node) {
		if (this.node != null) return new TagBase.ApplicationReturn(false);
		else {
			this.node = node;
			return new TagBase.ApplicationReturn(true);
		}
	}

	@Override
	public ApplicationReturn onRemoved(NodeBase node) {
		this.node = null;
		return new TagBase.ApplicationReturn(true);
	}
	
	public abstract double getContainRate(int index);
	public boolean contains(int index) {return this.getContainRate(index) >= 0.5;}
	
	
	// All functions below are useless, just to materialize abstract functions.
	
	@Override
	public ApplicationReturn onGlobalUpdate(NodeBase state) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onLocalUpdate(NodeBase state) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderPre(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderPost(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderVerts(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdges(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderFaces(NodeBase object, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderVert(NodeBase object, int index, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdge(NodeBase object, int index, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplicationReturn onRenderFace(NodeBase object, int index, double ptick) {
		// TODO �Զ����ɵķ������
		return null;
	}

}

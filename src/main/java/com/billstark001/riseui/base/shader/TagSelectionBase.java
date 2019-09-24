package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseTag;
import com.billstark001.riseui.base.BaseTag.ApplicationReturn;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class TagSelectionBase extends BaseTag {
	
	static enum Type {
		VERTEX,
		EDGE,
		FACE;
	}
	
	private final Type type;
	private BaseNode node;
	public Type getType() {return this.type;}
	public BaseNode getNode() {return this.node;}
	
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
		case BaseTag.TAG_PHRASE_ADDED:
		case BaseTag.TAG_PHRASE_REMOVED:
			return true;
		default:
			return false;
		}
	}

	@Override
	public ApplicationReturn onAdded(BaseNode node) {
		if (this.node != null) return new BaseTag.ApplicationReturn(false);
		else {
			this.node = node;
			return new BaseTag.ApplicationReturn(true);
		}
	}

	@Override
	public ApplicationReturn onRemoved(BaseNode node) {
		this.node = null;
		return new BaseTag.ApplicationReturn(true);
	}
	
	public abstract double getContainRate(int index);
	public boolean contains(int index) {return this.getContainRate(index) >= 0.5;}
	
	
	// All functions below are useless, just to materialize abstract functions.
	
	@Override
	public ApplicationReturn onGlobalUpdate(BaseNode state) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onLocalUpdate(BaseNode state) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderPre(BaseNode object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderPost(BaseNode object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderVerts(BaseNode object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdges(BaseNode object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderFaces(BaseNode object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderVert(BaseNode object, int index, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdge(BaseNode object, int index, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderFace(BaseNode object, int index, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

}

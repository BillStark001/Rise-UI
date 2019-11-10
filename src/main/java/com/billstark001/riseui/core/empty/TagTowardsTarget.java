package com.billstark001.riseui.core.empty;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Vector;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagTowardsTarget extends TagBase {

	private Vector target_pos;
	
	public TagTowardsTarget() {this(Vector.UNIT0_D3);}
	public TagTowardsTarget(Vector target) {
		this.setTarget(target);
	}
	
	public Vector getTarget() {return target_pos;}
	public void setTarget(Vector target_pos) {this.target_pos = target_pos;}
	@Override
	public void update(TickEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public boolean appliesOn(int phrase) {
		// TODO �Զ����ɵķ������
		return false;
	}
	@Override
	public ApplicationReturn onAdded(NodeBase node) {
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public ApplicationReturn onRemoved(NodeBase node) {
		// TODO �Զ����ɵķ������
		return null;
	}
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

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
	public ApplyReturn onAdded(NodeBase node) {
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public ApplyReturn onRemoved(NodeBase node) {
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public ApplyReturn onGlobalUpdate(NodeBase state) {
		// TODO �Զ����ɵķ������
		return null;
	}
	@Override
	public ApplyReturn onLocalUpdate(NodeBase state) {
		// TODO �Զ����ɵķ������
		return null;
	}
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

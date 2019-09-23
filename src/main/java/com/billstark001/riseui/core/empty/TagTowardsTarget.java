package com.billstark001.riseui.core.empty;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseTag;
import com.billstark001.riseui.base.state.SimpleState;
import com.billstark001.riseui.base.state.StateStandard3D;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagTowardsTarget extends BaseTag {

	private Vector target_pos;
	
	public TagTowardsTarget() {this(Vector.UNIT0_D3);}
	public TagTowardsTarget(Vector target) {
		this.setTarget(target);
	}
	
	public Vector getTarget() {return target_pos;}
	public void setTarget(Vector target_pos) {this.target_pos = target_pos;}
	@Override
	public void update(TickEvent e) {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public boolean appliesOn(int phrase) {
		// TODO 自动生成的方法存根
		return false;
	}
	@Override
	public ApplicationReturn onAdded(BaseNode node) {
		// TODO 自动生成的方法存根
		return null;
	}
	@Override
	public ApplicationReturn onRemoved(BaseNode node) {
		// TODO 自动生成的方法存根
		return null;
	}
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

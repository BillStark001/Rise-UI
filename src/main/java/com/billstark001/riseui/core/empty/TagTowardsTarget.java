package com.billstark001.riseui.core.empty;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseTag;
import com.billstark001.riseui.math.Vector;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
	public void onGlobalUpdate(BaseNode node) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onLocalUpdate(BaseNode node) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onRenderPre(BaseNode object, double ptick) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onRenderPost(BaseNode object, double ptick) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onAdd(BaseNode node) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onRemove(BaseNode node) {
		// TODO �Զ����ɵķ������
		
	}
	
	@Override
	public void update(TickEvent e) {
		// TODO �Զ����ɵķ������
		System.out.println(1);
	}

}

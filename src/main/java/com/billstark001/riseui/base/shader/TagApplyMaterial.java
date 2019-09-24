package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseTag;
import com.billstark001.riseui.client.GlRenderHelper;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterial extends BaseTag {
	
	private static GlRenderHelper renderer = GlRenderHelper.getInstance();
	
	private BaseMaterial material;
	public BaseMaterial getMaterial() {return this.material;}
	public void setMaterial(BaseMaterial mat) {this.material = mat;}

	public TagApplyMaterial(int hierarchy, boolean activated, BaseMaterial material) {
		super(hierarchy, activated);
		this.setMaterial(material);
	}
	
	public TagApplyMaterial(BaseMaterial material) {this(0, true, material);}
	public TagApplyMaterial() {this(0, true, null);}
	
	@Override
	public boolean isActivated() {return super.isActivated() && (this.material == null);}

	public TagApplyMaterial(boolean activated, BaseMaterial material) {super(activated); this.setMaterial(material);}
	public TagApplyMaterial(int hierarchy, BaseMaterial material) {super(hierarchy); this.setMaterial(material);}
	public TagApplyMaterial(boolean activated) {super(activated);}
	public TagApplyMaterial(int hierarchy) {super(hierarchy);}

	@Override
	public void update(TickEvent e) {}

	@Override
	public boolean appliesOn(int phrase) {
		switch (phrase) {
		case BaseTag.TAG_PHRASE_RENDER_PRE:
		case BaseTag.TAG_PHRASE_RENDER_POST:
		case BaseTag.TAG_PHRASE_RENDER_PARTICULAR_VERTEX:
		case BaseTag.TAG_PHRASE_RENDER_PARTICULAR_EDGE:
		case BaseTag.TAG_PHRASE_RENDER_PARTICULAR_FACE:
			return true;
		default:
			return false;
		}
	}

	@Override
	public ApplicationReturn onAdded(BaseNode node) {return null;}
	@Override
	public ApplicationReturn onRemoved(BaseNode node) {return null;}
	@Override
	public ApplicationReturn onGlobalUpdate(BaseNode state) {return null;}
	@Override
	public ApplicationReturn onLocalUpdate(BaseNode state) {return null;}
	
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

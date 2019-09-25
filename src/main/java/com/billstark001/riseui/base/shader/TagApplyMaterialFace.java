package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseTag;
import com.billstark001.riseui.base.shader.TagSelectionBase.Type;
import com.billstark001.riseui.client.GlRenderHelper;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterialFace extends BaseTag {
	
	//private static GlRenderHelper renderer = GlRenderHelper.getInstance();
	
	private MaterialFace material;
	public MaterialFace getMaterial() {return this.material;}
	public void setMaterial(MaterialFace mat) {this.material = mat;}
	
	private TagSelectionBase selection;
	public TagSelectionBase getSelection() {return this.selection;}
	public void setSelection(TagSelectionBase selection) {this.selection = selection;}

	public TagApplyMaterialFace(int hierarchy, boolean activated, MaterialFace material, TagSelectionBase selection) {
		super(hierarchy, activated);
		this.setMaterial(material);
		this.setSelection(selection);
	}
	
	public TagApplyMaterialFace(int hierarchy, boolean activated, MaterialFace material) {this(hierarchy, activated, material, null);}
	public TagApplyMaterialFace(MaterialFace material, TagSelectionBase selection) {this(0, true, material, selection);}
	public TagApplyMaterialFace(MaterialFace material) {this(0, true, material, null);}
	public TagApplyMaterialFace() {this(0, true, null, null);}
	
	@Override
	public boolean isActivated() {return super.isActivated() && (this.material == null);}

	public TagApplyMaterialFace(boolean activated, MaterialFace material) {super(activated); this.setMaterial(material);}
	public TagApplyMaterialFace(int hierarchy, MaterialFace material) {super(hierarchy); this.setMaterial(material);}
	public TagApplyMaterialFace(boolean activated) {super(activated);}
	public TagApplyMaterialFace(int hierarchy) {super(hierarchy);}

	@Override
	public void update(TickEvent e) {}

	@Override
	public boolean appliesOn(int phrase) {
		switch (phrase) {
		case BaseTag.TAG_PHRASE_RENDER_PRE:
		case BaseTag.TAG_PHRASE_RENDER_POST:
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
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdges(BaseNode object, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderFaces(BaseNode object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderVert(BaseNode object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdge(BaseNode object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderFace(BaseNode object, int index, double ptick) {
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.FACE) flag = this.selection.contains(index);
		if (flag) this.material.getAlbedo().bindTexture();
		return null;
	}

}

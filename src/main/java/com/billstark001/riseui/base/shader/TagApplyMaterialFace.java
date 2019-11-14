package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.shader.TagSelectionBase.Type;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterialFace extends TagBase {
	
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
	public boolean isActivated() {return super.isActivated() && (this.material != null);}

	public TagApplyMaterialFace(boolean activated, MaterialFace material) {super(activated); this.setMaterial(material);}
	public TagApplyMaterialFace(int hierarchy, MaterialFace material) {super(hierarchy); this.setMaterial(material);}
	public TagApplyMaterialFace(boolean activated) {super(activated);}
	public TagApplyMaterialFace(int hierarchy) {super(hierarchy);}

	@Override
	public void update(TickEvent e) {}

	@Override
	public boolean appliesOn(int phrase) {
		switch (phrase) {
		case TagBase.TAG_PHRASE_RENDER_PRE:
		case TagBase.TAG_PHRASE_RENDER_POST:
		case TagBase.TAG_PHRASE_RENDER_FACES:
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE:
			return true;
		default:
			return false;
		}
	}

	@Override
	public ApplicationReturn onAdded(NodeBase node) {return null;}
	@Override
	public ApplicationReturn onRemoved(NodeBase node) {return null;}
	@Override
	public ApplicationReturn onGlobalUpdate(NodeBase state) {return null;}
	@Override
	public ApplicationReturn onLocalUpdate(NodeBase state) {return null;}
	
	@Override
	public ApplicationReturn onRenderPre(NodeBase object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderPost(NodeBase object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderVerts(NodeBase object, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdges(NodeBase object, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderFaces(NodeBase object, double ptick) {
		boolean flag = false;
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			Texture2DBase albedo = this.material.getAlbedo();
			if (albedo != null) albedo.bindTexture();
		}
		return null;
	}

	@Override
	public ApplicationReturn onRenderVert(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdge(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderFace(NodeBase object, int index, double ptick) {
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.FACE) flag = this.selection.contains(index);
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			Texture2DBase albedo = this.material.getAlbedo();
			if (albedo != null) albedo.bindTexture();
		}
		return null;
	}

}

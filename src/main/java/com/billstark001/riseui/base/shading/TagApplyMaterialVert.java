package com.billstark001.riseui.base.shading;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.shading.TagSelectionBase.Type;
import com.billstark001.riseui.client.GlHelper;
import com.billstark001.riseui.computation.Vector;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterialVert extends TagBase {
	
	//private static GlRenderHelper renderer = GlRenderHelper.getInstance();
	
	private MaterialVertBase material;
	public MaterialVertBase getMaterial() {return this.material;}
	public void setMaterial(MaterialVertBase mat) {this.material = mat;}
	
	private TagSelectionBase selection;
	public TagSelectionBase getSelection() {return this.selection;}
	public void setSelection(TagSelectionBase selection) {this.selection = selection;}

	public TagApplyMaterialVert(int hierarchy, boolean activated, MaterialVertBase material, TagSelectionBase selection) {
		super(hierarchy, activated);
		this.setMaterial(material);
		this.setSelection(selection);
	}
	
	public TagApplyMaterialVert(int hierarchy, boolean activated, MaterialVertBase material) {this(hierarchy, activated, material, null);}
	public TagApplyMaterialVert(MaterialVertBase material, TagSelectionBase selection) {this(0, true, material, selection);}
	public TagApplyMaterialVert(MaterialVertBase material) {this(0, true, material, null);}
	public TagApplyMaterialVert() {this(0, true, null, null);}
	
	@Override
	public boolean isActivated() {return super.isActivated() && (this.material != null);}

	public TagApplyMaterialVert(boolean activated, MaterialVertBase material) {super(activated); this.setMaterial(material);}
	public TagApplyMaterialVert(int hierarchy, MaterialVertBase material) {super(hierarchy); this.setMaterial(material);}
	public TagApplyMaterialVert(boolean activated) {super(activated);}
	public TagApplyMaterialVert(int hierarchy) {super(hierarchy);}

	@Override
	public void update(TickEvent e) {}

	@Override
	public boolean appliesOn(int phrase) {
		switch (phrase) {
		case TagBase.TAG_PHRASE_RENDER_PRE:
		case TagBase.TAG_PHRASE_RENDER_POST:
		case TagBase.TAG_PHRASE_RENDER_VERTICES:
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_VERTEX:
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
		boolean flag = false;
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			GlHelper.getInstance().setColorAndAlpha(this.material.getColor(Vector.UNIT0_D3));
			GlHelper.getInstance().setPointSize(this.material.getSize(Vector.UNIT0_D3));
		}
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdges(NodeBase object, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderFaces(NodeBase object, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdge(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderVert(NodeBase object, int index, double ptick) {
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.VERTEX) flag = this.selection.contains(index);
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			if (this.material.needsPos()) {
				Vector pos = object.getVertPos(index);
				if (pos != null) {
					GlHelper.getInstance().setColorAndAlpha(this.material.getColor(pos));
					GlHelper.getInstance().setPointSize(this.material.getSize(pos));
				}
			} else {
				GlHelper.getInstance().setColorAndAlpha(this.material.getColor(Vector.UNIT0_D3));
				GlHelper.getInstance().setPointSize(this.material.getSize(Vector.UNIT0_D3));
			}
		}
		return null;
	}

	@Override
	public ApplicationReturn onRenderFace(NodeBase object, int index, double ptick) {
		return null;
	}

}

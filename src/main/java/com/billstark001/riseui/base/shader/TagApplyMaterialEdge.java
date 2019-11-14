package com.billstark001.riseui.base.shader;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.shader.TagSelectionBase.Type;
import com.billstark001.riseui.client.GlHelper;
import com.billstark001.riseui.computation.Vector;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterialEdge extends TagBase {
	
	//private static GlRenderHelper renderer = GlRenderHelper.getInstance();
	
	private MaterialEdgeBase material;
	public MaterialEdgeBase getMaterial() {return this.material;}
	public void setMaterial(MaterialEdgeBase mat) {this.material = mat;}
	
	private TagSelectionBase selection;
	public TagSelectionBase getSelection() {return this.selection;}
	public void setSelection(TagSelectionBase selection) {this.selection = selection;}

	public TagApplyMaterialEdge(int hierarchy, boolean activated, MaterialEdgeBase material, TagSelectionBase selection) {
		super(hierarchy, activated);
		this.setMaterial(material);
		this.setSelection(selection);
	}
	
	public TagApplyMaterialEdge(int hierarchy, boolean activated, MaterialEdgeBase material) {this(hierarchy, activated, material, null);}
	public TagApplyMaterialEdge(MaterialEdgeBase material, TagSelectionBase selection) {this(0, true, material, selection);}
	public TagApplyMaterialEdge(MaterialEdgeBase material) {this(0, true, material, null);}
	public TagApplyMaterialEdge() {this(0, true, null, null);}
	
	@Override
	public boolean isActivated() {return super.isActivated() && (this.material != null);}

	public TagApplyMaterialEdge(boolean activated, MaterialEdgeBase material) {super(activated); this.setMaterial(material);}
	public TagApplyMaterialEdge(int hierarchy, MaterialEdgeBase material) {super(hierarchy); this.setMaterial(material);}
	public TagApplyMaterialEdge(boolean activated) {super(activated);}
	public TagApplyMaterialEdge(int hierarchy) {super(hierarchy);}

	@Override
	public void update(TickEvent e) {}

	@Override
	public boolean appliesOn(int phrase) {
		switch (phrase) {
		case TagBase.TAG_PHRASE_RENDER_PRE:
		case TagBase.TAG_PHRASE_RENDER_POST:
		case TagBase.TAG_PHRASE_RENDER_EDGES:
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_EDGE:
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
		boolean flag = false;
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			Vector[] vt = {Vector.UNIT0_D3};
			GlHelper.getInstance().setColorAndAlpha(this.material.getColor(vt)[0]);
			GlHelper.getInstance().setLineWidth(this.material.getWidth(vt)[0]);
		}
		return null;
	}

	@Override
	public ApplicationReturn onRenderFaces(NodeBase object, double ptick) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ApplicationReturn onRenderVert(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplicationReturn onRenderEdge(NodeBase object, int index, double ptick) {
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.EDGE) flag = this.selection.contains(index);
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			if (this.material.needsPos()) {
				Vector[] pos = object.getEdges(index);
				if (pos != null) {
					GlHelper.getInstance().pushColors(this.material.getColor(pos));
					GlHelper.getInstance().pushWidths(this.material.getWidth(pos));
				}
			} else {
				GlHelper.getInstance().pushColors(this.material.getColor(new Vector[object.getEdgeIndicesLength(index)]));
				GlHelper.getInstance().pushWidths(this.material.getWidth(new Vector[object.getEdgeIndicesLength(index)]));
			}
		}
		return null;
	}

	@Override
	public ApplicationReturn onRenderFace(NodeBase object, int index, double ptick) {
		return null;
	}

}

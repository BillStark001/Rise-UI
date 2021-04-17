package com.billstark001.riseui.base.shading.mat;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.NodeCompilableBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.fields.Operator;
import com.billstark001.riseui.base.shading.mat.TagSelectionBase.Type;
import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.render.GlHelper;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterialEdge extends TagBase {
	
	@Override
	public boolean onMat() {return false;}
	@Override
	public boolean onShader() {return false;}
	@Override
	public boolean onData() {return false;}
	@Override
	public Operator getShaderOpr() {return null;}
	@Override
	public Operator getDataOpr() {return null;}
	
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
		case TagBase.TAG_PHRASE_RENDER_EDGES_PRE:
		case TagBase.TAG_PHRASE_RENDER_EDGES_POST:
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_EDGE:
			return true;
		default:
			return false;
		}
	}

	@Override
	public ApplyReturn onAdded(NodeBase node) {return null;}
	@Override
	public ApplyReturn onRemoved(NodeBase node) {return null;}
	
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
		return null;
	}

	@Override
	public ApplyReturn onRenderEdgesPre(NodeBase object, double ptick) {
		Shader.SHADER_EDGE.applyState();
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
	public ApplyReturn onRenderFacesPre(NodeBase object, double ptick, int inform) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public ApplyReturn onRenderVert(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplyReturn onRenderEdge(NodeBase object_, int index, double ptick) {
		if (!(object_ instanceof NodeCompilableBase)) return new ApplyReturn(false, null, null);
		NodeCompilableBase object = (NodeCompilableBase) object_;
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.EDGE) flag = this.selection.contains(index);
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		int[] colors = null;
		double[] widths = null;
		if (flag) {
			Vector[] pos = null;
			if (this.material.needsPos()) 
				pos = object.getEdges(index);
			if (pos == null)
				pos = new Vector[object.getEdgeIndicesLength(index)];
			colors = this.material.getColor(pos);
			widths = this.material.getWidth(pos);
		}
		return new ApplyReturn(flag, colors, widths);
	}

	@Override
	public ApplyReturn onRenderFace(NodeBase object, int index, double ptick, boolean inform) {
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

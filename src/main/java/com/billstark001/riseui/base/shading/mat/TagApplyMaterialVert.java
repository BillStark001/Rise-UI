package com.billstark001.riseui.base.shading.mat;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.NodeCompilableBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.fields.Operator;
import com.billstark001.riseui.base.shading.mat.TagSelectionBase.Type;
import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.render.GlHelper;

import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TagApplyMaterialVert extends TagBase {
	
	//private static GlRenderHelper renderer = GlRenderHelper.getInstance();
	
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
		case TagBase.TAG_PHRASE_RENDER_VERTICES_PRE:
		case TagBase.TAG_PHRASE_RENDER_VERTICES_POST:
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_VERTEX:
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
		Shader.SHADER_VERT.applyState();
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
	public ApplyReturn onRenderEdgesPre(NodeBase object, double ptick) {
		return null;
	}

	@Override
	public ApplyReturn onRenderFacesPre(NodeBase object, double ptick, int inform) {
		return null;
	}

	@Override
	public ApplyReturn onRenderEdge(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplyReturn onRenderVert(NodeBase object_, int index, double ptick) {
		if (!(object_ instanceof NodeCompilableBase)) return new ApplyReturn(false, 0XFFFFFFFF, 1);
		NodeCompilableBase object = (NodeCompilableBase) object_;
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.VERTEX) flag = this.selection.contains(index);
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		int color = UtilsTex.color(255, 255, 255);
		double size = 1;
		if (flag) {
			Vector pos = Vector.UNIT0_D3;
			if (this.material.needsPos()) 
				pos = object.getVertPos(index);
			color = this.material.getColor(pos);
			size = this.material.getSize(pos);
		}
		return new ApplyReturn(flag, color, size);
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

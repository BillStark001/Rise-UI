package com.billstark001.riseui.base.shading.mat;

import java.util.ArrayList;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.shading.mat.TagSelectionBase.Type;
import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.render.GlHelper;

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
		case TagBase.TAG_PHRASE_RENDER_FACES_PRE:
		case TagBase.TAG_PHRASE_RENDER_FACES_POST:
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE:
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
	public ApplyReturn onGlobalUpdate(NodeBase state) {return null;}
	@Override
	public ApplyReturn onLocalUpdate(NodeBase state) {return null;}
	
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
		return null;
	}

	@Override
	public ApplyReturn onRenderFacesPre(NodeBase object, double ptick, int inform) {
		boolean flag = true;
		// if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		if (flag) {
			if (inform < 0) {
				return new ApplyReturn(true, 2);
			} else if (inform == 0) {
				this.material.SHADER_DIFFUSE.applyState();
				Texture2DBase diffuse = this.material.getDiffuse();
				if (diffuse == null) 
					return new ApplyReturn(false);
				GlHelper.getInstance().applyTexture(diffuse);
			} else if (inform == 1) {
				this.material.SHADER_LIGHT.applyState();
				Texture2DBase light = this.material.getLight();
				if (light == null) 
					return new ApplyReturn(false);
				GlHelper.getInstance().applyTexture(light);
			}
		}
		return new ApplyReturn(true);
	}

	@Override
	public ApplyReturn onRenderVert(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplyReturn onRenderEdge(NodeBase object, int index, double ptick) {
		return null;
	}

	@Override
	public ApplyReturn onRenderFace(NodeBase object, int index, double ptick, boolean inform) {
		boolean flag = false;
		if (this.selection != null && this.selection.getType() == Type.FACE) flag = this.selection.contains(index);
		if (this.selection == null) flag = true;
		if (this.material == null) flag = false;
		return new ApplyReturn(true, flag?1.:0.);
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
		Shader.SHADER_DIFFUSE.applyState();
		return null;
	}

}

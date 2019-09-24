package com.billstark001.riseui.core.polygon;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.ICompilable;
import com.billstark001.riseui.base.IMeshable;
import com.billstark001.riseui.base.NodeCompilableBase;
import com.billstark001.riseui.base.shader.BaseMaterial;
import com.billstark001.riseui.base.state.SimpleState;
import com.billstark001.riseui.base.state.StateStandard3D;
import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.io.ObjFile;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;

public class Polygon extends NodeCompilableBase{
	
	private Triad[][] face_ind;
	private int[][] edge_ind;
	
	private Matrix pos;
	private Matrix uvm;
	private Matrix nrm;
	
	private Matrix pos_r;
	private Matrix uvm_r;
	private Matrix nrm_r;
	private SimpleState render_state;
	
	public Polygon (Matrix pos, Matrix uvm, Matrix nrm, Triad[][] face_indices) {
		super();

		this.face_ind = face_indices;
		this.pos = pos;
		this.pos_r = pos;
		this.uvm = uvm;
		this.uvm_r = uvm;
		this.nrm = nrm;
		this.nrm_r = nrm;
		this.render_state = new StateStandard3D();
		
		this.genEdges();
		this.markRecompile();
	}
	
	public Polygon(Polygon m) {
		this(m.pos, m.uvm, m.nrm, m.face_ind);
	}
	
	public void genEdges() {
		ArrayList<int[]> edges = ObjFile.genGridEdges(this.face_ind);
		int[][] edge_ind = edges.toArray(new int[0][0]);
		this.edge_ind = edge_ind;
	}
	
	public int getVertCount() {return this.pos.getShape().getX();}
	public int getEdgeCount() {return this.edge_ind.length;}
	public int getFaceCount() {return this.face_ind.length;}
	
	@Override
	public boolean setParent(BaseNode obj) {
		this.markRecompile();
		return super.setParent(obj);
	}
	
	public Vector getVertPos(int index) {if (pos != null) return pos_r.getLine(index); else return new Vector(0, 0, 0);}
	public Vector getVertNrm(int index) {if (nrm != null) return nrm_r.getLine(index); else return null;}
	public Vector getVertUVM(int index) {if (uvm != null) return uvm_r.getLine(index); else return new Vector(0, 0, 0);}
	
	public Triad[] getFaceIndices(int index) {
		if (index < 0 || index >= this.face_ind.length) return null;
		else return this.face_ind[index];
	}

	@Override
	public boolean isEdgeLooped(int index) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int[] getEdgeIndices(int index) {
		if (index < 0 || index >= this.edge_ind.length) return null;
		else return this.edge_ind[index];
	}
		
	public void setRenderState(SimpleState state) {
		if (state == null) state = new StateStandard3D();
		this.render_state = state; 
		this.calcRender();
	}
	public SimpleState getRenderState() {return this.render_state;}
	
	public void calcRender() {
		if (this.pos != null)
			this.pos_r = Utils.applyStateMat(this.pos, this.render_state.getState());
		if (this.nrm != null)
			this.nrm_r = Utils.applyStateMat(this.nrm, this.render_state.getState());
		this.markRecompile();
	}
	
	public void rasterize() {
		if (!this.isCompiled()) {
			this.compileList();
		}
		this.pos = this.pos_r;
		this.nrm = this.nrm_r;
		this.render_state = new StateStandard3D();
	}

	
	// Some Preset Polygons
	
}

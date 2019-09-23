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

public class PolygonMesh extends NodeCompilableBase{
	
	private Triad[] face_ind;
	private int[] face_endindex;
	
	private Matrix pos;
	private Matrix uvm;
	private Matrix nrm;
	
	private Matrix pos_r;
	private Matrix uvm_r;
	private Matrix nrm_r;
	private SimpleState special_state;
	
	public PolygonMesh (Matrix pos, Matrix uvm, Matrix nrm, Triad[] face_indices, int[] face_endindex) {
		super();

		this.face_endindex = face_endindex;
		this.face_ind = face_indices;
		this.pos = pos;
		this.uvm = uvm;
		this.uvm_r = uvm;
		this.nrm = nrm;
		this.special_state = new StateStandard3D();
		
	}
	
	public PolygonMesh(PolygonMesh m) {
		this(m.pos, m.uvm, m.nrm, m.face_ind, m.face_endindex);
	}
	
	public int getVertCount() {return this.pos.getShape().getX();}
	public int getEdgeCount() {return 0;}
	public int getFaceCount() {return face_endindex.length;}
	
	@Override
	public boolean setParent(BaseNode obj) {
		this.markRecompile();
		return super.setParent(obj);
	}
	
	private int[] face (int index) {
		int[] ans = {0, face_endindex.length};
		if(index > 0 && index < face_endindex.length) ans[0] = face_endindex[index - 1];
		if(index >= 0 && index < face_endindex.length) ans[1] = face_endindex[index];
		return ans;
	}
	
	public Triad[] getFaceIndices(int index) {
		int[] f = face(index);
		Triad[] ans = new Triad[f[1] - f[0]];
		for (int i = f[0]; i < f[1]; ++i) ans[i - f[0]] = face_ind[i];
		return ans;
	}
	
	public Vector getVertPos(int index) {if (pos != null) return pos_r.getLine(index); else return new Vector(0, 0, 0);}
	public Vector getVertNrm(int index) {if (nrm != null) return nrm_r.getLine(index); else return null;}
	public Vector getVertUVM(int index) {if (uvm != null) return uvm_r.getLine(index); else return new Vector(0, 0, 0);}

	@Override
	public boolean isEdgeLooped(int index) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int[] getEdgeIndices(int index) {
		// TODO 自动生成的方法存根
		return null;
	}
		
	/*
	public PolygonGrid generateGrid() {
		ArrayList<int[]> edges = ObjFile.genGridEdges(this.face_ind, this.face_endindex);
		PolygonGrid g = new PolygonGrid(pos, edges);
		g.setPos(getPos());
		g.setRot(getRot());
		g.setScale(getScale());
		g.rasterize();
		return g;
	}
	*/
	// Some Preset Polygons
	
}

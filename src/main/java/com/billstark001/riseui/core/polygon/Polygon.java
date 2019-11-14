package com.billstark001.riseui.core.polygon;

import java.util.ArrayList;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.NodeCompilableBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.io.ObjFile;

public class Polygon extends NodeCompilableBase{
	
	private Triad[][] face_ind;
	private int[][] edge_ind;
	
	private Matrix pos;
	private Matrix uvm;
	private Matrix nrm;
	
	private Matrix pos_r;
	private Matrix uvm_r;
	private Matrix nrm_r;
	private State3DSimple render_state;
	
	public Polygon (Matrix pos, Matrix uvm, Matrix nrm, Triad[][] face_indices) {
		super();

		this.face_ind = face_indices;
		this.pos = pos;
		this.pos_r = pos;
		this.uvm = uvm;
		this.uvm_r = uvm;
		this.nrm = nrm;
		this.nrm_r = nrm;
		this.render_state = new State3DIntegrated();
		
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
	public boolean setParentRemainGlobalState(NodeBase obj) {
		this.markRecompile();
		return super.setParentRemainGlobalState(obj);
	}
	
	public Vector getVertPos(int index) {if (pos != null) return pos_r.getLine(index); else return new Vector(0, 0, 0);}
	public Vector getVertNrm(int index) {if (nrm != null) return nrm_r.getLine(index); else return null;}
	public Vector getVertUVM(int index) {if (uvm != null) return uvm_r.getLine(index); else return new Vector(0, 0, 0);}
	
	@Override
	public Triad[] getFaceIndices(int index) {
		if (index < 0 || index >= this.face_ind.length) return null;
		else return this.face_ind[index];
	}
	
	@Override
	public int getFaceIndicesLength(int index) {
		if (index < 0 || index >= this.face_ind.length) return 0;
		else return this.face_ind[index].length;
	}

	@Override
	public boolean isEdgeLooped(int index) {
		return false;
	}

	@Override
	public int[] getEdgeIndices(int index) {
		if (index < 0 || index >= this.edge_ind.length) return null;
		else return this.edge_ind[index];
	}
	
	@Override
	public int getEdgeIndicesLength(int index) {
		if (index < 0 || index >= this.edge_ind.length) return 0;
		else return this.edge_ind[index].length;
	}
		
	public void setRenderState(State3DSimple state) {
		if (state == null) state = new State3DIntegrated();
		this.render_state = state; 
		this.calcRender();
	}
	public State3DSimple getRenderState() {return this.render_state;}
	
	public void calcRender() {
		if (this.pos != null)
			this.pos_r = Utils3D.applyStateMat(this.pos, this.render_state.get());
		if (this.nrm != null)
			this.nrm_r = Utils3D.applyStateMat(this.nrm, this.render_state.get());
		this.markRecompile();
	}
	
	public void rasterize() {
		this.calcRender();
		this.pos = this.pos_r;
		this.nrm = this.nrm_r;
		this.render_state = new State3DIntegrated();
		this.markRecompile();
	}

	
	// Some Preset Polygons
	
}

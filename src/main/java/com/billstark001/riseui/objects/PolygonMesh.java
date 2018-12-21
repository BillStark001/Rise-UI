package com.billstark001.riseui.objects;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.material.BaseMaterial;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.resources.ObjFile;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;

public class PolygonMesh extends BaseObject implements IMeshable, ICompilable, IRenderable{
	
	private Triad[] vertices;
	private BaseMaterial[] mindex;
	private int[] findex;
	
	private Matrix vpos;
	private Matrix texuv;
	private Matrix normal;
	
	private Matrix vr;
	private Matrix tr;
	private Matrix nr;
	
	private boolean compiled;
	private int displayList;
	
	public PolygonMesh (Matrix pos, Matrix uv, Matrix normal, Triad[] vertices, int[] endindex, BaseMaterial[] mats) {
		
		this.compiled = false;
		this.displayList = -1;
		
		this.mindex = mats;
		this.findex = endindex;
		this.vertices = vertices;
		this.vpos = pos;
		this.texuv = uv;
		this.tr = uv;
		this.normal = normal;
		this.pos = new Vector(0, 0, 0);
		this.rot = Quaternion.UNIT;
		this.scale = new Vector(1, 1, 1);
		calcRender();
	}
	
	public PolygonMesh(PolygonMesh m) {
		this(m.vpos, m.texuv, m.normal, m.vertices, m.findex, m.mindex);
	}
	
	public int getFaceCount () {return findex.length;}
	
	@Override
	public boolean setParent(BaseObject obj) {
		this.markRecompile();
		return super.setParent(obj);
	}
	
	private int[] face (int index) {
		int[] ans = {0, findex.length};
		if(index > 0 && index < findex.length) ans[0] = findex[index - 1];
		if(index >= 0 && index < findex.length) ans[1] = findex[index];
		return ans;
	}
	
	public Triad[] getFace(int index) {
		int[] f = face(index);
		Triad[] ans = new Triad[f[1] - f[0]];
		for (int i = f[0]; i < f[1]; ++i) ans[i - f[0]] = vertices[i];
		return ans;
	}
	
	public Vector getVertex(int index) {if (vpos != null) return vr.getLine(index); else return new Vector(0, 0, 0);}
	public Vector getNormal(int index) {if (normal != null) return nr.getLine(index); else return null;}
	public Vector getUVMap(int index) {if (texuv != null) return tr.getLine(index); else return new Vector(0, 0, 0);}
	
	public void setPos(Vector v) {super.setPos(v); this.markRecompile();}
	public void setRot(Vector v) {super.setRot(v); this.markRecompile();}
	public void setRot(Quaternion v) {super.setRot(v); this.markRecompile();}
	public void setScale(Vector v) {super.setScale(v); this.markRecompile();}
	public void setScale(double v) {super.setScale(v); this.markRecompile();}
	public void setGlobalPos(Vector v) {super.setGlobalPos(v); this.markRecompile();}
	public void setGlobalRot(Vector v) {super.setGlobalRot(v); this.markRecompile();}
	public void setGlobalRot(Quaternion v) {super.setGlobalRot(v); this.markRecompile();}
	public void setGlobalScale(Vector v) {super.setGlobalScale(v); this.markRecompile();}
	public void setGlobalScale(double v) {super.setGlobalScale(v); this.markRecompile();}
	public void offset(Vector v) {super.offset(v); this.markRecompile();}
	public void rotate(Quaternion q) {super.rotate(q); this.markRecompile();}
	public void rotate(Vector v) {super.rotate(v); this.markRecompile();}
	public void zoom(Vector v) {super.zoom(v); this.markRecompile();}
	public void zoom(double d) {super.zoom(d); this.markRecompile();}
	
	private void offsetMesh(Vector v) {vpos = Utils.offset(vpos, v); this.markRecompile();}
	
	private void zoomMesh(Vector v) {vpos = Utils.zoom(vpos, v); normal = Utils.zoom(normal, v); this.markRecompile();}
	private void zoomMesh(double v) {vpos = Utils.zoom(vpos, v); normal = Utils.zoom(normal, v); this.markRecompile();}
	
	private void rotateMesh(Quaternion q) {vpos = Utils.rotate(vpos, q); normal = Utils.rotate(normal, q); this.markRecompile();}
	private void rotateMesh(Vector q) {vpos = Utils.rotate(vpos, q); normal = Utils.rotate(normal, q); this.markRecompile();}
	private void rotateMesh(Matrix q) {vpos = Utils.rotate(vpos, q); normal = Utils.rotate(normal, q); this.markRecompile();}

	public boolean isCompiled() {return this.compiled;}
	public void markRecompile() {this.compiled = false;}
	
	public void calcRender() {	
		
		vr = Utils.zoom(vpos, scale);
		nr = Utils.zoom(normal, scale);
		
		vr = Utils.rotate(vr, rot);
		nr = Utils.rotate(nr, rot);
		
		vr = Utils.offset(vr, pos);
		
		this.markRecompile();
	}
	
	public void compileList() {
		
		calcRender();
		
		if (this.displayList == -1) this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
        GlRenderHelper.getInstance().renderMesh(this);
        GlStateManager.glEndList();
        
        this.compiled = true;
	}
	
	public int getDisplayList() {return this.displayList;}
	
	public void rasterize() {
		zoomMesh(scale);
		rotateMesh(rot);
		offsetMesh(pos);
		setPos(new Vector(0, 0, 0));
		setRot(Quaternion.UNIT);
		setScale(new Vector(1, 1, 1));
		//updateGlobalInfo();
		calcRender();
	}
	
	public BaseMaterial getMaterial(int index) {
		return mindex[index];
	}

	public void render() {
		GlRenderHelper.getInstance().renderMesh(this);
		//GlRenderHelper.getInstance().renderCompiled(this);
	}
	
	public PolygonGrid generateGrid() {
		ArrayList<int[]> edges = ObjFile.genGridEdges(this.vertices, this.findex);
		PolygonGrid g = new PolygonGrid(vpos, edges);
		g.setPos(getPos());
		g.setRot(getRot());
		g.setScale(getScale());
		g.rasterize();
		return g;
	}
	
	// Some Preset Polygons
	
}

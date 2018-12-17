package com.billstark001.riseui.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

public class PolygonMesh extends BaseObject implements IMeshable, ICompilable, IRenderable{
	
	private String[] mindex;
	private int[] findex;
	
	private Matrix vertex;
	private Matrix uv;
	private Matrix normal;
	
	private Matrix vr;
	private Matrix ur;
	private Matrix nr;
	
	private boolean compiled;
	private int displayList;
	
	public PolygonMesh (Matrix vertex, Matrix uv, Matrix normal, int[] endindex, String[] matindex) {
		
		this.compiled = false;
		this.displayList = -1;
		
		this.mindex = matindex;
		this.findex = endindex;
		this.vertex = vertex;
		this.uv = uv;
		this.ur = uv;
		this.normal = normal;
		this.pos = new Vector(0, 0, 0);
		this.rot = Quaternion.UNIT;
		this.scale = new Vector(1, 1, 1);
		calcRender();
	}
	
	public PolygonMesh(Matrix vertex) {
		this(vertex, null, null, null, null);
	}
	
	public PolygonMesh(PolygonMesh m) {
		this(m.vertex, m.uv, m.normal, m.findex, m.mindex);
	}
	
	public int getFaceCount () {return findex.length;}
	
	private int[] face (int index) {
		int[] ans = {0, findex.length};
		if(index > 0 && index < findex.length) ans[0] = findex[index - 1];
		if(index >= 0 && index < findex.length) ans[1] = findex[index];
		return ans;
	}
	
	public Matrix getFaceVertex (int index) {if (vr != null) return vr.getLines(face(index)[0], face(index)[1]); else return null;}
	
	public Matrix getFaceUVMap(int index) {
		if (ur != null)
			return ur.getLines(face(index)[0], face(index)[1]);
		else {
			int length = face(index)[1] - face(index)[0];
			if (length == 3) {
				double[][] dt = {{0, 0, 0}, {0, 1, 0}, {1, 1, 0}};
				return new Matrix(dt);
			} else if (length == 4) {
				double[][] dt = {{0, 0, 0}, {0, 1, 0}, {1, 1, 0}, {1, 0, 0}};
				return new Matrix(dt);
			} else return new Matrix(Vector.Zeros(3), length);
		}
			
	}

	public Matrix getFaceNormal(int index) {
		if (nr != null)
			return nr.getLines(face(index)[0], face(index)[1]);
		else {
			int length = face(index)[1] - face(index)[0];
			Vector[] vt = new Vector[length];
			Matrix vertex = getFaceVertex(index);
			if (vertex == null) return null;
			vt[0] = (vertex.getLine(length - 1).cross(vertex.getLine(0))).normalize();
			for(int i = 1; i < length; ++i) {
				vt[i] = (vertex.getLine(i - 1).cross(vertex.getLine(i))).normalize();
			}
			return new Matrix(vt);
		}
	}
	
	public void setPos(Vector v) {super.setPos(v); this.markRecompile();}
	public void setRot(Vector v) {super.setRot(v); this.markRecompile();}
	public void setRot(Quaternion v) {super.setRot(v); this.markRecompile();}
	public void setScale(Vector v) {super.setScale(v); this.markRecompile();}
	public void setScale(double v) {super.setScale(v); this.markRecompile();}

	public void offset(Vector v) {vertex = Utils.offset(vertex, v); this.markRecompile();}
	
	public void zoom(Vector v) {vertex = Utils.zoom(vertex, v); normal = Utils.zoom(normal, v); this.markRecompile();}
	public void zoom(double v) {vertex = Utils.zoom(vertex, v); normal = Utils.zoom(normal, v); this.markRecompile();}
	
	public void rotate(Quaternion q) {vertex = Utils.rotate(vertex, q); normal = Utils.rotate(normal, q); this.markRecompile();}
	public void rotate(Vector q) {vertex = Utils.rotate(vertex, q); normal = Utils.rotate(normal, q); this.markRecompile();}
	public void rotate(Matrix q) {vertex = Utils.rotate(vertex, q); normal = Utils.rotate(normal, q); this.markRecompile();}

	public boolean isCompiled() {return this.compiled;}
	public void markRecompile() {this.compiled = false;}
	
	public void calcRender() {		
		vr = Utils.rotate(vr, rot);
		nr = Utils.rotate(normal, rot);
		
		vr = Utils.zoom(vr, scale);
		nr = Utils.zoom(nr, scale);
		
		vr = Utils.offset(vertex, pos);
		
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
		rotate(rot);
		zoom(scale);
		offset(pos);
		this.pos = new Vector(0, 0, 0);
		this.rot = Quaternion.UNIT;
		this.scale = new Vector(1, 1, 1);
		calcRender();
	}
	
	public String getMaterial(int index) {
		return mindex[index];
	}

	public void render() {
		GlRenderHelper.getInstance().renderCompiled(this);
	}
	
	public void generateGrid() {
		PolygonGrid g = new PolygonGrid(vertex, findex, null);
		g.setPos(getPos());
		g.setRot(getRot());
		g.setScale(getScale());
		g.rasterize();
	}
	
	// Some Preset Polygons
	
}

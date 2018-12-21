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

public class PolygonGridOld extends BaseObject implements ICompilable, IRenderable{
	
	private int[] segindex;
	private boolean[] looped;
	
	private Matrix vertex;
	private Matrix vr;
	
	private boolean compiled;
	private int displayList;
	
	public PolygonGridOld (Matrix vertex, int[] endindex, boolean[] looped) {
		
		this.compiled = false;
		this.displayList = -1;
		
		this.segindex = endindex;
		this.looped = looped;
		this.vertex = vertex;
		
		this.pos = new Vector(0, 0, 0);
		this.rot = Quaternion.UNIT;
		this.scale = new Vector(1, 1, 1);
		
		calcRender();
	}
	
	public PolygonGridOld(Matrix v_, int[] vindex_) {
		this(v_, vindex_, null);
	}
	
	public PolygonGridOld(PolygonGridOld m) {
		this(m.vertex, m.segindex, m.looped);
	}
	
	@Override
	public boolean setParent(BaseObject obj) {
		this.markRecompile();
		return super.setParent(obj);
	}

	public int getSegmentCount () {return segindex.length;}
	
	private int[] segment (int index) {
		int[] ans = {0, segindex.length};
		if(index > 0 && index < segindex.length) ans[0] = segindex[index - 1];
		if(index >= 0 && index < segindex.length) ans[1] = segindex[index];
		return ans;
	}
	
	public Matrix getSegmentByIndex (int index) {if (vr != null) return vr.getLines(segment(index)[0], segment(index)[1]); else return null;}
	public boolean getSegmentLooped (int index) {if (looped == null) return true; else return looped[index];}
	public boolean getSwitchStripLoop (int index) {if (looped == null || index < 1 || index >= segindex.length) return false; else return looped[index] == looped[index - 1];}

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
	
	private void offsetGrid(Vector v) {vertex = Utils.offset(vertex, v); this.markRecompile();}
	
	private void zoomGrid(Vector v) {vertex = Utils.zoom(vertex, v); this.markRecompile();}
	private void zoomGrid(double v) {vertex = Utils.zoom(vertex, v); this.markRecompile();}
	
	private void rotateGrid(Quaternion q) {vertex = Utils.rotate(vertex, q); this.markRecompile();}
	private void rotateGrid(Vector q) {vertex = Utils.rotate(vertex, q); this.markRecompile();}
	private void rotateGrid(Matrix q) {vertex = Utils.rotate(vertex, q); this.markRecompile();}

	public boolean isCompiled() {return this.compiled;}
	public void markRecompile() {this.compiled = false;}
	
	public void calcRender() {
		vr = Utils.zoom(vertex, scale);
		vr = Utils.rotate(vr, rot);
		vr = Utils.offset(vr, pos);
		this.markRecompile();
	}
	
	public void compileList() {
		
		calcRender();
		
		if (this.displayList == -1) this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
        //GlRenderHelper.getInstance().renderGrid(this);
        GlStateManager.glEndList();
        
        this.compiled = true;
	}
	
	public int getDisplayList() {return this.displayList;}
	
	public void rasterize() {
		zoomGrid(scale);
		rotateGrid(rot);
		offsetGrid(pos);
		setPos(new Vector(0, 0, 0));
		setRot(Quaternion.UNIT);
		setScale(new Vector(1, 1, 1));
		calcRender();
	}
	
	public void render() {
		GlRenderHelper.getInstance().renderCompiled(this);
	}
	
	// Some Preset Polygons
	
}

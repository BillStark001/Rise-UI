package com.billstark001.riseui.core.polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.base.object.BaseObject;
import com.billstark001.riseui.base.object.ICompilable;
import com.billstark001.riseui.base.object.IGridable;
import com.billstark001.riseui.base.object.IRenderable;
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

public class PolygonGrid extends BaseObject implements IGridable, ICompilable, IRenderable{
	
	private ArrayList<int[]> segments;
	private boolean looped;
	
	private Matrix vpos;
	private Matrix vr;
	
	private boolean compiled;
	private int displayList;
	
	public PolygonGrid (Matrix posv, ArrayList<int[]> segments, boolean looped) {
		
		this.compiled = false;
		this.displayList = -1;
		
		this.segments = segments;
		this.looped = looped;
		this.vpos = posv;
		
		this.pos = new Vector(0, 0, 0);
		this.rot = Quaternion.UNIT;
		this.scale = new Vector(1, 1, 1);
		
		calcRender();
	}
	
	public PolygonGrid(Matrix posv, ArrayList<int[]> segments) {
		this(posv, segments, false);
	}
	
	public PolygonGrid(PolygonGrid m) {
		this(m.vpos, m.segments, m.looped);
	}
	
	@Override
	public boolean setParent(BaseObject obj) {
		this.markRecompile();
		return super.setParent(obj);
	}

	public int getSegmentCount () {return segments.size();}
	public int[] getSegment (int index) {
		return segments.get(index);
	}
	public Vector getVertex(int index) {if (vpos != null) return vr.getLine(index); else return new Vector(0, 0, 0);}
	
	public boolean getLooped () {return looped;}
	public void setLooped(boolean looped) {this.looped = looped;}

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
	
	private void offsetGrid(Vector v) {vpos = Utils.offset(vpos, v); this.markRecompile();}
	
	private void zoomGrid(Vector v) {vpos = Utils.zoom(vpos, v); this.markRecompile();}
	private void zoomGrid(double v) {vpos = Utils.zoom(vpos, v); this.markRecompile();}
	
	private void rotateGrid(Quaternion q) {vpos = Utils.rotate(vpos, q); this.markRecompile();}
	private void rotateGrid(Vector q) {vpos = Utils.rotate(vpos, q); this.markRecompile();}
	private void rotateGrid(Matrix q) {vpos = Utils.rotate(vpos, q); this.markRecompile();}

	public boolean isCompiled() {return this.compiled;}
	public void markRecompile() {this.compiled = false;}
	
	public void calcRender() {
		vr = Utils.zoom(vpos, scale);
		vr = Utils.rotate(vr, rot);
		vr = Utils.offset(vr, pos);
		this.markRecompile();
	}
	
	public void compileList() {
		
		calcRender();
		
		if (this.displayList == -1) this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
        GlRenderHelper.getInstance().renderGrid(this);
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

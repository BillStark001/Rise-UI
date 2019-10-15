package com.billstark001.riseui.base;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.base.states.StateBase;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.math.Matrix;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;

public abstract class NodeCompilableBase extends NodeBase {

	private boolean compiled;
	private int displayList;
	
	public NodeCompilableBase () {
		super();
		this.compiled = false;
		this.displayList = -1;
	}
	
	@Override
	public void setLocalState(StateBase<Matrix> state) {
		this.markRecompile();
		super.setLocalState(state);
	}
	
	public boolean isCompiled() {return this.compiled;}
	public void markRecompile() {this.compiled = false;}
	
	public void checkAndCompile() {if (!this.isCompiled()) this.compileList();}
	public void compileList() {
		if (this.displayList == -1) this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, GL11.GL_COMPILE);
        this.render(0);
        GlStateManager.glEndList();
        this.compiled = true;
	}
	
	public int getDisplayList() {return this.displayList;}
	public int clearDisplayList() {
		int ans = this.displayList;
		this.displayList = -1;
		this.compiled = false;
		return ans;
	}
	
	@Override
	public void render(double ptick) {
		if (!this.isCompiled()) {
			super.render(ptick);
		} else {
			GlStateManager.callList(this.getDisplayList());
		}
	}
	
}

package com.billstark001.riseui.base.shading;

import com.billstark001.riseui.computation.Pair;
import com.billstark001.riseui.computation.UtilsTex;

import net.minecraft.client.renderer.GlStateManager;

public abstract class Texture2DBase {

	public static final Texture2DFromRes INEXISTENT = new Texture2DFromRes("riseui:inexistent_texture");
	public static final Texture2DFromRes MISSING = new Texture2DFromRes("riseui:missing_texture");
	
	public static final Pair DEFAULT_SOLUTION = Pair.PAIR_16_16;
	public static final int DEFAULT_COLOR = UtilsTex.color(1., 0, 1, 1);
	// Init
	
	public Texture2DBase(int h, int w, boolean blur, boolean mipmap, boolean clamp) {this(new Pair(h, w), blur, mipmap, clamp);}
	public Texture2DBase(Pair solution, boolean blur, boolean mipmap, boolean clamp) {
		this.setSolution(solution);
		this.blur = this.blurLast = blur;
		this.clamp = this.clampLast = clamp;
		this.mipmap = this.mipmapLast = mipmap;
		this.mipmap_level = 1;
	}
	
	public Texture2DBase(boolean blur, boolean mipmap, boolean clamp) {this(DEFAULT_SOLUTION, blur, mipmap, clamp);}
	public Texture2DBase(boolean blur, boolean clamp) {this(DEFAULT_SOLUTION, blur, false, clamp);}
	
	public Texture2DBase(Pair solution) {this(solution, false, false, false);}
	public Texture2DBase(int h, int w) {this(h, w, false, false, false);}
	public Texture2DBase(int l) {this(l, l, false, false, false);}
	public Texture2DBase() {this(DEFAULT_SOLUTION);}
	
	// Solution and Time
	
	private double time;
	public void setTime(double time) {this.time = time;}
	public double getTime() {return this.time;}
	
	private Pair solution;
	public void setSolution(Pair s) {this.solution = s; resetTexId();}
	public void setSolution(int h, int w) {this.solution = new Pair(h, w); resetTexId();}
	public Pair getSolution() {return this.solution;}
	public int getWidth() {return this.solution.getY();}
	public int getHeight() {return this.solution.getX();}
	
	// TextureID Management
	
	private int glTextureId = -1;
	public boolean hasTexId() {return this.glTextureId >= 0;}
	public void resetTexId() {
		if (this.glTextureId != -1)
			GlStateManager.deleteTexture(this.glTextureId);
		this.glTextureId = -1;
	}
	public int getTexId() {return this.glTextureId;}
	public int allocNewTexId() {
		this.glTextureId = UtilsTex.getGlTextureId();
		return this.glTextureId;
	}
	
	// Blur, Clamp and Mipmap Management
	
	protected boolean blur, clamp, mipmap;
	protected boolean blurLast, clampLast, mipmapLast;
	public boolean isBlurred() {return blur;}
	public boolean isClamped() {return clamp;}
	public boolean isMipmap() {return mipmap;}
	
	public void setClampDirect() {this.setClampDirect(this.isClamped());}
	public void setClampDirect(boolean clamp) {
		this.clamp = clamp;
		UtilsTex.setTextureClamped(clamp);
	}

	public void setClamp(boolean clamp) {
		this.clampLast = this.clamp;
		this.setClampDirect(clamp);
	}

	public void restoreLastClamp() {
		this.setClampDirect(this.clampLast);
	}
	
	public void setBlurDirect(boolean blur) {this.setBlurMipmapDirect(blur, this.isMipmap());}
	public void setMipmapDirect(boolean mipmap) {this.setBlurMipmapDirect(this.isBlurred(), mipmap);}
	public void setBlurMipmapDirect() {this.setBlurMipmapDirect(this.isBlurred(), this.isMipmap());}
	public void setBlurMipmapDirect(boolean blur, boolean mipmap) {
		this.blur = blur;
		this.mipmap = mipmap;
		UtilsTex.setTextureBlurMipmap(blur, mipmap);
	}

	public void setBlur(boolean blur) {this.setBlurMipmap(blur, this.isMipmap());}
	public void setMipmap(boolean mipmap) {this.setBlurMipmap(this.isBlurred(), mipmap);}
	public void setBlurMipmap(boolean blur, boolean mipmap) {
		this.blurLast = this.blur;
		this.mipmapLast = this.mipmap;
		this.setBlurMipmapDirect(blur, mipmap);
	}

	public void restoreLastBlurMipmap() {
		this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
	}
	
	protected int mipmap_level;
	public int getMipmapLevelRaw() {return mipmap_level;}
	public int getMipmapLevel() {return this.isMipmap() ? this.getMipmapLevelRaw() : 0;}
	public void setMipmapLevel(int mipmap_level) {this.mipmap_level = mipmap_level;}
	
	// Render
	
	public abstract boolean getTextureARGB(int y_start, int y_length, int[] cache, int offset);
	
	public boolean render() {
		if (!this.hasTexId()) this.allocNewTexId();
		UtilsTex.bindTexture(this.getTexId());
		UtilsTex.allocateTecture2D(this);
		return UtilsTex.uploadTexture2D(this);
	}
}

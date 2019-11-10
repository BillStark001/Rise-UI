package com.billstark001.riseui.base.shader;

import java.awt.image.BufferedImage;

import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Pair;

import net.minecraft.client.renderer.texture.TextureUtil;

public abstract class Texture2DBase {

	public static final Texture2DFromRes INEXISTENT = new Texture2DFromRes("riseui:inexistent_texture");
	public static final Texture2DFromRes MISSING = new Texture2DFromRes("riseui:missing_texture");
	
	private Pair solution;
	public void setSolution(Pair s) {this.solution = s; clearRender();}
	public void setSolution(int h, int w) {this.solution = new Pair(h, w); clearRender();}
	public Pair getSolution() {return this.solution;}
	
	private int rendered = -1;
	public boolean isRendered() {return this.rendered >= 0;}
	public void clearRender() {
		this.rendered = -1;
	}
	public int getRenderedId() {
		if (!this.isRendered()) this.rendered = TextureUtil.glGenTextures();
		return this.rendered;
	}
	
	private boolean blur, clamp;
	public boolean isBlur() {return blur;}
	public void setBlur(boolean blur) {this.blur = blur;}
	public boolean isClamp() {return clamp;}
	public void setClamp(boolean clamp) {this.clamp = clamp;}
	
	public Texture2DBase(Pair solution) {this.setSolution(solution);}
	public Texture2DBase(int h, int w) {this.setSolution(new Pair(h, w));}
	public Texture2DBase(int l) {this(l, l);}
	public Texture2DBase() {this(16, 16);}
	
	public abstract Matrix render(double time);
	public Matrix render() {return render(0);}
	
	public int checkAndRender() {
		if (!this.isRendered()) {
			BufferedImage bufferedimage = null;
			// TODO bufferedimage = TextureUtil.readBufferedImage(iresource.getInputStream());
            // TODO TextureUtil.uploadTextureImageAllocate(this.getRenderedId(), bufferedimage, this.isBlur(), this.isClamp());
		}
		return this.rendered;
	}
	
	public void bindTexture() {
		GlRenderHelper.getInstance().bindTexture(this.checkAndRender());
	}
}

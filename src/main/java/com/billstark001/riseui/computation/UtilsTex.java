package com.billstark001.riseui.computation;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.billstark001.riseui.base.shader.Texture2DBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;

public class UtilsTex {

	public static final int COLOR_CONSTANT = 255;
	public static final double MIN_ALPHA = 1;
	private static final int TEX2D_BUFFER_SIZE = 0x400000;
	private static final IntBuffer TEX_BUFFER = GLAllocation.createDirectIntBuffer(TEX2D_BUFFER_SIZE);
	
	private static void storeToBuffer(int[] src, int length) {storeToBuffer(src, 0, length);}
	private static void storeToBuffer(int[] src, int offset, int length) {
		int[] aint = src;

		if (Minecraft.getMinecraft().gameSettings.anaglyph) {
			aint = TextureUtil.updateAnaglyph(src);
		}

		TEX_BUFFER.clear();
		TEX_BUFFER.put(aint, offset, length);
		TEX_BUFFER.position(0).limit(length);
	}
	
	public static int color(double a) {return color(1, 1, 1, a);}
	public static int color(double r, double g, double b) {return color(r, g, b, MIN_ALPHA);}
	public static int color(double r, double g, double b, double a) {
		a = Math.max(0, Math.min(a, 1));
		r = Math.max(0, Math.min(r, 1));
		g = Math.max(0, Math.min(g, 1));
		b = Math.max(0, Math.min(b, 1));
		int ai, ri, gi, bi;
		ai = (int) (a * COLOR_CONSTANT);
		ri = (int) (r * COLOR_CONSTANT);
		gi = (int) (g * COLOR_CONSTANT);
		bi = (int) (b * COLOR_CONSTANT);
		return (ai << 24) + (ri << 16) + (gi << 8) + bi;
	}

	public static int color(float a) {return color(1, 1, 1, a);}
	public static int color(float r, float g, float b) {return color(r, g, b, MIN_ALPHA);}
	public static int color(float r, float g, float b, float a) {
		a = Math.max(0, Math.min(a, 1));
		r = Math.max(0, Math.min(r, 1));
		g = Math.max(0, Math.min(g, 1));
		b = Math.max(0, Math.min(b, 1));
		int ai, ri, gi, bi;
		ai = (int) (a * COLOR_CONSTANT);
		ri = (int) (r * COLOR_CONSTANT);
		gi = (int) (g * COLOR_CONSTANT);
		bi = (int) (b * COLOR_CONSTANT);
		return (ai << 24) + (ri << 16) + (gi << 8) + bi;
	}
	
	public static int color(int a) {return color(1, 1, 1, a);}
	public static int color(int r, int g, int b) {return color(r, g, b, (int) (MIN_ALPHA * COLOR_CONSTANT));}
	public static int color(int r, int g, int b, int a) {
		a = Math.max(0, Math.min(a, COLOR_CONSTANT));
		r = Math.max(0, Math.min(r, COLOR_CONSTANT));
		g = Math.max(0, Math.min(g, COLOR_CONSTANT));
		b = Math.max(0, Math.min(b, COLOR_CONSTANT));
		return (a << 24) + (r << 16) + (g << 8) + b;
	}

	public static int[] colorToRGBA(int c) {
		int a, r, g, b;
		a = c >> 24 & 255;
		r = c >> 16 & 255;
		g = c >> 8 & 255;
		b = c & 255;
		int[] ans = {r, g, b, a};
		return ans;
	}
	
	public static int[] colorToARGB(int c) {
		int a, r, g, b;
		a = c >> 24 & 255;
		r = c >> 16 & 255;
		g = c >> 8 & 255;
		b = c & 255;
		int[] ans = {a, r, g, b};
		return ans;
	}
	
	public static void setTextureClamped(boolean clamp) {
		if (clamp) {
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}
	}

	public static void setTextureBlurred(boolean blur) {
		setTextureBlurMipmap(blur, false);
	}

	public static void setTextureBlurMipmap(boolean blur, boolean mipmap) {
		if (blur) {
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, mipmap ? GL11.GL_LINEAR_MIPMAP_LINEAR : GL11.GL_LINEAR);
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		} else {
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, mipmap ? GL11.GL_NEAREST_MIPMAP_LINEAR : GL11.GL_NEAREST);
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}
	}

	public static int getGlTextureId() {
		return GlStateManager.generateTexture();
	}

	public static int bindTexture(int tex_id) {
		// TODO It may cause bugs while an object's compiled !!!
		if (tex_id >= 0)
			GlStateManager.bindTexture(tex_id);
		return tex_id;
	}

	public static boolean uploadTexture2D(Texture2DBase tex) {return uploadTexture2D(tex, 0, 0);}
	public static boolean uploadTexture2D(Texture2DBase tex, int x_offset, int y_offset) {
		boolean ans = true;
		int width = tex.getWidth();
		int height = tex.getHeight();
		int cache_size = TEX2D_BUFFER_SIZE / width;
		int[] cache = new int[cache_size * width];
		tex.setBlurMipmapDirect();
		tex.setClampDirect();

		for (int l = 0; l < width * height; l += width * cache_size) {
			int y_pointer = l / width;
			int y_length = Math.min(cache_size, height - y_pointer);
			int buffer_index = width * y_length;
			ans = ans && tex.getTextureARGB(y_pointer, y_length, cache, 0);
			storeToBuffer(cache, buffer_index);
			IntBuffer buffer = TEX_BUFFER;
			GlStateManager.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x_offset, y_offset + y_pointer, width, y_length,
					GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
		}
		return ans;
	}
}

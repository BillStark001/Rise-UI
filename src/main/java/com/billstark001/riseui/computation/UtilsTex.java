package com.billstark001.riseui.computation;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;

import com.billstark001.riseui.base.shading.Texture2DBase;

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
		int[] src_t = src;

		if (Minecraft.getMinecraft().gameSettings.anaglyph) {
			src_t = TextureUtil.updateAnaglyph(src);
		}

		TEX_BUFFER.clear();
		TEX_BUFFER.put(src_t, offset, length);
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
	
	// Texture related
	
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
			GlStateManager.enableTexture2D();
			//GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex_id);
			GlStateManager.bindTexture(tex_id);
		return tex_id;
	}
	
	public static void allocateTecture2D(Texture2DBase tex) {allocateTexture2D(tex.getTexId(), tex.getMipmapLevel(), tex.getWidth(), tex.getHeight());}
	public static void allocateTexture2D(int tex_id, int mipmap_level, int width, int height) {
		if (mipmap_level >= 0) {
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, mipmap_level);
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, 0);
			GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, mipmap_level);
			GlStateManager.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
		}
		for (int i = 0; i <= mipmap_level; ++i) {
			GlStateManager.glTexImage2D(GL11.GL_TEXTURE_2D, i, GL11.GL_RGBA, width >> i, height >> i, 0, GL12.GL_BGRA,
					GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer) null);
		}
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
		GlStateManager.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);

		for (int l = 0; l < width * height; l += width * cache_size) {
			int y_pointer = l / width;
			int y_length = Math.min(cache_size, height - y_pointer);
			int buffer_size = width * y_length;
			ans = ans && tex.getTextureARGB(y_pointer, y_length, cache, 0);
			storeToBuffer(cache, 0, buffer_size);
			GlStateManager.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x_offset, y_offset + y_pointer, width, y_length,
					GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, TEX_BUFFER);
		}
		return ans;
	}
	
	// Interpolation related
	
	/**
	 * @see com.billstark001.riseui.computation.UtilsTex#spline3(Vector, Vector, boolean)
	 */
	public static Vector[] spline3(Vector x, Vector y) {return spline3(x, y, false);}
	/**
	 * S(x) = a + b * x + c * x^2 + d * x^3
	 * @see https://www.cnblogs.com/xpvincent/archive/2013/01/26/2878092.html
	 * @param x Vector of dimension N.
	 * @param y Vector of dimension N.
	 * @param natural_edge true if use natural edge interpolation(2nd derivatives of the first and the last point is 0), false if use 0-clamped interpolation(1st derivatives of the first and the last point is 0). *false as default.
	 * @return [a, b, c, d] 4 Vectors of dimension N of spline results. * null if the dimension of X != dimension of Y.
	 */
	public static Vector[] spline3(Vector x, Vector y, boolean natural_edge) {
		if (x.getDimension() != y.getDimension()) {
			try {
				throw new LengthMismatchException(x, y);
			} catch (LengthMismatchException e) {
				e.printStackTrace();
				return null;
			}
		}
		int n = x.getDimension();
		// Solve h
		Vector h = x.get(1, n).subtract(x.get(0, -1)); // Vector(n-1):[h0, h[n-2]]
		// Solve m
		Vector h_ = (h.insert(n-1, 0).add(h.insert(0, 0))).mult(2); // Vector(n)
		double[] y__ = new double[n-1];
		for (int i = 0; i < n - 1; ++i)
			y__[i]= (y.get(i+1) - y.get(i)) / h.get(i);
		double[] y_ = new double[n];
		for (int i = 1; i < n - 1; ++i) 
			y_[i] = y__[i] - y__[i-1];
		Vector a_, b_, c_;
		if (natural_edge) {
			a_ = h.get(0, n-2).insert(n-2, 0);
			b_ = new Vector(1, 1).insert(1, h_.get(1, n-1));
			c_ = h.get(1, n-1).insert(0, 0);
		} else {
			y_[0] = y__[0];
			y_[n-1] = -y__[n-1];
			a_ = c_ = h;
			b_ = h_;
		}
		Vector m = UtilsLinalg.solveTridiagnoal(a_, b_, c_, new Vector(y_));
		// Solve a, b, c, d
		Vector a, b, c, d, dy, dm;
		dy = (y.get(1, n).insert(n-1, y.get(n-1))).subtract(y);
		dm = (m.get(1, n).insert(n-1, m.get(n-1))).subtract(m);
		a = y;
		b = dy.mult(h.reciprocal()).subtract(h.mult(0.5).mult(m)).subtract(h.mult(1/6).mult(dm));
		c = m.mult(0.5);
		d = dm.mult((h.mult(6)).reciprocal());
		Vector[] ans = {a, b, c, d};
		return ans;
	}
	
	public static int fuseColor(int c1, int c2, double rate) {return fuseColor(c1, c2, 1, 1, rate);}
	public static int fuseColor(int c1, int c2, double bright1, double bright2, double rate) {
		int[] cd1 = colorToRGBA(c1);
		int[] cd2 = colorToRGBA(c2);
		int[] cd3 = new int[4];
		for (int i = 0; i < 4; ++i) {
			double cd3i = cd1[i] * bright1 * (1-rate) + cd2[i] * bright2 * rate;
			cd3[i] = Math.max(0, Math.min((int) cd3i, 255));
		}
		return color(cd3[0], cd3[1], cd3[2], cd3[3]);
	}
	
}

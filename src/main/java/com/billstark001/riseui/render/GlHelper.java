package com.billstark001.riseui.render;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.shading.mat.Texture2DBase;
import com.billstark001.riseui.base.shading.mat.Texture2DFromRes;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.UtilsInteract;
import com.billstark001.riseui.computation.UtilsLinalg;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.empty.Light;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class GlHelper {

	private final Tessellator T;
	private final BufferBuilder R;
	private final TextureManager M;

	public static final int DEFAULT_COLOR = 0xFFFFFFFF;
	public static final double DEFAULT_WIDTH = 1;
	public static final double DEFAULT_SIZE = 2;

	private boolean render_debug = false;
	public void setDebugState(boolean s) {render_debug = s;}
	public boolean isDebugging() {return render_debug;}

	private int r, g, b, a;
	private Queue<Integer> color_cache;
	private Queue<Double> width_cache;
	private Queue<Double> size_cache;

	private final static GlHelper INSTANCE = new GlHelper(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), Minecraft.getMinecraft().renderEngine);

	private GlHelper(Tessellator T, BufferBuilder R, TextureManager M) {
		this.T = T;
		this.R = R;
		this.M = M;
		resetVertRender();
	}

	public static GlHelper getInstance() {return INSTANCE;}

	// Basic Functions

	public void startDrawing(int type, VertexFormat format) {R.begin(type, format);}
	public void startDrawingFace() {R.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);}
	public void startDrawingVert() {R.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);}
	public void startDrawingEdge(boolean isLooped) {
		if (isLooped) R.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
		else R.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
	}
	public void endDrawing() {T.draw();}

	public void addVertex(Vector pos, Vector nrm, Vector uv) {
		R.pos(pos.get(0), pos.get(1), pos.get(2)).normal((float) nrm.get(0), (float) nrm.get(1), (float) nrm.get(2))
		.tex(uv.get(0), uv.get(1)).endVertex();
	}

	public void addVertex(Vector pos, Vector uv) {
		R.pos(pos.get(0), pos.get(1), pos.get(2)).tex(uv.get(0), uv.get(1)).endVertex();
	}

	public void addVertex(Vector pos) {
		int[] colors = popColorRGBA();
		double width = popWidth();
		double size = popSize();
		if (width >= 0) setLineWidth(width);
		if (size >= 0) setPointSize(size);
		R.pos(pos.get(0), pos.get(1), pos.get(2)).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
	}
	public void addVertex(Vector pos, int color) {
		int[] colors = UtilsTex.colorToRGBA(color);
		double width = popWidth();
		double size = popSize();
		if (width >= 0) setLineWidth(width);
		if (size >= 0) setPointSize(size);
		R.pos(pos.get(0), pos.get(1), pos.get(2)).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
	}

	// Color and Width Assignment
	public void setColor(int c) {r = c >> 16 & 255; g = c >> 8 & 255; b = c & 255;}	
	public void setColor(int r, int g, int b) {this.r = r; this.g = g; this.b = b;}
	public void setColor(double r, double g, double b) {setColor(UtilsTex.color(r, g, b));}
	public void setColor(float r, float g, float b) {setColor(UtilsTex.color(r, g, b));}

	public void setAlpha(int a) {this.a = a;}
	public void setAlpha(double a) {a = (int) (a * 255);}
	public void setAlpha(float a) {a = (int) (a * 255);}

	public void setColorAndAlpha(double r, double g, double b, double a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColorAndAlpha(float r, float g, float b, float a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColorAndAlpha(int r, int g, int b, int a) {this.r = r; this.g = g; this.b = b; this.a = a;}

	public void setColorAndAlpha(int c) {a = c >> 24 & 255; r = c >> 16 & 255; g = c >> 8 & 255; b = c & 255;}	
	public void setColorAndAlpha(int c, int a) {setColor(c & 0xFFFFFF); setAlpha(a);}
	public void setColorAndAlpha(int c, double a) {setColor(c & 0xFFFFFF); setAlpha(a);}
	public void setColorAndAlpha(int c, float a) {setColor(c & 0xFFFFFF); setAlpha(a);}

	public void setLineWidth(double width) {GL11.glLineWidth((float) width);}
	public void setPointSize(double size) {GL11.glPointSize((float) size);}
	public void setLineWidth(float width) {GL11.glLineWidth(width);}
	public void setPointSize(float size) {GL11.glPointSize(size);}

	public void pushColors(int...colors) {
		for (int color: colors)
			color_cache.add(color);
	}

	public int[] popColorRGBA() {
		int[] ans = {r, g, b, a};
		if (!color_cache.isEmpty()) ans = UtilsTex.colorToRGBA(color_cache.poll());
		return ans;
	}

	public int[] popColorARGB() {
		int[] ans = {a, r, g, b};
		Integer ans_ = color_cache.poll();
		if (ans_ != null) ans = UtilsTex.colorToARGB(ans_);
		return ans;
	}

	public void pushWidths(double[] widths) {
		for (double width: widths)
			width_cache.add(width);
	}

	public double popWidth() {
		double ans = -1;
		if (!width_cache.isEmpty()) ans = width_cache.poll();
		return ans;
	}

	public void pushSizes(double...sizes) {
		for (double size: sizes)
			size_cache.add(size);
	}

	public double popSize() {
		double ans = -1;
		if (!size_cache.isEmpty()) ans = size_cache.poll();
		return ans;
	}

	public void resetVertRender() {
		color_cache = new LinkedList<Integer>();
		width_cache = new LinkedList<Double>();
		size_cache = new LinkedList<Double>();
		setColorAndAlpha(DEFAULT_COLOR);
		setLineWidth(DEFAULT_WIDTH);
		setPointSize(DEFAULT_SIZE);
	}

	// State Management
	
	private static boolean blend_enabled = false;
	public void disableBlend() {GlStateManager.disableBlend(); blend_enabled = false;}
	public void blendFunc(int src, int dst) {
		blend_enabled = true;
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(src, dst);
	}
	public void blendFuncAlpha() {blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);}
	public void blendFuncOne() {blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ZERO);}

	public void clearAccumCache() {
		GL11.glClear(GL11.GL_ACCUM);
	}

	public void disableDepth() {
		GlStateManager.disableDepth();
	}

	public void enableDepth() {
		GlStateManager.enableDepth();
	}
	
	// List Related
	/**
	 * Generates the specified number of display lists and returns the first index.
	 * @param count
	 * @return the first index
	 */
	public int genDispLists(int count) {
		return GLAllocation.generateDisplayLists(Math.max(count, 0));
	}
	
	/**
	 * Generates a display lists and returns its index.
	 * @return index
	 */
	public int genDispList() {
		return GLAllocation.generateDisplayLists(1);
	}
	
	/**
	 * Marks the start of recording a list.
	 * @param index
	 * @param mode
	 */
	public void startList(int index, int mode) {
        GlStateManager.glNewList(index, mode);
	}
	
	/**
	 * Marks the start of recording a list.
	 * @param index
	 * @param mode
	 */
	public void startCompileList(int index) {
        GlStateManager.glNewList(index, GL11.GL_COMPILE);
	}
	
	/**
	 * Marks the end of recording a list(if started).
	 * @param index
	 * @param mode
	 */
	public void endList() {
		GlStateManager.glEndList();
	}

	// Render

	@Deprecated
	public void renderFace(Matrix vertpos, Matrix normal, Matrix uvmap) {
		Vector[] vpos = vertpos.toVecArray();
		Vector[] vuv = uvmap.toVecArray();
		startDrawingFace();
		for (int i = 0; i < vpos.length; ++i) addVertex(vpos[i], vuv[i]);
		endDrawing();
		startDrawingFace();
		for (int i = vpos.length - 1; i > -1 ; --i) addVertex(vpos[i], vuv[i]);
		endDrawing();
	}
	
	public boolean needReverseNormal(Matrix state) {
		return UtilsLinalg.solveDeterminant(state) <= 0;
	}

	public void renderObjectLocal(NodeBase obj, double ptick, boolean reverse_normal) {
		if (obj == null) return;
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.multMatrix(obj.getLocalState().get().storeBufferF());
		reverse_normal = reverse_normal ^ needReverseNormal(obj.getLocalState().get());
		for (NodeBase o: obj.getChildren()) renderObjectLocal(o, ptick, reverse_normal);
		obj.render(ptick, reverse_normal);
		GlStateManager.popMatrix();
	}
	
	public void renderObjectDebugLocal(NodeBase obj, double ptick, boolean reverse_normal) {
		if (obj == null) return;
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.multMatrix(obj.getLocalState().get().storeBufferF());
		reverse_normal = reverse_normal ^ needReverseNormal(obj.getLocalState().get());
		for (NodeBase o: obj.getChildren()) renderObjectDebugLocal(o, ptick, reverse_normal);
		obj.renderDebug(ptick);
		GlStateManager.popMatrix();
	}

	public void renderObject(NodeBase obj, double ptick) {
		if (obj == null) return;
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.multMatrix(obj.getParentGlobalState().get().storeBufferF());
		renderObjectLocal(obj, ptick, needReverseNormal(obj.getParentGlobalState().get()));
		if (this.isDebugging()) renderObjectDebugLocal(obj, ptick, needReverseNormal(obj.getParentGlobalState().get()));
		GlStateManager.popMatrix();
	}

	public void renderWithoutGl(NodeBase obj, double ptick) {
		if (obj == null) return;
		if (this.isDebugging()) renderDebugWithoutGl(obj, ptick);
		
		for (NodeBase o: obj.getChildren()) renderWithoutGl(o, ptick);
		
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		Matrix render_matrix = obj.getGlobalState().get();
		GlStateManager.multMatrix(render_matrix.storeBufferF());
		obj.render(ptick, needReverseNormal(render_matrix));
		GlStateManager.popMatrix();
		
	}
	
	public void renderDebugWithoutGl(NodeBase obj, double ptick) {
		if (obj == null) return;
		
		for (NodeBase o: obj.getChildren()) renderDebugWithoutGl(o, ptick);
		
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		Matrix render_matrix = obj.getGlobalState().get();
		GlStateManager.multMatrix(render_matrix.storeBufferF());
		obj.renderDebug(ptick);
		GlStateManager.popMatrix();
		
	}

	public static Matrix getGlMatrix(int mat) {
		FloatBuffer mdata = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(mat, mdata);
		Matrix4f m = new Matrix4f();
		m.load(mdata);
		return UtilsInteract.transMat(m);
	}

	// Texture Related

	public boolean applyTexture(Texture2DBase tex) {
		if (tex instanceof Texture2DFromRes) {
			M.bindTexture(((Texture2DFromRes) tex).getLocation());
			return true;
		}

		boolean ans = false;
		if (tex.hasTexId()) {
			UtilsTex.bindTexture(tex.getTexId());
		} else {
			ans = tex.render();
			UtilsTex.bindTexture(tex.getTexId());
		}
		// setBrightness(tex.getTexId(), 1, 0, false, false);
		return ans;
	}

	// Illumination related
	
	/**
	 * TODO!!!
	 * @param light_node
	 * @param light
	 */
	public void loadLight(int light_node, Light light) {
		GlStateManager.enableLight(light_node);
		//GlStateManager.glLight(light_node, pname, params);
	}
	
	
	protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
	
	protected boolean setBrightness(int tex_id, double brightness, double partialTicks, boolean combineTextures, boolean turns_red) {
		float bright = (float) brightness;
		int color_multiplier = 0xFFFFFFFF; // getColorMultiplier(entitylivingbaseIn, f, partialTicks);
		boolean has_alpha = (color_multiplier >> 24 & 255) > 0;

		if (!has_alpha && !turns_red) {
			return false;
		} else if (!has_alpha && !combineTextures) {
			return false;
		}
		
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		
		if (turns_red) color_multiplier = 0xB3FF0000;
		brightnessBuffer.position(0);
		float a = (float) (color_multiplier >> 24 & 255) / 255.0F;
		float r = (float) (color_multiplier >> 16 & 255) / 255.0F;
		float g = (float) (color_multiplier >> 8 & 255) / 255.0F;
		float b = (float) (color_multiplier & 255) / 255.0F;
		brightnessBuffer.put(r);
		brightnessBuffer.put(g);
		brightnessBuffer.put(b);
		brightnessBuffer.put(1.0F - a);
		brightnessBuffer.flip();
		
		GlStateManager.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, brightnessBuffer);
		
		GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
		GlStateManager.enableTexture2D();
		GlStateManager.bindTexture(tex_id);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		return true;
	}

	protected void unsetBrightness() {
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
		GlStateManager.disableTexture2D();
		GlStateManager.bindTexture(0);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
		GlStateManager.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

}

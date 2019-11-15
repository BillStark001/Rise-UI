package com.billstark001.riseui.client;

import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.shader.Texture2DBase;
import com.billstark001.riseui.base.shader.Texture2DFromRes;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.UtilsInteract;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
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
	public void setDebugState(boolean s) {this.render_debug = s;}
	public boolean isDebugging() {return this.render_debug;}

	private int r, g, b, a;
	private Queue<Integer> color_cache;
	private Queue<Double> width_cache;
	private Queue<Double> size_cache;

	private final static GlHelper INSTANCE = new GlHelper(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), Minecraft.getMinecraft().renderEngine);

	private GlHelper(Tessellator T, BufferBuilder R, TextureManager M) {
		this.T = T;
		this.R = R;
		this.M = M;
		this.resetVertRender();
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
		int[] colors = this.popColorRGBA();
		double width = this.popWidth();
		double size = this.popSize();
		if (width >= 0) this.setLineWidth(width);
		if (size >= 0) this.setPointSize(size);
		R.pos(pos.get(0), pos.get(1), pos.get(2)).color(colors[0], colors[1], colors[2], colors[3]).endVertex();
	}

	// Color and Width Assignment
	public void setColor(int c) {this.r = c >> 16 & 255; this.g = c >> 8 & 255; this.b = c & 255;}	
	public void setColor(int r, int g, int b) {this.r = r; this.g = g; this.b = b;}
	public void setColor(double r, double g, double b) {setColor(UtilsTex.color(r, g, b));}
	public void setColor(float r, float g, float b) {setColor(UtilsTex.color(r, g, b));}
	
	public void setAlpha(int a) {this.a = a;}
	public void setAlpha(double a) {this.a = (int) (a * 255);}
	public void setAlpha(float a) {this.a = (int) (a * 255);}
	
	public void setColorAndAlpha(double r, double g, double b, double a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColorAndAlpha(float r, float g, float b, float a) {setColor(UtilsTex.color(r, g, b, a));}
	public void setColorAndAlpha(int r, int g, int b, int a) {this.r = r; this.g = g; this.b = b; this.a = a;}

	public void setColorAndAlpha(int c) {this.a = c >> 24 & 255; this.r = c >> 16 & 255; this.g = c >> 8 & 255; this.b = c & 255;}	
	public void setColorAndAlpha(int c, int a) {setColor(c & 0xFFFFFF); setAlpha(a);}
	public void setColorAndAlpha(int c, double a) {setColor(c & 0xFFFFFF); setAlpha(a);}
	public void setColorAndAlpha(int c, float a) {setColor(c & 0xFFFFFF); setAlpha(a);}
	
	public void setLineWidth(double width) {GL11.glLineWidth((float) width);}
	public void setPointSize(double size) {GL11.glPointSize((float) size);}
	
	public void pushColors(int[] colors) {
		for (int color: colors)
			this.color_cache.add(color);
	}
	
	public int[] popColorRGBA() {
		int[] ans = {r, g, b, a};
		if (!this.color_cache.isEmpty()) ans = UtilsTex.colorToRGBA(this.color_cache.poll());
		return ans;
	}
	
	public int[] popColorARGB() {
		int[] ans = {a, r, g, b};
		Integer ans_ = this.color_cache.poll();
		if (ans_ != null) ans = UtilsTex.colorToARGB(ans_);
		return ans;
	}
	
	public void pushWidths(double[] widths) {
		for (double color: widths)
			this.width_cache.add(color);
	}
	
	public double popWidth() {
		double ans = -1;
		if (!this.width_cache.isEmpty()) ans = this.width_cache.poll();
		return ans;
	}
	
	public void pushSizes(double[] widths) {
		for (double color: widths)
			this.size_cache.add(color);
	}
	
	public double popSize() {
		double ans = -1;
		if (!this.size_cache.isEmpty()) ans = this.size_cache.poll();
		return ans;
	}
	
	public void resetVertRender() {
		this.color_cache = new LinkedList<Integer>();
		this.width_cache = new LinkedList<Double>();
		this.size_cache = new LinkedList<Double>();
		this.setColorAndAlpha(DEFAULT_COLOR);
		this.setLineWidth(DEFAULT_WIDTH);
		this.setPointSize(DEFAULT_SIZE);
	}
	
	// State Management
	
	public void setVertState() {
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
	}
	
	public void setEdgeState() {
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
	}
	
	public void setFaceState() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
	}
	
	public void clearAccumCache() {
		GL11.glClear(GL11.GL_ACCUM);
	}
	
	public void disableDepth() {
		GlStateManager.disableDepth();
	}
	
	public void enableDepth() {
		GlStateManager.enableDepth();
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

	public void renderObjectLocal(NodeBase obj, double ptick) {
		if (obj == null) return;
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.multMatrix(obj.getLocalState().get().storeBufferF());
		obj.render(ptick);
		for (NodeBase o: obj.getChildren()) renderObjectLocal(o, ptick);
		GlStateManager.popMatrix();
	}
	
	public void renderObject(NodeBase obj, double ptick) {
		if (obj == null) return;
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		GlStateManager.multMatrix(obj.getParentGlobalState().get().storeBufferF());
		renderObjectLocal(obj, ptick);
		GlStateManager.popMatrix();
	}
	
	public void renderWithoutGl(NodeBase obj, double ptick) {
		if (obj == null) return;
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		Matrix render_matrix = obj.getGlobalState().get();
		GlStateManager.multMatrix(render_matrix.storeBufferF());
		obj.render(ptick);
		GlStateManager.popMatrix();
		for (NodeBase o: obj.getChildren()) renderWithoutGl(o, ptick);
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
			this.M.bindTexture(((Texture2DFromRes) tex).getLocation());
			return true;
		}
		
		boolean ans = false;
		if (tex.hasTexId()) {
			UtilsTex.bindTexture(tex.getTexId());
		} else {
			ans = tex.render();
		}
		return ans;
	}
}

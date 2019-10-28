package com.billstark001.riseui.client;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.io.MtlFile;
import com.billstark001.riseui.math.InteractUtils;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Vector;

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
public final class GlRenderHelper {

	private final Tessellator T;
	private final BufferBuilder R;
	private final TextureManager M;
	
	public static final int DEFAULT_COLOR = 0x00FFFF;
	
	private boolean render_debug = false;
	public void setDebugState(boolean s) {this.render_debug = s;}
	public boolean isDebugging() {return this.render_debug;}

	private int r, g, b, a;

	private final static GlRenderHelper INSTANCE = new GlRenderHelper(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), Minecraft.getMinecraft().renderEngine);

	private GlRenderHelper(Tessellator T, BufferBuilder R, TextureManager M) {
		this.T = T;
		this.R = R;
		this.M = M;
		setColor(DEFAULT_COLOR);
		setAlpha(0.5);
	}
	public static GlRenderHelper getInstance() {return INSTANCE;}

	//Base Functions

	public void startDrawing(int type, VertexFormat format) {R.begin(type, format);}
	public void startDrawingFace() {R.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);}
	public void startDrawingVert() {R.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);}
	public void startDrawingEdge(boolean isLooped) {
		if (isLooped) R.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
		else R.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
	}
	public void endDrawing() {T.draw();}

	public void addVertex(Vector pos, Vector nrm, Vector uv) {R.pos(pos.get(0), pos.get(1), pos.get(2)).normal((float) nrm.get(0), (float) nrm.get(1), (float) nrm.get(2)).tex(uv.get(0), uv.get(1)).endVertex();}
	public void addVertex(Vector pos, Vector uv) {R.pos(pos.get(0), pos.get(1), pos.get(2)).tex(uv.get(0), uv.get(1)).endVertex();}
	public void addVertex(Vector pos){R.pos(pos.get(0), pos.get(1), pos.get(2)).color(r, g, b, a).endVertex();}

	//Color Assignment

	public void setColor(double r, double g, double b) {this.r =(int)(r * 255); this.g =(int)(g * 255); this.b =(int)(b * 255);}
	public void setColor(int r, int g, int b) {this.r = r; this.g = g; this.b = b;}
	public void setColor(float r, float g, float b) {this.r =(int)(r * 255); this.g =(int)(g * 255); this.b =(int)(b * 255);}
	public void setColor(int c) {this.r = c >> 16 & 255; this.g = c >> 8 & 255; this.b = c & 255;}	

	public void setAlpha(int a) {this.a = a;}
	public void setAlpha(double a) {this.a = (int) (a * 255);}
	public void setAlpha(float a) {this.a = (int) (a * 255F);}

	public void setColor(int color, int alpha) {setColor(color); setAlpha(alpha);}
	public void setColor(int color, double alpha) {setColor(color); setAlpha(alpha);}
	public void setColor(int color, float alpha) {setColor(color); setAlpha(alpha);}
	
	public void setLineWidth(double width) {GlStateManager.glLineWidth((float) width);}
	
	//State Management
	public void dumpState() {
		// TODO
	}
	
	public void resetState() {
		// TODO
	}
	
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

	//Render

	public void renderSurface(Matrix vertpos, Matrix normal, Matrix uvmap) {
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
		for (NodeBase o: obj.getChildren()) renderObjectLocal(o, ptick);
		obj.render(ptick);
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
		//System.out.println(getGlMatrix(GL11.GL_MODELVIEW_MATRIX));
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		//obj.render(ptick);
		Matrix render_matrix = obj.getGlobalState().get();
		//System.out.println(render_matrix);
		GlStateManager.multMatrix(render_matrix.storeBufferF());
		obj.render(ptick);
		//System.out.println(getGlMatrix(GL11.GL_MODELVIEW_MATRIX));
		GlStateManager.popMatrix();
		for (NodeBase o: obj.getChildren()) renderWithoutGl(o, ptick);
	}
	
	public static Matrix getGlMatrix(int mat) {
		FloatBuffer mdata = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(mat, mdata);
		Matrix4f m = new Matrix4f();
		m.load(mdata);
		return InteractUtils.transMat(m);
	}
	
	public void bindTexture(int tex) {
		GlStateManager.bindTexture(tex);
	}
	
	public void bindTexture(ResourceLocation loc) {
		this.M.bindTexture(loc);
	}
}

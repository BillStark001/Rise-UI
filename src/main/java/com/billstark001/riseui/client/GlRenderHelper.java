package com.billstark001.riseui.client;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.material.BaseMaterial;
import com.billstark001.riseui.math.InteractUtils;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.ICompilable;
import com.billstark001.riseui.objects.IGridable;
import com.billstark001.riseui.objects.IMeshable;
import com.billstark001.riseui.objects.IRenderable;
import com.billstark001.riseui.resources.MtlFile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GlRenderHelper {

	private final Tessellator T;
	private final BufferBuilder R;
	private final TextureManager M;
	
	public static final int DEFAULT_COLOR = 0x00FFFF;

	private MtlFile mtl;
	private int r, g, b, a;

	private final static GlRenderHelper INSTANCE = new GlRenderHelper(Tessellator.getInstance(), Tessellator.getInstance().getBuffer(), Minecraft.getMinecraft().renderEngine);

	private GlRenderHelper(Tessellator T, BufferBuilder R, TextureManager M) {
		this.T = T;
		this.R = R;
		this.M = M;
		setColor(DEFAULT_COLOR);
		setAlpha(0.5);
		mtl = MtlFile.getDefault();
	}
	public static GlRenderHelper getInstance() {return INSTANCE;}

	//Base Functions

	public void startDrawing(int type, VertexFormat format) {R.begin(type, format);}
	public void startDrawingMesh() {R.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_TEX);}
	public void startDrawingGrid(boolean isLooped) {
		if (isLooped) R.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
		else R.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
	}
	public void endDrawing() {T.draw();}

	public void addVertex(Vector pos, Vector uv) {R.pos(pos.get(0), pos.get(1), pos.get(2)).tex(uv.get(0), uv.get(1)).endVertex();}
	public void addVertex(Vector pos){R.pos(pos.get(0), pos.get(1), pos.get(2)).color(r, g, b, a).endVertex();}

	public void assignMtlFile(MtlFile mtl) {if (mtl == null)this.mtl = MtlFile.getDefault(); else this.mtl = mtl;}
	public MtlFile getMtlFile() {return mtl;}

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
	
	public void setLineWidth(double width) {GL11.glLineWidth((float) width);}
	
	//State Management
	
	public void enableGridState() {
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        setLineWidth(1.5);
	}
	
	public void disableGridState() {
		GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	public void clearAccumCache() {
		GL11.glClear(GL11.GL_ACCUM);
	}

	//Render

	public void renderSurface(Matrix pos, Matrix uv) {
		startDrawingMesh();
		Vector[] vpos = pos.toVecArray();
		Vector[] vuv = uv.toVecArray();
		for (int i = 0; i < vpos.length; ++i) {
			addVertex(vpos[i], vuv[i]);
		}
		endDrawing();
	}

	public void renderMesh(IMeshable mesh) {
		//String cur_tex = ":";
		BaseMaterial cur_tex = BaseMaterial.MISSING;
		for (int i = 0; i < mesh.getFaceCount(); ++i){
			Triad[] t_ = mesh.getFace(i);
			cur_tex = mesh.getMaterial(i);
			if (cur_tex != null) cur_tex.applyOn(M);
			startDrawingMesh();
			for (Triad t: t_) {
				Vector v1, v2;
				v1 = mesh.getVertex(t.getX());
				v2 = mesh.getUVMap(t.getY());
				addVertex(v1, v2);
			}
			endDrawing();
		}
	}

	public void renderGrid(IGridable grid) {
		enableGridState();
		for (int i = 0; i < grid.getSegmentCount(); ++i){
			int[] v_ = grid.getSegment(i);
			startDrawingGrid(grid.getLooped());
			for (int v: v_) {
				addVertex(grid.getVertex(v));
			}
			endDrawing();
		}
		disableGridState();
	}

	public void renderCompiled(ICompilable obj) {
		if (!obj.isCompiled()) obj.compileList();
		BaseMaterial.INEXISTENT.applyOn(M);
		GL11.glCallList(obj.getDisplayList());
	}

	public void renderObject(BaseObject obj) {
		if (obj == BaseObject.ROOT_OBJECT) return;
		Vector p, r, s;
		Quaternion q;
		p = obj.getPos();
		q = Quaternion.reverseAxisRotate(obj.getRot());
		r = q.getImaginary();
		s = obj.getScale();
		
		GL11.glPushMatrix();
		if (obj instanceof IRenderable) ((IRenderable) obj).render();
		GL11.glTranslated(p.get(0), p.get(1), p.get(2));
		GL11.glRotated(q.getReal() * -360 / Math.PI, r.get(0), r.get(1), r.get(2));
		GL11.glScaled(s.get(0), s.get(1), s.get(2));
		for (BaseObject o: obj.getChildren()) renderObject(o);
		GL11.glPopMatrix();
		
	}
}

package com.billstark001.riseui.develop;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.xml.sax.SAXException;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.shading.mat.MaterialEdgeSimple;
import com.billstark001.riseui.base.shading.mat.MaterialFace;
import com.billstark001.riseui.base.shading.mat.MaterialVertSimple;
import com.billstark001.riseui.base.shading.mat.TagApplyMaterialEdge;
import com.billstark001.riseui.base.shading.mat.TagApplyMaterialFace;
import com.billstark001.riseui.base.shading.mat.TagApplyMaterialVert;
import com.billstark001.riseui.base.shading.mat.Texture2DGrayGen;
import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.computation.ColorGradient;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.UtilsInteract;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.core.polygon.Polygon;
import com.billstark001.riseui.core.polygon.Presets;
import com.billstark001.riseui.io.CharResourceLoader;
import com.billstark001.riseui.io.ColladaFile;
import com.billstark001.riseui.io.IOUtils;
import com.billstark001.riseui.io.MtlFile;
import com.billstark001.riseui.io.ObjFile;
import com.billstark001.riseui.render.GlHelper;
import com.dddviewr.collada.Collada;

import net.minecraft.util.ResourceLocation;

public class RenderTestObject{

	private static long tstart = System.currentTimeMillis();
	private static long tend;
	private static double anim_cycle = 10;

	private static CharResourceLoader res = CharResourceLoader.getInstance();
	private static GlHelper glhelper = GlHelper.getInstance();
	private static ObjFile h_obj, t_obj;
	private static MtlFile h_mtl, t_mtl;
	private static ColladaFile sp_dae;
	public static Polygon horse, table, sphere;
	private static Polygon hgrid;
	private static EmptyNode spider;

	private static final ResourceLocation lobj = new ResourceLocation("riseui:models/skh.obj");
	private static final ResourceLocation lmtl = new ResourceLocation("riseui:models/skh.mtl");
	private static final ResourceLocation tobj = new ResourceLocation("riseui:models/table.obj");
	private static final ResourceLocation tmtl = new ResourceLocation("riseui:models/table.mtl");
	private static final ResourceLocation spic = new ResourceLocation("riseui:models/motive_spider1.dae");

	public static void prepareRender() {

		glhelper.setDebugState(true);

		res.loadRes(lobj);
		res.loadRes(lmtl);
		res.loadRes(tobj);
		res.loadRes(tmtl);
		h_obj = new ObjFile(res.getRes(lobj));
		h_mtl = new MtlFile(res.getRes(lmtl));
		t_obj = new ObjFile(res.getRes(tobj));
		t_mtl = new MtlFile(res.getRes(tmtl));
		h_obj.linkMtlFile(h_mtl);
		t_obj.linkMtlFile(t_mtl);

		horse = h_obj.genPoly("skh");
		horse.setRenderState(new State3DIntegrated(null, null, 0.01));
		horse.rasterize();
		horse.setLocalState(new State3DIntegrated(new Vector(0, 0.5, 0)));

		table = t_obj.genPoly("table");
		table.setRenderState(new State3DIntegrated(null, null, 0.01));
		table.rasterize();
		table.setLocalState(new State3DIntegrated(new Vector(0, 2.5, 0)));

		hgrid = h_obj.genPoly("skh");
		hgrid.setRenderState(new State3DIntegrated(null, null, 0.005));
		hgrid.rasterize();
		hgrid.setLocalState(new State3DIntegrated(new Vector(0, 3.5, 0)));

		sphere = Presets.getPolygon("sphere_high_lod");
		sphere.setRenderState(new State3DIntegrated(null, null, new Vector(3, 1, 3)));
		sphere.addTag(new TagApplyMaterialFace(1, true, new MaterialFace().setDiffuse(new Texture2DGrayGen(Texture2DGrayGen::polar, ColorGradient.getDefaultTransparent()))));

		table.setParentRemainGlobalState(sphere);
		sphere.setParentRemainGlobalState(horse);

		horse.dump();

		// Spider
		Collada stemp = null;
		try {
			stemp = Collada.readFile(IOUtils.getInputStream(spic));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}

		sp_dae = new ColladaFile(stemp);
		sp_dae.parse();
		spider = (EmptyNode) sp_dae.getNodeByName("spider");
		spider.setLocalState(new State3DIntegrated(new Vector(2, 1, 2), null, 0.01));
		spider.setVisEdge(NodeBase.Visibility.TRUE);
		spider.setVisVert(NodeBase.Visibility.TRUE);
		TagApplyMaterialEdge tme = new TagApplyMaterialEdge(new MaterialEdgeSimple(UtilsTex.color(0.5, 0, 1, 0.3), 1.5));
		TagApplyMaterialVert tmv = new TagApplyMaterialVert(new MaterialVertSimple(UtilsTex.color(1, 0, 0.5, 0.6), 4));
		for (Iterator<NodeBase> it = spider.getTreeIteratorBreadthFirst(); it.hasNext();) {
			NodeBase nb = it.next();
			nb.addTag(tme);
			nb.addTag(tmv);
		}

	}

	public static Matrix getGlMatrix(int mat) {
		FloatBuffer mdata = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(mat, mdata);
		Matrix4f m = new Matrix4f();
		m.load(mdata);
		return UtilsInteract.transMat(m);
	}

	public static void doRender(double delta) {

		tend = System.currentTimeMillis();
		double cur_time = ((tend - tstart) / 1000.) % anim_cycle;
		//System.out.println(cur_time);
		
		glhelper.setDebugState(true);

		horse.setLocalState(new State3DIntegrated(new Vector(0, cur_time, 0)));
		spider.setChildrenFrameTime(cur_time);
		
		glhelper.renderObject(horse, delta);
		glhelper.renderObject(spider, delta);

		/*
		if (DevelopProxy.mark1 == 1)

		else if (DevelopProxy.mark1 == 2) 
			render.renderCompiled(cube);
		else
			render.renderMesh(cube);
			//render.renderCompiled(horse);
			//render.renderCompiled(cube);
		*/

		//GlStateManager.popMatrix();
		Shader.SHADER_DIFFUSE.applyState();
	}	

}

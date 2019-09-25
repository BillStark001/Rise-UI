package com.billstark001.riseui.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.shader.BaseMaterial;
import com.billstark001.riseui.base.shader.MaterialFace;
import com.billstark001.riseui.base.shader.TagApplyMaterialFace;
import com.billstark001.riseui.base.shader.TagSelectionHardTable;
import com.billstark001.riseui.base.shader.Texture2DBase;
import com.billstark001.riseui.base.shader.Texture2DFromRes;
import com.billstark001.riseui.base.state.StateStandard3D;
import com.billstark001.riseui.core.character.Joint;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.core.polygon.Polygon;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.FloatArray;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.content.animation.LibraryAnimations;
import com.dddviewr.collada.content.controller.Controller;
import com.dddviewr.collada.content.controller.LibraryControllers;
import com.dddviewr.collada.content.effects.Effect;
import com.dddviewr.collada.content.effects.EffectAttribute;
import com.dddviewr.collada.content.effects.EffectMaterial;
import com.dddviewr.collada.content.effects.LibraryEffects;
import com.dddviewr.collada.content.geometry.Geometry;
import com.dddviewr.collada.content.geometry.LibraryGeometries;
import com.dddviewr.collada.content.geometry.Mesh;
import com.dddviewr.collada.content.geometry.PolyList;
import com.dddviewr.collada.content.geometry.Primitives;
import com.dddviewr.collada.content.materials.LibraryMaterials;
import com.dddviewr.collada.content.materials.Material;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.BaseXform;
import com.dddviewr.collada.content.visualscene.Rotate;
import com.dddviewr.collada.content.visualscene.Scale;
import com.dddviewr.collada.content.visualscene.Translate;
import com.dddviewr.collada.content.visualscene.VisualScene;

import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

public class ColladaFile {
	
	private final Collada file;
	
	private VisualScene scene = null;
	private LibraryGeometries lgeo = null;
	private LibraryMaterials lmat = null;
	private LibraryEffects leff = null;
	private LibraryControllers lcon = null;
	private LibraryAnimations lani = null;
	
	private Map<Integer, MaterialFace> material = null;
	
	private Map<String, BaseNode> parsed = new HashMap<String, BaseNode>();
	
	public BaseNode getNodeByName(String name) {
		if (name == null) return null;
		return parsed.getOrDefault(name, null);
	}
	
	public BaseNode[] getNodes() {
		return parsed.values().toArray(new BaseNode[0]);
	}
	
	public String[] getNodeNames() {
		return parsed.keySet().toArray(new String[0]);
	}
	
	public ColladaFile(Collada file) {
		this.file = file;
	}
	
	public int getSceneIndex() {
		return file.getScene().getInstanceVisualScene().getUrl();
	}
	
	public void parse() {
		scene = file.getLibraryVisualScenes().getElement(file.getScene().getInstanceVisualScene().getUrl());
		lgeo = file.getLibraryGeometries();
		lmat = file.getLibraryMaterials();
		leff = file.getLibraryEffects();
		lcon = file.getLibraryControllers();
		lani = file.getLibraryAnimations();
		
		// Materials
		
		material = new HashMap<Integer, MaterialFace>();
		for (Material m: lmat.getElements()) {
			String name = m.getName();
			int id = m.getId();
			Effect etemp = leff.getElement(m.getInstanceEffect().getUrl());
			EffectMaterial emat = etemp.getEffectMaterial();
			MaterialFace mat = new MaterialFace(name);
			EffectAttribute dif, emi, tra, spe;
			dif = emat.getDiffuse();
			emi = emat.getEmission();
			tra = emat.getTransparency();
			spe = emat.getSpecular();
			if (dif != null) mat.setAlbedo(new Texture2DFromRes(dif.getTexture().getTexture()));
			if (emi != null) mat.setEmission(new Texture2DFromRes(emi.getTexture().getTexture()));
			if (tra != null) mat.setTransparency(new Texture2DFromRes(tra.getTexture().getTexture()));
			if (spe != null) mat.setSpecular(new Texture2DFromRes(spe.getTexture().getTexture()));
			this.material.put(id, mat);
		}
		
		//leff.getElement(lmat.getElement(1).getInstanceEffect().getUrl()).dump();
		for (Node ntemp: scene.getNodes()) {
			BaseNode n = parseNode(ntemp);
			String put_name = n.getName();
			while (parsed.containsKey(put_name)) put_name = put_name + "_";
			parsed.put(put_name, n);
		}
	}
	
	public StateStandard3D parseState(BaseXform[] xf) {
		StateStandard3D ans = null;
		Vector p = null;
		double ry = 0, rx = 0, rz = 0;
		Vector s = null;
		for (BaseXform xft: xf) {
			if (xft instanceof Translate) p = new Vector(((Translate) xft).getData());
			else if (xft instanceof Scale) s = new Vector(((Scale) xft).getData());
			else if (xft instanceof Rotate) {
				float[] d = ((Rotate) xft).getData();
				if (d[0] > 0.999) rx = d[3];
				else if (d[1] > 0.999) ry = d[3];
				else if (d[2] > 0.999) rz = d[3];
			}
		}
		Vector vr = new Vector(rx, ry, rz).mult(Math.PI / 180);
		Quaternion r = Quaternion.eulerToQuat(vr);
		ans = new StateStandard3D(p, r, s);
		return ans;
	}
	
	public Polygon parseMesh(Mesh m) {
		Matrix mpos = new Matrix(m.getPositionData().parseD(4, 1));
		Matrix mtex = new Matrix(m.getTexCoordData().parseTexWithReverse(true));
		Matrix mnor = new Matrix(m.getNormalData().parseD(4, 0));

		ArrayList<Triad[]> faces = new ArrayList<Triad[]>();
		ArrayList<MaterialFace> mats = new ArrayList<MaterialFace>();
		for (Primitives prim: m.getPrimitives()) {
			ArrayList<Triad> face = new ArrayList<Triad>();
			PolyList pr = (PolyList) prim;
			int[][] vtemp = pr.getParsed();
			int nr_faces = vtemp.length; //pr.getVcount().getAccData()[pr.getVcount().getData().length - 1];
			for (int i = 0; i < nr_faces; ++i) {
				face.add(new Triad(vtemp[i][0], vtemp[i][2], vtemp[i][1]));
				if (i == 0) mats.add(this.material.get(prim.getMaterial()));
				else mats.add(null);
			}
			faces.add(face.toArray(new Triad[0]));
		}
		
		int face_count = faces.size();
		MaterialFace[] mat_cache = mats.toArray(new MaterialFace[0]);
		
		Polygon ans = new Polygon(mpos, mtex, mnor, faces.toArray(new Triad[0][0]));
		
		Map<MaterialFace, boolean[]> selections = new HashMap<MaterialFace, boolean[]>();
		MaterialFace current_mat = null;
		for (int i = 0; i < face_count; ++i) {
			MaterialFace mat = mat_cache[i];
			if (mat != null) {
				current_mat = mat;
				if (!selections.containsKey(current_mat))
					selections.put(current_mat, new boolean[face_count]);
			}
			selections.get(current_mat)[i] = true;
		}
		for (MaterialFace mat: selections.keySet()) {
			TagSelectionHardTable sel = new TagSelectionHardTable(selections.get(mat));
			//System.out.println(ans.addTag(sel));
			//System.out.println(ans.addTag(new TagApplyMaterialFace(mat, sel)));
		}
		
		return ans;
	}
	
	public BaseNode parseNode(Node n) {
		//System.out.println(n);
		StateStandard3D c = parseState(n.getXforms());
		BaseNode ans = null;
		if (n.isJoint()) ans = new Joint(c, n.getName());
		else if (n.getInstanceGeometry() != null && n.getInstanceGeometry().size() > 0) {
			Geometry gtemp = lgeo.getElement(n.getInstanceGeometry().get(0).getUrl());
			ans = parseMesh(gtemp.getMesh());
			ans.setName(n.getName());
			ans.setLocalState(c);
		} else {
			ans = new EmptyNode(c, n.getName());
		}
		
		// TODO InstanceCamera & InstanceLight
		if (n.getInstanceController() != null) {
			Controller c1 = lcon.getElement(n.getInstanceController().getId());
		}
		for (Node nt: n.getChildNodes()) {
			BaseNode bnt = parseNode(nt);
			ans.addChildRemainLocal(bnt);
		}
		return ans;
	}

}

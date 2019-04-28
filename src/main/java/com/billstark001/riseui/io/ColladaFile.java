package com.billstark001.riseui.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.StateContainer;
import com.billstark001.riseui.base.shader.BaseMaterial;
import com.billstark001.riseui.core.character.Joint;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.core.polygon.PolygonMesh;
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
import com.dddviewr.collada.content.effects.LibraryEffects;
import com.dddviewr.collada.content.geometry.Geometry;
import com.dddviewr.collada.content.geometry.LibraryGeometries;
import com.dddviewr.collada.content.geometry.Mesh;
import com.dddviewr.collada.content.geometry.PolyList;
import com.dddviewr.collada.content.geometry.Primitives;
import com.dddviewr.collada.content.materials.LibraryMaterials;
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
		
		//leff.getElement(lmat.getElement(1).getInstanceEffect().getUrl()).dump();
		for (Node ntemp: scene.getNodes()) {
			BaseNode n = parseNode(ntemp);
			String put_name = n.getName();
			while (parsed.containsKey(put_name)) put_name = put_name + "_";
			parsed.put(put_name, n);
		}
	}
	
	public StateContainer parseState(BaseXform[] xf) {
		StateContainer ans = null;
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
		ans = new StateContainer(p, r, s);
		return ans;
	}
	
	public PolygonMesh parseMesh(Mesh m) {
		Matrix mpos = new Matrix(m.getPositionData().parseD(3));
		Matrix mtex = new Matrix(m.getTexCoordData().parseD(3));
		Matrix mnor = new Matrix(m.getNormalData().parseD(3));
		ArrayList<Integer> findext = new ArrayList<Integer>();
		ArrayList<Triad> vertices = new ArrayList<Triad>();
		ArrayList<BaseMaterial> mindex = new ArrayList<BaseMaterial>();
		for (Primitives p: m.getPrimitives()) {
			PolyList pr = (PolyList) p;
			for (int i: pr.getVcount().getData()) findext.add(i);
			int[][] vtemp = pr.getParsed();
			int nr_faces = vtemp.length; //pr.getVcount().getAccData()[pr.getVcount().getData().length - 1];
			for (int i = 0; i < nr_faces; ++i) {
				vertices.add(new Triad(vtemp[i][0], vtemp[i][1], vtemp[i][2]));
				if (i == 0) mindex.add(new BaseMaterial("riseui:tex/cave_spider.png"));
				// TODO Material Instantiation
				else mindex.add(null);
			}
		}
		int[] findex = new int[findext.size()];
		findex[0] = findext.get(0);
		for (int i = 1; i < findext.size(); ++i) findex[i] = findex[i-1] + findext.get(i);
		
		PolygonMesh ans = new PolygonMesh(mpos, mtex, mnor, vertices.toArray(new Triad[0]), findex, mindex.toArray(new BaseMaterial[0]));
		return ans;
	}
	
	public BaseNode parseNode(Node n) {
		//System.out.println(n);
		StateContainer c = parseState(n.getXforms());
		BaseNode ans = null;
		if (n.isJoint()) ans = new Joint(c.p, c.r, c.s, n.getName());
		else if (n.getInstanceGeometry() != null && n.getInstanceGeometry().size() > 0) {
			Geometry gtemp = lgeo.getElement(n.getInstanceGeometry().get(0).getUrl());
			ans = parseMesh(gtemp.getMesh());
			ans.setName(n.getName());
			ans.setPos(c.p);
			ans.setRot(c.r);
			ans.setScale(c.s);
		} else {
			ans = new EmptyNode(c.p, c.r, c.s, n.getName());
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

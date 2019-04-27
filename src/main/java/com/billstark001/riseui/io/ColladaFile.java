package com.billstark001.riseui.io;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.BaseObject;
import com.billstark001.riseui.base.StateContainer;
import com.billstark001.riseui.core.character.Joint;
import com.billstark001.riseui.core.polygon.PolygonMesh;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.FloatArray;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.content.geometry.Geometry;
import com.dddviewr.collada.content.geometry.LibraryGeometries;
import com.dddviewr.collada.content.geometry.Mesh;
import com.dddviewr.collada.content.geometry.PolyList;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.BaseXform;
import com.dddviewr.collada.content.visualscene.Rotate;
import com.dddviewr.collada.content.visualscene.Scale;
import com.dddviewr.collada.content.visualscene.Translate;
import com.dddviewr.collada.content.visualscene.VisualScene;

import scala.actors.threadpool.Arrays;

public class ColladaFile {
	
	private final Collada file;
	private LibraryGeometries lgeo = null;
	
	public ColladaFile(Collada file) {
		this.file = file;
	}
	
	public int getSceneIndex() {
		return file.getScene().getInstanceVisualScene().getUrl();
	}
	
	public void parse() {
		file.getScene().dump();
		VisualScene scene = file.getLibraryVisualScenes().getElement(file.getScene().getInstanceVisualScene().getUrl());
		lgeo = file.getLibraryGeometries();
		
		Node ntemp = scene.getNode(6);
		//ntemp.dump();
		//parseNode(ntemp);
		parseMesh(new StateContainer(), lgeo.getElement(207).getMesh());
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
		Vector vr = new Vector(rx, ry, rz);
		Quaternion r = Quaternion.eulerToQuatFast(vr);
		ans = new StateContainer(p, r, s);
		return ans;
	}
	
	public PolygonMesh parseMesh(StateContainer c, Mesh m) {
		m.dump();
		Matrix mpos = new Matrix(m.getPositionData().parseD(3));
		Matrix mtex = new Matrix(m.getTexCoordData().parseD(3));
		Matrix mnor = new Matrix(m.getNormalData().parseD(3));
		PolyList l = (PolyList) m.getPrimitives().get(0);
		int[] findex = l.getVcount().getAccData();
		int[][] vtemp = l.getParsed();
		Triad[] faces = new Triad[findex[findex.length - 1]];
		for (int i = 0; i < faces.length; ++i) {
			faces[i] = new Triad(vtemp[i][0], vtemp[i][1], vtemp[i][2]);
		}
		PolygonMesh ans = new PolygonMesh(mpos, mtex, mnor, faces, findex, null);
		return ans;
	}
	
	public BaseNode parseNode(Node n) {
		StateContainer c = parseState(n.getXforms());
		BaseNode ans = null;
		if (n.isJoint()) ans = new Joint(c.p, c.r, c.s, n.getName());
		else if (n.getInstanceGeometry() != null && n.getInstanceGeometry().size() > 0) {
			Geometry gtemp = lgeo.getElement(n.getInstanceGeometry().get(0).getUrl());
			ans = parseMesh(c, gtemp.getMesh());
			ans.setName(n.getName());
			ans.setPos(c.p);
			ans.setRot(c.r);
			ans.setScale(c.s);
		}
		if (n.getInstanceController() != null) {
			
		}
		for (Node nt: n.getChildNodes()) {
			BaseNode bnt = parseNode(nt);
			ans.addChild(bnt);
		}
		return ans;
	}

}

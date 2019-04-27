package com.billstark001.riseui.io;

import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.StateContainer;
import com.billstark001.riseui.math.Vector;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.BaseXform;
import com.dddviewr.collada.content.visualscene.Rotate;
import com.dddviewr.collada.content.visualscene.Scale;
import com.dddviewr.collada.content.visualscene.Translate;
import com.dddviewr.collada.content.visualscene.VisualScene;

import scala.actors.threadpool.Arrays;

public class ColladaFile {
	
	private final Collada file;
	
	public ColladaFile(Collada file) {
		this.file = file;
	}
	
	public int getSceneIndex() {
		return file.getScene().getInstanceVisualScene().getUrl();
	}
	
	public void parse() {
		file.getScene().dump();
		VisualScene scene = file.getLibraryVisualScenes().getElement(file.getScene().getInstanceVisualScene().getUrl());
		
		
		Node ntemp = scene.getNode(6);
		ntemp.dump();
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

}

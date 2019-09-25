package com.billstark001.riseui.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.billstark001.riseui.base.shader.MaterialFace;
import com.billstark001.riseui.base.shader.Texture2DBase;
import com.billstark001.riseui.base.shader.Texture2DFromRes;

import net.minecraft.client.resources.IResourceManager;

public class MtlFile {
	
	private Map<String, MaterialFace> mat;
	
	private final String orig;
	
	public MtlFile (String str) {
		this.orig = str;
		mat = new HashMap<String, MaterialFace>();
		readMtl();
	}
	
	private static final MtlFile DEFAULT = new MtlFile("newmtl default\nKd 1 1 1\nmap_Kd riseui:missing_texture\nillum 7");
	public static MtlFile getDefault() {return DEFAULT;}
	
	private static String[] optimize (String str) {
		String[] s = str.split("\n");
		for (int i = 0; i < s.length; ++i) {
			if (s[i].split("\r").length == 0) s[i] = "";
			else s[i] = s[i].split("\r")[0];
		}
		return s;
	}
	
	private static String getType (String s) {
		if (s.equals("") || s.startsWith(" ") || s.startsWith("#")) {return "dummy";}
		int c = (int)s.toCharArray()[0];
		if (c == 13 || c == 65535) {return "dummy";}
		s = s.split(" ")[0];
		if (!s.equals("map_Kd") && !s.equals("newmtl")) {return "dummy";}
		return s;
	}
	
	private void readMtl () {
		String[] st = optimize(orig);
		String name = null;
		for(String i: st){
			//System.out.println(i);
			String t = getType(i);
			if (t.equals("dummy")) continue;
			if (t.equals("newmtl")) name = i.substring(7);
			else if (t.equals("map_Kd")) {
				String val = i.substring(7);
				Texture2DFromRes tex = new Texture2DFromRes(val);
				MaterialFace mat = new MaterialFace(name);
				mat.setAlbedo(tex);
			}
		}
	}
	
	public String[] getList () {
		Object[] ot = mat.keySet().toArray();
		String[] ans = new String[ot.length];
		for (int i = 0; i < ot.length; ++i) {
			ans[i] = ot[i].toString();
		}
		return ans;
	}
	
	public MaterialFace getMaterial (String name) {
		return mat.getOrDefault(name, MaterialFace.DEFAULT);
	}
	
}
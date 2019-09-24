package com.billstark001.riseui.core.polygon;

import java.util.HashMap;

import com.billstark001.riseui.io.ObjFile;
import com.billstark001.riseui.io.CharResourceLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

public class Presets {

	private static ObjFile pre, preh;
	//private static HashMap lm = new HashMap<String, PolygonMesh>();
	//private static HashMap lw = new HashMap<String, PolygonGrid>();
	private static String[] names;
	
	public static void loadPresets() {
		ResourceLocation low = new ResourceLocation("riseui:models/presets.obj");
		ResourceLocation high = new ResourceLocation("riseui:models/presets_high_lod.obj");
		CharResourceLoader res = CharResourceLoader.getInstance();
		res.loadRes(low);
		res.loadRes(high);
		pre = new ObjFile(res.getRes(low));
		preh = new ObjFile(res.getRes(high));
		pre.linkMtlFile(null);
		preh.linkMtlFile(null);
		String[] s1, s2;
		s1 = pre.getObjectList();
		s2 = preh.getObjectList();
		names = (String[]) Arrays.copyOf(s1, s1.length + s2.length);
		System.arraycopy(s2, 0, names, s1.length, s2.length);
	}
	
	public static String[] getObjects() {return names;}
	
	public static Polygon getPolygon(String name) {
		Polygon ans = pre.genPoly(name);
		if (ans == null) ans = preh.genPoly(name);
		//if (ans != null) lm.put(name, ans);
		return ans;
	}
	
	//private static String[] names = presets.getObjectList(); 
}

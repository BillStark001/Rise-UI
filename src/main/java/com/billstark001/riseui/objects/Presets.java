package com.billstark001.riseui.objects;

import java.util.HashMap;

import com.billstark001.riseui.resources.ObjFile;
import com.billstark001.riseui.resources.ResourceLoader;

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
		ResourceLoader res = ResourceLoader.getInstance();
		res.loadRes(low);
		res.loadRes(high);
		pre = new ObjFile(res.getRes(low));
		preh = new ObjFile(res.getRes(high));
		String[] s1, s2;
		s1 = pre.getObjectList();
		s2 = preh.getObjectList();
		names = (String[]) Arrays.copyOf(s1, s1.length + s2.length);
		System.arraycopy(s2, 0, names, s1.length, s2.length);
	}
	
	public static String[] getObjects() {return names;}
	
	public static PolygonMesh getMesh(String name) {
		PolygonMesh ans = pre.genMesh(name);
		if (ans == null) ans = preh.genMesh(name);
		//if (ans != null) lm.put(name, ans);
		return ans;
	}
	
	public static PolygonGrid getGrid(String name) {
		PolygonGrid ans = pre.genGrid(name);
		if (ans == null) ans = preh.genGrid(name);
		//if (ans != null) lw.put(name, ans);
		return ans;
	}
	
	//private static String[] names = presets.getObjectList(); 
}

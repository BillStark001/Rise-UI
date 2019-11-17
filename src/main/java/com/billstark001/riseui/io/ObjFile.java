package com.billstark001.riseui.io;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.billstark001.riseui.base.shading.MaterialFace;
import com.billstark001.riseui.base.shading.TagApplyMaterialFace;
import com.billstark001.riseui.base.shading.TagSelectionHardTable;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Pair;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.polygon.Polygon;

import net.minecraft.util.ResourceLocation;

public final class ObjFile {

	private ArrayList<Vector> vertex;
	private ArrayList<Vector> normal;
	private ArrayList<Vector> coord;
	private Map<String, ArrayList> proc;

	private final String orig;
	private MtlFile linked_mtl = MtlFile.getDefault();
	private String mtldir;

	public ObjFile(String str) {
		this.orig = str;
		vertex = new ArrayList<Vector>();
		normal = new ArrayList<Vector>();
		coord = new ArrayList<Vector>();
		proc = new HashMap<String, ArrayList>();
		parse();
	}

	private static String[] optimize(String str) {
		String[] s = str.split("\n");
		for (int i = 0; i < s.length; ++i) {
			if (s[i].split("\r").length == 0)
				s[i] = "";
			else
				s[i] = s[i].split("\r")[0];
		}
		return s;
	}

	private static String getType(String s) {
		if (s.equals("") || s.startsWith(" ") || s.startsWith("#")) {
			return "dummy";
		}
		int c = (int) s.toCharArray()[0];
		if (c == 13 || c == 65535) {
			return "dummy";
		}
		s = s.split(" ")[0];
		if (s.equals("o")) {
			s = "g";
		}
		return s;
	}

	private static Vector parseVertex(String s) {
		String[] st = s.split(" ");
		Vector v = new Vector(Double.valueOf(st[1]), Double.valueOf(st[2]), Double.valueOf(st[3]));
		return v;
	}
	
	private static Vector parseVertexHomo(String s, double fill) {
		String[] st = s.split(" ");
		Vector v = new Vector(Double.valueOf(st[1]), Double.valueOf(st[2]), Double.valueOf(st[3]), fill);
		return v;
	}

	private Triad[] parseFace(String s) {
		String[] st = s.split(" ");
		int l = 0;
		for (int i = 1; i < st.length; ++i) {
			if (st[i].equals(""))
				continue;
			++l;
		}
		Triad[] v = new Triad[l];
		int x = 0;
		for (int i = 1; i < st.length; ++i) {
			if (st[i].equals(""))
				continue;
			String[] ss = st[i].split("/");
			if (ss.length == 2)
				v[x++] = new Triad(Integer.valueOf(ss[0]), Integer.valueOf(ss[1]), 0);
			else
				v[x++] = new Triad(Integer.valueOf(ss[0]), Integer.valueOf(ss[1]), Integer.valueOf(ss[2]));
		}
		return v;
	}

	private void parse() {
		proc = new HashMap<String, ArrayList>();
		String[] st = optimize(orig);
		ArrayList<String> tpoly = null;
		String t_ = null;
		for (String i : st) {
			String t = getType(i);
			if (t == "dummy")
				continue;
			else if (t.startsWith("v")) {
				if (t.equals("v"))
					vertex.add(parseVertexHomo(i, 1));
				if (t.equals("vt"))
					coord.add(parseVertex(i));
				if (t.equals("vn"))
					normal.add(parseVertexHomo(i, 0));
			} else if (t.equals("f")) {
				tpoly.add(i);
			} else if (t.equals("usemtl")) {
				tpoly.add("m " + i.substring(7));
			} else if (t.equals("g")) {
				if (t_ != null)
					proc.put(t_, tpoly);
				tpoly = new ArrayList<String>();
				t_ = i.substring(2);
			} else if (t.equals("mtllib")) {
				this.setMtldir(i.substring(7));
				this.linkMtlFile();
			}
		}
		if (t_ != null)
			proc.put(t_, tpoly);
	}
	
	public Polygon genPoly(String name) {
		if (!proc.containsKey(name))
			return null;
		
		ArrayList<String> orig = proc.get(name);
		ArrayList<String> matid = new ArrayList<String>();
		int face_count = 0;
		for (String cur : orig) {
			if (cur.startsWith("m"))
				matid.add(cur.substring(2));
			else if (cur.startsWith("f"))
				++face_count;
		}
		
		Map<String, Integer> mv = new LinkedHashMap<String, Integer>();
		Map<String, Integer> mn = new LinkedHashMap<String, Integer>();
		Map<String, Integer> mt = new LinkedHashMap<String, Integer>();
		int vc = 0, nc = 0, tc = 0;
		
		ArrayList<Triad[]> faces_t = new ArrayList<Triad[]>();
		MaterialFace[] mat_cache = new MaterialFace[face_count];
		face_count = 0;

		for (String cur : orig) {
			ArrayList<Triad> face_t = new ArrayList<Triad>();
			
			if (cur.startsWith("m")) {
				String mat_name = cur.substring(2);
				if (this.linked_mtl != null) 
					mat_cache[face_count] = this.linked_mtl.getMaterial(mat_name);
				else
					mat_cache[face_count] = new MaterialFace(mat_name);
			}
			if (!cur.startsWith("f")) continue;
			String[] st = cur.split(" ");
			int cur_num_count = 0;
			
			for (String s: st) {
				if (s.equals("") || s.equals("f")) continue;
				++cur_num_count;
				String[] ss = s.split("/");
				if (!mv.containsKey(ss[0])) mv.put(ss[0], vc++);
				if (ss.length > 2 && !mn.containsKey(ss[2])) mn.put(ss[2], nc++);
				if (!mt.containsKey(ss[1])) mt.put(ss[1], tc++);
			}
			String[] st1 = cur.split(" ");
			
			int x = 0;
			for (int i = 1; i < st1.length; ++i) {
				if (st1[i].equals(""))
					continue;
				String[] ss = st1[i].split("/");
				if (ss.length == 2)
					face_t.add(new Triad(mv.get(ss[0]), mt.get(ss[1]), -1));
				else
					face_t.add(new Triad(mv.get(ss[0]), mt.get(ss[1]), mn.get(ss[2])));
			}
			
			faces_t.add(face_t.toArray(new Triad[0]));
			face_count++;
		}
		//pr(faces);
		//pr(Arrays.toString(vindex));
		//pr(Arrays.toString(mats));
		
		ArrayList<Vector> vtv = new ArrayList<Vector>();
		ArrayList<Vector> vtt = new ArrayList<Vector>();
		ArrayList<Vector> vtn = new ArrayList<Vector>();
		
		for (Object sv: mv.keySet()) vtv.add(vertex.get(Integer.valueOf((String) sv) - 1));
		for (Object st: mt.keySet()) vtt.add(coord.get(Integer.valueOf((String) st) - 1));
		for (Object sn: mn.keySet()) vtn.add(normal.get(Integer.valueOf((String) sn) - 1));
		
		Matrix mtv = null, mtt = null, mtn = null;
		if (vc != 0) mtv = new Matrix(vtv);
		if (tc != 0) mtt = new Matrix(vtt);
		if (nc != 0) mtn = new Matrix(vtn);

		Triad[][] faces = faces_t.toArray(new Triad[0][0]);
		Polygon ans = new Polygon(mtv, mtt, mtn, faces);
		
		// Materials
		
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
			ans.addTag(sel);
			ans.addTag(new TagApplyMaterialFace(mat, sel));
		}
		
		
		ans.setName(name);
		return ans;
	}
	
	private static Comparator<Pair> paircomp = new Comparator<Pair>() {
		@Override
		public int compare(Pair t1, Pair t2) {
			int x_ = 0;
			x_ = compare_(t1.getX(), t2.getX());
			if (!(x_ == 0))
				return x_;
			x_ = compare_(t1.getY(), t2.getY());
			if (!(x_ == 0))
				return x_;
			return x_;
		}

		private int compare_(Integer x, Integer y) {
			return x - y;
		}
	};
	
	public static ArrayList<int[]> genGridEdges(Triad[][] vertices) {
		ArrayList<Pair> lv = new ArrayList<Pair>();
		for (int i = 0; i < vertices.length; ++i) {
			Triad[] st = vertices[i];			
			ArrayList<Integer> v1 = new ArrayList<Integer>();
			for (Triad s: st) {
				v1.add(s.getX());
			}
			
			for (int i1 = 0; i1 < v1.size(); ++i1) {
				int x1 = i1 - 1, x2 = i1;
				if (i1 == 0)
					x1 += v1.size();
				x1 = v1.get(x1);
				x2 = v1.get(x2);
				if (x1 > x2) {
					int x0 = x2;
					x2 = x1;
					x1 = x0;
				}
				lv.add(new Pair(x1, x2));
			}
		}
		lv.sort(paircomp);
		
		ArrayList<int[]> ansa = new ArrayList<int[]>();
		for (Pair p: lv) {
			ansa.add(p.toIntArray());
		}
		return ansa;
	}

	/*
	public PolygonGrid genGrid(String name) {

		ArrayList<String> orig = proc.get(name);
		Map<String, Integer> mv = new LinkedHashMap<String, Integer>();
		int vc = 0;

		for (String cur : orig) {
			if (!cur.startsWith("f")) continue;
			String[] st = cur.split(" ");
			
			for (String s: st) {
				if (s.equals("") || s.equals("f")) continue;
				String[] ss = s.split("/");
				if (!mv.containsKey(ss[0])) mv.put(ss[0], vc++);
			}
		}
		
		ArrayList<Vector> vtv = new ArrayList<Vector>();
		for (Object sv: mv.keySet()) vtv.add(vertex.get(Integer.valueOf((String) sv) - 1));
		Matrix mtv = null;
		if (vc != 0) mtv = new Matrix(vtv);
		
		ArrayList<Pair> lv = new ArrayList<Pair>();
		ArrayList[] graph = new ArrayList[mv.size()];
		for (int i = 0; i < mv.size(); ++i) graph[i] = new ArrayList<Integer>();
		for (String cur : orig) {
			if (!cur.startsWith("f"))
				continue;
			
			String[] st = cur.split(" ");
			ArrayList<String> v1 = new ArrayList<String>();
			int x3 = 0;
			for (String s: st) {
				if (s.equals("") || s.equals("\r") || s.equals("f"))
					continue;
				String[] ss = s.split("/");
				v1.add(ss[0]);
			}
			
			for (int i = 0; i < v1.size(); ++i) {
				int x1 = i - 1, x2 = i;
				if (i == 0)
					x1 += v1.size();
				x1 = mv.get(v1.get(x1));
				x2 = mv.get(v1.get(x2));
				if (x1 > x2) {
					int x0 = x2;
					x2 = x1;
					x1 = x0;
				}
				lv.add(new Pair(x1, x2));
				if (!graph[x1].contains(x2)) graph[x1].add(x2);
			}
		}
		lv.sort(paircomp);
		//for (int i = 0; i < mv.size(); ++i) pr(graph[i]);
		
		Pair prev = null;
		
		for (int i = lv.size(); i > 0; --i) {
			Pair p = lv.get(i - 1);
			if (prev != null && p.equals(prev)) lv.remove(i - 1);
			prev = p;
		}
		
		ArrayList<int[]> ansa = new ArrayList<int[]>();
		for (Pair p: lv) {
			ansa.add(p.toIntArray());
		}
		
		PolygonGrid ans = new PolygonGrid(mtv, ansa);
		ans.setName(name);
		return ans;
	}
	*/

	public String getMtldir() {
		return mtldir;
	}

	private void setMtldir(String mtldir) {
		this.mtldir = mtldir;
	}
	
	public void linkMtlFile(MtlFile mtl) {
		if (mtl == null) this.linked_mtl = MtlFile.getDefault();
		else this.linked_mtl = mtl;
	}
	
	public void linkMtlFile() {
		String sm = CharResourceLoader.getInstance().getRes(new ResourceLocation(mtldir));
		if (sm == CharResourceLoader.MISSING_RES) this.linked_mtl = MtlFile.getDefault();
		else this.linked_mtl = new MtlFile(sm);
	}

	public String[] getObjectList() {
		return proc.keySet().toArray(new String[0]);
	}

}

package com.billstark001.riseui.io;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.billstark001.riseui.base.shader.BaseMaterial;
import com.billstark001.riseui.core.polygon.PolygonGrid;
import com.billstark001.riseui.core.polygon.PolygonMesh;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Pair;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;

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
	/*
	 * private static String read (String file) { StringBuffer s = new
	 * StringBuffer(); try { FileReader input = new FileReader(file); int b = 0;
	 * try { while (b != -1) { b = input.read(); s = s.append((char)b); }
	 * input.close(); } catch (IOException e) { e.printStackTrace(); } } catch
	 * (FileNotFoundException e) { System.out.println("Inexistent File!");
	 * e.printStackTrace(); } return s.toString(); }
	 */

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
					vertex.add(parseVertex(i));
				if (t.equals("vt"))
					coord.add(parseVertex(i));
				if (t.equals("vn"))
					normal.add(parseVertex(i));
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
			}
		}
		if (t_ != null)
			proc.put(t_, tpoly);
	}
	
	public PolygonMesh genMesh(String name) {
		if (!proc.containsKey(name))
			return null;
		
		ArrayList<String> orig = proc.get(name);
		ArrayList<String> matid = new ArrayList<String>();
		int face = 0;
		for (String cur : orig) {
			if (cur.startsWith("m"))
				matid.add(cur.substring(2));
			else if (cur.startsWith("f"))
				++face;
		}
		
		Map<String, Integer> mv = new LinkedHashMap<String, Integer>();
		Map<String, Integer> mn = new LinkedHashMap<String, Integer>();
		Map<String, Integer> mt = new LinkedHashMap<String, Integer>();
		int vc = 0, nc = 0, tc = 0;
		
		ArrayList<Triad> faces = new ArrayList<Triad>();
		int[] vindex = new int[face];
		BaseMaterial[] mats = new BaseMaterial[face];
		int vcount = 0;
		face = 0;

		for (String cur : orig) {
			BaseMaterial mat = null;
			if (cur.startsWith("m")) {
				mats[face] = new BaseMaterial(linked_mtl.getMat(cur.substring(2), cur.substring(2)));
			}
			if (!cur.startsWith("f")) continue;
			String[] st = cur.split(" ");
			int l = 0;
			
			for (String s: st) {
				if (s.equals("") || s.equals("f")) continue;
				++l;
				++vcount;
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
					faces.add(new Triad(mv.get(ss[0]), mt.get(ss[1]), -1));
				else
					faces.add(new Triad(mv.get(ss[0]), mt.get(ss[1]), mn.get(ss[2])));
			}
			
			vindex[face++] = vcount;
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
		
		//pr(vtv);
		//pr(vtt);
		//pr(vtn);
		Triad[] t_ = faces.toArray(new Triad[0]);
		return new PolygonMesh(mtv, mtt, mtn, t_, vindex, mats);
	}
	/*
	@Deprecated
	public PolygonMeshOld generateMesh(String name) {
		if (!proc.containsKey(name))
			return null;
		ArrayList<String> orig = proc.get(name);
		int mat = 0, face = 0;
		for (String cur : orig) {
			if (cur.startsWith("m"))
				++mat;
			else if (cur.startsWith("f"))
				++face;
		}
		String[] matid = new String[face];
		int[] faceid = new int[face];
		ArrayList<Vector> lv = new ArrayList<Vector>();
		ArrayList<Vector> ln = new ArrayList<Vector>();
		ArrayList<Vector> lu = new ArrayList<Vector>();
		ArrayList<Matrix> luc = null, lnc = null, lvc = null;
		int v_count = 0, f_count = 0;
		for (String cur : orig) {
			if (cur.startsWith("m")) {
				matid[f_count] = cur.substring(2);
			} else if (cur.startsWith("f")) {
				String[] st = cur.split(" ");
				for (int i = 1; i < st.length; ++i) {
					if (st[i].equals(""))
						continue;
					String[] ss = st[i].split("/");
					lv.add(vertex.get(Integer.valueOf(ss[0]) - 1));
					if (ss.length == 3)
						ln.add(normal.get(Integer.valueOf(ss[2]) - 1));
					lu.add(coord.get(Integer.valueOf(ss[1]) - 1));
					++v_count;
				}
				faceid[f_count] = v_count;
				++f_count;
			}
		}
		Matrix mu = new Matrix(lu.toArray(new Vector[0]));
		Matrix mv = new Matrix(lv.toArray(new Vector[0]));
		Matrix mn = null;
		if (ln.size() > 0)
			mn = new Matrix(ln.toArray(new Vector[0]));
		return new PolygonMeshOld(mv, mu, mn, faceid, matid);
	}

	public void pr(Object o) {
		System.out.println(o);
	}
	*/
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
	
	public static ArrayList<int[]> genGridEdges(Triad[] vertices, int[] findex) {
		ArrayList<Pair> lv = new ArrayList<Pair>();
		
		for (int i = 0; i < findex.length; ++i) {
			int fs = 0, fe = findex.length;
			if(i > 0 && i < findex.length) fs = findex[i - 1];
			if(i >= 0 && i < findex.length) fe = findex[i];
			Triad[] st = new Triad[fe - fs];
			for (int j = fs; j < fe; ++j) st[j - fs] = vertices[j];
			
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
		return ans;
	}

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
		String sm = ResourceLoader.getInstance().getRes(new ResourceLocation(mtldir));
		if (sm == ResourceLoader.MISSING_RES) this.linked_mtl = MtlFile.getDefault();
		else this.linked_mtl = new MtlFile(sm);
	}

	public String[] getObjectList() {
		return proc.keySet().toArray(new String[0]);
	}

}

package com.billstark001.riseui.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.fields.Operator;
import com.billstark001.riseui.base.fields.OprConstFramed;
import com.billstark001.riseui.base.fields.OprFuncSimple;
import com.billstark001.riseui.base.fields.OprGenVec3;
import com.billstark001.riseui.base.fields.OprUtils;
import com.billstark001.riseui.base.nodestate.State3DIntegrated;
import com.billstark001.riseui.base.fields.OprGen3D;
import com.billstark001.riseui.base.shading.mat.MaterialFace;
import com.billstark001.riseui.base.shading.mat.TagApplyMaterialFace;
import com.billstark001.riseui.base.shading.mat.TagSelectionHardTable;
import com.billstark001.riseui.base.shading.mat.Texture2DFromRes;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.character.Joint;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.core.empty.Light;
import com.billstark001.riseui.core.polygon.Polygon;
import com.dddviewr.collada.Accessor;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.Input;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.content.animation.Animation;
import com.dddviewr.collada.content.animation.Channel;
import com.dddviewr.collada.content.animation.LibraryAnimations;
import com.dddviewr.collada.content.animation.Sampler;
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
import com.dddviewr.collada.content.images.LibraryImages;
import com.dddviewr.collada.content.lights.LibraryLights;
import com.dddviewr.collada.content.materials.LibraryMaterials;
import com.dddviewr.collada.content.materials.Material;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.BaseXform;
import com.dddviewr.collada.content.visualscene.Rotate;
import com.dddviewr.collada.content.visualscene.Scale;
import com.dddviewr.collada.content.visualscene.Translate;
import com.dddviewr.collada.content.visualscene.VisualScene;
import com.dddviewr.collada.format.Base;

public class ColladaFile {
	
	private final Collada file;
	
	private VisualScene scene = null;
	private LibraryGeometries lgeo = null;
	private LibraryMaterials lmat = null;
	private LibraryEffects leff = null;
	private LibraryControllers lcon = null;
	private LibraryAnimations lani = null;
	private LibraryImages limg = null;
	private LibraryLights llgt = null;
	
	private Map<Integer, MaterialFace> material = null;
	private Map<String, NodeBase> parsed = new HashMap<String, NodeBase>();
	
	private Map<Integer, HashMap<String, Integer>> target_channel_sampler = new HashMap<Integer, HashMap<String, Integer>>();
	private Map<Integer, OprConstFramed<Double>> sampler_tracks = new HashMap<Integer, OprConstFramed<Double>>();
	
	public NodeBase getNodeByName(String name) {
		if (name == null) return null;
		return parsed.getOrDefault(name, null);
	}
	
	public NodeBase[] getNodes() {
		return parsed.values().toArray(new NodeBase[0]);
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
		limg = file.getLibraryImages();
		llgt = file.getLibraryLights();
		
		// Animations - DB Establishment
		
		if (lani != null) {
			for (Animation m: lani.getElements()) {
				ArrayList<Source> src_list = (ArrayList<Source>) m.getSources();
				Sampler smpl = m.getSampler();
				Channel chnl = m.getChannel();
				
				//Channel
				int channel_sampler_id = chnl.getId();
				int channel_target_id = new Base(chnl.getTarget().split("/")[0]).getId();
				String target_name = chnl.getTarget().split("/")[1];
				
				if (!target_channel_sampler.containsKey(channel_target_id)) {
					target_channel_sampler.put(channel_target_id, new HashMap<String, Integer>());
				}
				HashMap<String, Integer> chn_smpl = target_channel_sampler.get(channel_target_id);
				chn_smpl.put(target_name, channel_sampler_id);
				
				//Sampler & Sources
				int id_time = 0, id_val = 0, id_tanin = 0, id_tanout = 0, id_interp = 0;
				for (Input i: smpl.getInputs()) {
					String sem = i.getSemantic();
					int src = i.getSource();
					if (sem.equals("INPUT")) id_time = src;
					if (sem.equals("INTERPOLATION")) id_interp = src;
					if (sem.equals("IN_TANGENT")) id_tanin = src;
					if (sem.equals("OUT_TANGENT")) id_tanout = src;
					if (sem.equals("OUTPUT")) id_val = src;
				}
				
				double[] t_time = null; 
				Double[] t_val = null;
				int[] t_interp = null;
				Vector[] t_tanin = null, t_tanout = null;
				
				OprUtils.Interpolation<Double>[] t_ip = null;
				for (Source s: src_list) {
					Accessor acc = s.getAccessor();
					
					if (s.getId() == id_interp) {
						t_interp = new int[acc.getCount()];
						for (int i = 0; i < acc.getCount(); ++i) {
							String erp = s.getNameArray().getData()[i];
							if (erp.equals("BEZIER")) t_interp[i] = 2;
							else if (erp.equals("LINEAR")) t_interp[i] = 1;
							else t_interp[i] = 0;
						}
					} else if (s.getId() == id_time) {
						t_time = new double[acc.getCount()];
						float[] t_time_t = s.getFloatArray().getData();
						for (int i = 0; i < acc.getCount(); ++i) {
							t_time[i] = t_time_t[i];
						}
					} else if (s.getId() == id_val) {
						t_val = new Double[acc.getCount()];
						float[] t_val_t = s.getFloatArray().getData();
						for (int i = 0; i < acc.getCount(); ++i) {
							t_val[i] = (double) t_val_t[i];
						}
					} else if (s.getId() == id_tanin) {
						t_tanin = new Vector[acc.getCount()];
						boolean reverse_vec = acc.getParam(0).getName().equals("Y");
						float[] t_tanin_orig = s.getFloatArray().getData();
						for (int i = 0; i < acc.getCount(); ++i) {
							int val_0, val_1;
							if (reverse_vec) {
								val_0 = 2 * i + 1;
								val_1 = 2 * i;
							} else {
								val_0 = 2 * i;
								val_1 = 2 * i + 1;
							}
							t_tanin[i] = new Vector(t_tanin_orig[val_0], t_tanin_orig[val_1]);
						}
					} else if (s.getId() == id_tanout) {
						t_tanout = new Vector[acc.getCount()];
						boolean reverse_vec = acc.getParam(0).getName().equals("Y");
						float[] t_tanout_orig = s.getFloatArray().getData();
						for (int i = 0; i < acc.getCount(); ++i) {
							int val_0, val_1;
							if (reverse_vec) {
								val_0 = 2 * i + 1;
								val_1 = 2 * i;
							} else {
								val_0 = 2 * i;
								val_1 = 2 * i + 1;
							}
							t_tanout[i] = new Vector(t_tanout_orig[val_0], t_tanout_orig[val_1]);
						}
					}
				}
				
				t_ip = new OprUtils.Interpolation[t_interp.length];
				for (int i = 0; i < t_interp.length; ++i) {
					if (t_interp[i] == 2) {
						t_ip[i] = new OprUtils.Bezier3D(t_tanin[i], t_tanout[i]); 
					} else if (t_interp[i] == 1) {
						t_ip[i] = new OprUtils.LinearD();
					} else {
						t_ip[i] = new OprUtils.Step<Double>(); 
					}
				}
				/*
				FieldFramedDouble.Interpolation[] ti = new FieldFramedDouble.Interpolation[t_interp.length];
				for (int i = 0; i < t_interp.length; ++i) {
					if (t_interp[i] == 2) {
						ti[i] = Interpolation.BEZIER3;
					} else if (t_interp[i] == 1) {
						ti[i] = Interpolation.LINEAR;
					} else {
						ti[i] = Interpolation.STEP; 
					}
				}
				*/
				//FieldFramedDouble std_tmp = new FieldFramedDouble(t_time, t_val, ti, t_tanin, t_tanout);
				OprConstFramed<Double> std_tmp = new OprConstFramed<Double>(t_time, t_val, t_ip);
				sampler_tracks.put(smpl.getId(), std_tmp);
				
			}
		} else {
			
		}
		
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
			if (dif != null) mat.setDiffuse(new Texture2DFromRes(limg.getElement(etemp.findNewParam(dif.getTexture().getTexture()).getSampler2D().getInstanceImage().getUrl()).getInitFrom()));
			if (emi != null) mat.setLight(new Texture2DFromRes(limg.getElement(etemp.findNewParam(emi.getTexture().getTexture()).getSampler2D().getInstanceImage().getUrl()).getInitFrom()));
			if (tra != null) mat.setAlpha(new Texture2DFromRes(limg.getElement(etemp.findNewParam(tra.getTexture().getTexture()).getSampler2D().getInstanceImage().getUrl()).getInitFrom()));
			if (spe != null) mat.setSpecular(new Texture2DFromRes(limg.getElement(etemp.findNewParam(spe.getTexture().getTexture()).getSampler2D().getInstanceImage().getUrl()).getInitFrom()));
			this.material.put(id, mat);
		}
		
		//leff.getElement(lmat.getElement(1).getInstanceEffect().getUrl()).dump();
		for (Node ntemp: scene.getNodes()) {
			NodeBase n = parseNode(ntemp);
			String put_name = n.getName();
			while (parsed.containsKey(put_name)) put_name = put_name + "_";
			parsed.put(put_name, n);
		}
	}
	
	public State3DIntegrated parseState(BaseXform[] xf) {
		State3DIntegrated ans = null;
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
		ans = new State3DIntegrated(p, r, s);
		return ans;
	}
	
	public double[] parseStateToArray(BaseXform[] xf) {
		double[] ans = null;
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
		ans = new double[9];
		if (p != null) {
			ans[0] = p.get(0); ans[1] = p.get(1); ans[2] = p.get(2);
		}
		if (s != null) {
			ans[6] = s.get(0); ans[7] = s.get(1); ans[8] = s.get(2);
		}
		double mult_fac = 1;//Math.PI / 180;
		ans[3] = rx * mult_fac; ans[4] = ry * mult_fac; ans[5] = rz * mult_fac;
		return ans;
	}
	
	public Polygon parseMesh(Mesh m) {
		Matrix mpos = new Matrix(m.getPositionData().parseD(4, 1));
		Matrix mtex = new Matrix(m.getTexCoordData().parseTexWithReverse(true));
		Matrix mnor = new Matrix(m.getNormalData().parseD(4, 0));

		ArrayList<Triad[]> faces = new ArrayList<Triad[]>();
		ArrayList<MaterialFace> mats = new ArrayList<MaterialFace>();
		for (Primitives pr: m.getPrimitives()) {
			PolyList prim = (PolyList) pr;
			int[][] vtemp = prim.getParsed();
			int[] face_vcounts = prim.getVcount().getAccData();
			int nr_faces = face_vcounts.length;
			int cur_face = 0;
			int nr_vertices = face_vcounts[nr_faces - 1];
			
			ArrayList<Triad> face = new ArrayList<Triad>();
			MaterialFace cur_mat = this.material.get(prim.getMaterial());
			
			for (int i = 0; i < nr_vertices + 1; ++i) {
				if (face_vcounts[cur_face] == i) { // triad[], mat
					// Triad[]
					faces.add(face.toArray(new Triad[0]));
					face = new ArrayList<Triad>();
					// Mat
					if (cur_face == 0) mats.add(cur_mat);
					else mats.add(null);
					
					cur_face += 1;
				}
				
				if (i >= nr_vertices) continue;
				
				face.add(new Triad(vtemp[i][0], vtemp[i][2], vtemp[i][1]));
				
			}
			
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
			ans.addTag(sel);
			ans.addTag(new TagApplyMaterialFace(mat, sel));
		}
		
		return ans;
	}
	
	public OprGen3D parseStateTrack(double[] static_state, int id) {
		HashMap<String, Integer> map_channel_sampler = target_channel_sampler.get(id);
		if (map_channel_sampler == null) return null;
		else {
			OprGenVec3 p = new OprGenVec3();
			OprGenVec3 r = new OprGenVec3();
			OprGenVec3 s = new OprGenVec3();
			OprConstFramed<Double> std_tmp = null;
			
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("translate.X", 0));
			if (std_tmp != null) p.setX(std_tmp); else p.setX(new OprConstFramed<Double>(static_state[0]));
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("translate.Y", 0));
			if (std_tmp != null) p.setY(std_tmp); else p.setY(new OprConstFramed<Double>(static_state[1]));
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("translate.Z", 0));
			if (std_tmp != null) p.setZ(std_tmp); else p.setZ(new OprConstFramed<Double>(static_state[2]));
		
			double mult_fac = 1;//Math.PI / 180;
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("rotateX.ANGLE", 0));
			if (std_tmp != null) r.setX(std_tmp); else r.setX(new OprConstFramed<Double>(static_state[3] * mult_fac));
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("rotateY.ANGLE", 0));
			if (std_tmp != null) r.setY(std_tmp); else r.setY(new OprConstFramed<Double>(static_state[4] * mult_fac));
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("rotateZ.ANGLE", 0));
			if (std_tmp != null) r.setZ(std_tmp); else r.setZ(new OprConstFramed<Double>(static_state[5] * mult_fac));
			
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("scale.X", 0));
			if (std_tmp != null) s.setX(std_tmp); else s.setX(new OprConstFramed<Double>(static_state[6]));
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("scale.Y", 0));
			if (std_tmp != null) s.setY(std_tmp); else s.setY(new OprConstFramed<Double>(static_state[7]));
			std_tmp = sampler_tracks.get(map_channel_sampler.getOrDefault("scale.Z", 0));
			if (std_tmp != null) s.setZ(std_tmp); else s.setZ(new OprConstFramed<Double>(static_state[8]));
			
			//return new Track3DIntegrated(
			//		new Track3DPos(p), 
			//		new Track3DRotEuler(r), 
			//		new Track3DScl(s)
			//		);
			Operator<Vector> rr = new OprFuncSimple<Vector, Vector>(r, OprUtils.GEN_ROTZOOM);
			return new OprGen3D(
					new OprFuncSimple<Matrix, Vector>(p, OprUtils.GEN_POS), 
					//new Track3DRotEuler(r), 
					new OprFuncSimple<Matrix, Vector>(rr, OprUtils.GEN_ROT), 
					new OprFuncSimple<Matrix, Vector>(s, OprUtils.GEN_SCL)
					);
			 
		}
	}
	
	public NodeBase parseNode(Node n) {
		//System.out.println(n);
		State3DIntegrated c = parseState(n.getXforms());
		OprGen3D t = parseStateTrack(parseStateToArray(n.getXforms()), n.getId());
		NodeBase ans = null;
		if (n.isJoint()) ans = new Joint(c, n.getName());
		else if (n.getInstanceGeometry() != null && n.getInstanceGeometry().size() > 0) {
			Geometry gtemp = lgeo.getElement(n.getInstanceGeometry().get(0).getUrl());
			ans = parseMesh(gtemp.getMesh());
			ans.setName(n.getName());
			ans.setLocalState(c);
		} else if (n.isLight()) {
			Light ans_ = new Light(c, n.getName());
			com.dddviewr.collada.content.lights.Light il = llgt.getElement(n.getInstanceLight().getUrl());
			ans_.setColorRGB(il.getColor().getR(), il.getColor().getG(), il.getColor().getB());
			ans_.setAttenuation(Light.ATT_CONSTANT, il.constant_attenuation);
			ans_.setAttenuation(Light.ATT_LINEAR, il.linear_attenuation);
			ans_.setAttenuation(Light.ATT_QUADRATIC, il.quadratic_attenuation);
			ans_.setSpotCutoff(il.falloff_angle);
			ans_.setSpotExponent(il.falloff_exponent);
			ans_.setType(il.getType());
			ans = ans_;
		} else {
			ans = new EmptyNode(c, n.getName());
		}
		if (t != null && t.containsFrames()) ans.setLocalState(t);
		
		// TODO InstanceCamera & InstanceLight
		if (n.getInstanceController() != null) {
			Controller c1 = lcon.getElement(n.getInstanceController().getId());
		}
		for (Node nt: n.getChildNodes()) {
			NodeBase bnt = parseNode(nt);
			ans.addChild(bnt);
		}
		return ans;
	}

}

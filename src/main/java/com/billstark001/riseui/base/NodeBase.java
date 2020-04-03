package com.billstark001.riseui.base;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

import org.apache.commons.lang3.ArrayUtils;

import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.base.states.StateBase;
import com.billstark001.riseui.base.states.simple3d.State3DBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.base.states.simple3d.State3DPos;
import com.billstark001.riseui.base.states.simple3d.State3DRot;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.base.states.tracked3d.Track3DBase;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.empty.NodeEmpty;
import com.billstark001.riseui.render.GlHelper;

import scala.actors.threadpool.Arrays;

public abstract class NodeBase extends BaseObject{
	
	protected StateBase<Matrix> local_state;
	protected State3DSimple global_state; 
	// 4*4 homogeneous matrix
	// V'=V*M
	// computation order: M=S*R*P
	// SVD order: M=R1*S*R2*P
	
	@Override
	public boolean setFrameTime(double ftime) {
		boolean flag = super.setFrameTime(ftime);
		this.markGlobalDirty();
		return flag;
	}
	
	public void setChildrenFrameTime(double ftime) {
		this.setFrameTime(ftime);
		for (TagBase t: this.tags) t.setFrameTime(ftime);
		for (NodeBase n: this.children) n.setChildrenFrameTime(ftime);
	}

	protected List<TagBase> tags = new ArrayList<TagBase>();
	protected List<NodeBase> children = new ArrayList<NodeBase>();
	protected NodeBase parent = null;
	
	protected boolean global_dirty;
	
	public static enum Visibility {
		DEFAULT,
		TRUE,
		FALSE;
	}
	
	private Visibility vis_vert = Visibility.DEFAULT, vis_edge = Visibility.DEFAULT, vis_face = Visibility.DEFAULT;
	public static final Visibility VIS_VERT_DEFAULT = Visibility.FALSE;
	public static final Visibility VIS_EDGE_DEFAULT = Visibility.FALSE;
	public static final Visibility VIS_FACE_DEFAULT = Visibility.TRUE;
	public void setVisVert(Visibility vis) {if (vis == null) vis = Visibility.DEFAULT; this.vis_vert = vis;}
	public void setVisEdge(Visibility vis) {if (vis == null) vis = Visibility.DEFAULT; this.vis_edge = vis;}
	public void setVisFace(Visibility vis) {if (vis == null) vis = Visibility.DEFAULT; this.vis_face = vis;}
	public Visibility getVisVert() {return this.vis_vert;}
	public Visibility getVisEdge() {return this.vis_edge;}
	public Visibility getVisFace() {return this.vis_face;}
	public boolean vertsVisible() {
		if (this.getVisVert() != Visibility.DEFAULT) return this.getVisVert() == Visibility.TRUE;
		else if (this.parent == null) return VIS_VERT_DEFAULT == Visibility.TRUE;
		else return this.parent.vertsVisible();
	}
	public boolean edgesVisible() {
		if (this.getVisEdge() != Visibility.DEFAULT) return this.getVisEdge() == Visibility.TRUE;
		else if (this.parent == null) return VIS_EDGE_DEFAULT == Visibility.TRUE;
		else return this.parent.edgesVisible();
	}
	public boolean facesVisible() {
		if (this.getVisFace() != Visibility.DEFAULT) return this.getVisFace() == Visibility.TRUE;
		else if (this.parent == null) return VIS_FACE_DEFAULT == Visibility.TRUE;
		else return this.parent.facesVisible();
	}
	
	public static final State3DSimple STATE_STANDARD = State3DSimple.DEFAULT_STATE;

	public NodeBase(State3DIntegrated state, String name) {
		super(name);
		global_dirty = false;
		this.local_state = new State3DIntegrated(state);
		this.global_state = new State3DIntegrated(state); // new ComplexState((State3DIntegrated) this.local_state, BaseNode.STATE_STANDARD);
		this.parent = null;
	}
	
	public NodeBase(State3DSimple state, String name) {
		super(name);
		global_dirty = false;
		this.local_state = new State3DSimple(state);
		this.global_state = new State3DSimple(state); // new ComplexState(this.local_state, BaseNode.STATE_STANDARD);
		this.parent = null;
	}
	
	public NodeBase(Vector pos, Quaternion rot, Vector scl, String name) {
		super(name);
		global_dirty = false;
		this.local_state = new State3DIntegrated(pos, rot, scl);
		this.global_state = new State3DIntegrated(pos, rot, scl); // new ComplexState((State3DIntegrated) this.local_state, BaseNode.STATE_STANDARD);//Utils.getStateMat(pos, rot, scl);
		this.parent = null;
	}
	
	public NodeBase(Vector pos, Quaternion rot, Vector scl) {this(pos, rot, scl, DEFAULT_NAME);}
	public NodeBase(Vector pos, Quaternion rot, double scl) {this(pos, rot, new Vector(scl, scl, scl), DEFAULT_NAME);}
	public NodeBase(Vector pos, Vector rot, Vector scl) {this(pos, Quaternion.eulerToQuat(rot), scl, DEFAULT_NAME);}
	public NodeBase(Vector pos, Quaternion rot) {this(pos, rot, null, DEFAULT_NAME);}
	public NodeBase(Vector pos) {this(pos, new State3DRot().getDefaultRepr(), null, DEFAULT_NAME);}
	public NodeBase() {this(null, new State3DRot().getDefaultRepr(), null, DEFAULT_NAME);}
	
	public NodeBase(Vector pos, Quaternion rot, double scl, String name) {this(pos, rot, new Vector(scl, scl, scl), name);}
	public NodeBase(Vector pos, Vector rot, Vector scl, String name) {this(pos, Quaternion.eulerToQuat(rot), scl, name);}
	public NodeBase(Vector pos, Quaternion rot, String name) {this(pos, rot, null, name);}
	public NodeBase(Vector pos, String name) {this(pos, new State3DRot().getDefaultRepr(), null, name);}
	public NodeBase(String name) {this(null, new State3DRot().getDefaultRepr(), null, name);}
	
	public NodeBase getParent() {return parent;}
	public NodeBase[] getChildren() {return children.toArray(new NodeBase[0]);}
	public NodeBase getChild(int index) {
		try {
			return this.children.get(index);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	public NodeBase getChild(String name) {
		NodeBase ans = null;
		for (NodeBase obj: children) {
			if (obj.getName().equals(name)) {
				ans = obj;
				break;
			}
		}
		return ans;
	}
	
	private class TreeIteratorDF implements Iterator<NodeBase> {
		
		private Stack<NodeBase> nodes;
		
		public TreeIteratorDF(NodeBase root) {
			this.nodes = new Stack<NodeBase>();
			this.nodes.push(root);
		}
		
		@Override
		public boolean hasNext() {
			return !nodes.empty();
		}

		@Override
		public NodeBase next() {
			if (!this.hasNext()) throw new NoSuchElementException();
			NodeBase current_node = nodes.pop();
			for (NodeBase node: current_node.getChildren()) this.nodes.push(node);
			return current_node;
		}
		
	}
	
	private class TreeIteratorBF implements Iterator<NodeBase> {
		
		private Queue<NodeBase> nodes;
		
		public TreeIteratorBF(NodeBase root) {
			this.nodes = new LinkedList<NodeBase>();
			this.nodes.add(root);
		}
		
		@Override
		public boolean hasNext() {
			return !nodes.isEmpty();
		}

		@Override
		public NodeBase next() {
			if (!this.hasNext()) throw new NoSuchElementException();
			NodeBase current_node = nodes.poll();
			for (NodeBase node: current_node.getChildren()) this.nodes.add(node);
			return current_node;
		}
		
	}
	
	public TreeIteratorDF getTreeIteratorDepthFirst() {
		return new TreeIteratorDF(this);
	}
	
	public TreeIteratorBF getTreeIteratorBreadthFirst() {
		return new TreeIteratorBF(this);
	}
	
	public boolean isDescendant(NodeBase obj) {return obj.isAncestor(this);}
	public boolean isAncestor(NodeBase obj) {
		boolean flag = false;
		if (obj == null) return flag;
		while (!flag) {
			obj = obj.getParent();
			if (obj == this) {
				flag = true;
				break;
			}
			if (obj == null) break;
		}
		return flag;
	}

	
	// Node tree operations with local world states remain
	
	public boolean addChild(NodeBase obj) {return obj.setParent(this);}
	public boolean setParent(NodeBase parent) {
		if (parent == null) return removeParent();
		if (parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (this.parent != null && this.parent.children.contains(this)) {
			this.parent.children.remove(this);
			//updateLocalState();
		}
		this.parent = parent;
		if (!parent.children.contains(this)) parent.children.add(this);
		markGlobalDirty();
		//updateGlobalStateCheckDirty();
		return true;
	}
	public boolean removeParent() {
		if (this.parent == null) return false;
		if (this.parent.children.contains(this)) this.parent.children.remove(this);
		this.parent = null;
		markGlobalDirty();
		return true;
	}
	public boolean removeChild(String name) {return removeChild(getChild(name));}
	public boolean removeChild(int index) {return removeChild(getChild(index));}
	public boolean removeChild(NodeBase obj) {
		if(obj == null) return false;
		if (children.contains(obj)) {
			obj.removeParent();
			return true;
		}
		return false;
	}
	public boolean removeAllChildren(String name) {
		while (children.size() > 0) {
			NodeBase cache = children.get(0);
			if (cache.getName().equals(name))
				removeChild(cache);
		}
		return true;
	}
	public boolean removeAllChildren() {
		while (children.size() > 0) {
			removeChild(0);
		}
		return true;
	}

	// Node tree operations with local world states remain
	
	public boolean addChildRemainGlobalState(NodeBase obj) {return obj.setParentRemainGlobalState(this);}
	public boolean setParentRemainGlobalState(NodeBase parent) {
		if (parent == null) return removeParentRemainGlobalState();
		if (parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (this.parent != null && this.parent.children.contains(this)) this.parent.children.remove(this);
		updateGlobalStateCheckDirty();
		this.parent = parent;
		if (!parent.children.contains(this)) parent.children.add(this);
		updateLocalState();
		return true;
	}
	public boolean removeParentRemainGlobalState() {
		if (this.parent == null) return false;
		if (this.parent.children.contains(this)) this.parent.children.remove(this);
		updateGlobalStateCheckDirty();
		this.parent = null;
		updateLocalState();
		return true;
	}
	public boolean removeChildRemainGlobalState(String name) {return removeChildRemainGlobalState(getChild(name));}
	public boolean removeChildRemainGlobalState(int index) {return removeChildRemainGlobalState(getChild(index));}
	public boolean removeChildRemainGlobalState(NodeBase obj) {
		if(obj == null) return false;
		if (children.contains(obj)) {
			obj.removeParentRemainGlobalState();
			return true;
		}
		return false;
	}
	public boolean removeAllChildrenRemainGlobalState(String name) {
		while (children.size() > 0) {
			NodeBase cache = children.get(0);
			if (cache.getName().equals(name))
			 removeChildRemainGlobalState(cache);
		}
		return true;
	}
	public boolean removeAllChildrenRemainGlobalState() {
		while (children.size() > 0) {
			removeChildRemainGlobalState(0);
		}
		return true;
	}

	
	// Information Maintaining
	
	public boolean isGlobalDirty() {return global_dirty;}
	public void markGlobalDirty() {
		this.global_dirty = true;
		for (NodeBase obj: children) obj.markGlobalDirty();
	}
	public void clarifyGlobal() {this.global_dirty = false;}
	
	/**
	 * Called while a new global state was assigned.
	 * S_L = S_G * S_PG^-1
	 */
	protected void updateLocalState() {this.updateLocalStateWithoutTag(); this.applyTags(TagBase.TAG_PHRASE_LOCAL_UPDATE);}
	protected void updateLocalStateWithoutTag() {
		State3DSimple A = this.getParentGlobalState();
		State3DSimple C = this.getGlobalState();
		State3DSimple B = State3DBase.stateDecomposeB(C, A);
		this.local_state = B;
	}
	
	protected void updateGlobalStateCheckDirty() {if (isGlobalDirty()) updateGlobalState();}
	protected void updateGlobalStateCheckDirtyWithoutTag() {if (isGlobalDirty()) updateGlobalStateWithoutTag();}
	protected void updateGlobalState() {this.updateGlobalStateWithoutTag(); this.applyTags(TagBase.TAG_PHRASE_GLOBAL_UPDATE);}
	protected void updateGlobalStateWithoutTag() {
		NodeBase parent = new NodeEmpty();
		if (this.parent != null) {
			this.parent.updateGlobalStateCheckDirty();
			parent = this.parent;
		}
		this.global_state = State3DBase.stateCompose(this.getLocalState(), parent.getGlobalState());
	}
	//Local Info. Getter & Setter
	
	public boolean isLocalStateTracked() {
		return this.local_state.isTracked();
	}

	public State3DBase getLocalState() {
		if (!this.isLocalStateTracked()) return ((State3DBase) this.local_state);
		else return ((Track3DBase) this.local_state).getSimpleState(this.getFrameTime());
	}
	public StateBase<Matrix> getLocalStateRaw() {
		return this.local_state;
	}
	public boolean isLocalStateStandardized() {return (this.local_state instanceof State3DIntegrated);}
	public State3DSimple getLocalStateStandardized() {
		if (this.local_state instanceof State3DIntegrated)
			return (State3DIntegrated) this.local_state;
		else return null;
	}
	
	public void setLocalState(StateBase<Matrix> state) {
		this.local_state = state;
		markGlobalDirty();
	}
	
	public Vector getLocalPos() {
		State3DBase state_temp = this.getLocalState();
		if (state_temp instanceof State3DIntegrated) return ((State3DIntegrated) state_temp).getPos();
		else if (state_temp instanceof State3DPos) return ((State3DPos) state_temp).getStateRepr();
		else return state_temp.get().getLine(4).get(0, 3);
	}

	// Global Info. Getter & Setter

	public State3DSimple getGlobalState() {this.updateGlobalStateCheckDirty(); return this.global_state;}
	public State3DSimple getParentGlobalState() {
		if (this.getParent() == null) return State3DSimple.DEFAULT_STATE;
		else return this.getParent().getGlobalState();
	}
	
	public Vector getGlobalPos() {
		return this.global_state.get().getLine(4).get(0, 3);
	}

	public boolean addTag(TagBase tag) {
		boolean flag = (!tag.appliesOn(TagBase.TAG_PHRASE_ADDED)) || tag.onAdded(this).succeeded;
		if (flag) {
			tags.add(tag);
			state_tag_cache = new HashMap<Integer, TagBase[]>();
		}
		return flag;
	}
	
	public boolean removeTag(TagBase tag) {
		boolean flag = (!tag.appliesOn(TagBase.TAG_PHRASE_REMOVED)) || (tag.onRemoved(this).succeeded && tags.contains(tag));
		if (flag) {
			tags.remove(tag);
			state_tag_cache = new HashMap<Integer, TagBase[]>();
		}
		return flag;
	}
	
	public void applyTags(int phrase, Object...extra) {
		TagBase.sortTags(tags);
		TagBase t;
		for (int i = 0; i < tags.size(); ++i) {
			t = tags.get(i);
			if (t.isActivated() && t.appliesOn(phrase)) t.applyOn(phrase, this, extra);
		}
	}
	
	public void applyTagsWithExclusion(int phrase, TagBase[] excluded, Object...extra) {
		TagBase.sortTags(tags);
		TagBase t;
		for (int i = 0; i < tags.size(); ++i) {
			t = tags.get(i);
			if (t.isActivated() && t.appliesOn(phrase) && !ArrayUtils.contains(excluded, t)) t.applyOn(phrase, this, extra);
		}
	}
	
	public TagBase[] findTagsRaw(int phrase) {
		ArrayList<TagBase> tags_ans = new ArrayList<TagBase>();
		TagBase.sortTags(this.tags);
		TagBase t;
		for (int i = 0; i < this.tags.size(); ++i) {
			t = this.tags.get(i);
			if (t.appliesOn(phrase)) tags_ans.add(t);
		}
		return tags_ans.toArray(new TagBase[0]);
	}
	
	private Map<Integer, TagBase[]> state_tag_cache = new HashMap<Integer, TagBase[]>();
	
	public TagBase[] findTags(int phrase) {
		if (state_tag_cache == null) state_tag_cache = new HashMap<Integer, TagBase[]>();
		TagBase[] ans = state_tag_cache.getOrDefault(phrase, null);
		if (ans == null) {
			ans = findTagsRaw(phrase);
			state_tag_cache.put(phrase, ans);
		}
		return ans;
	}
	
	public TagBase[] findTags(int[] phrases) {
		ArrayList<TagBase> tags_ans = new ArrayList<TagBase>();
		TagBase.sortTags(this.tags);
		TagBase t;
		for (int i = 0; i < this.tags.size(); ++i) {
			t = this.tags.get(i);
			boolean flag = t.isActivated();
			for (int phrase: phrases) flag = flag && t.appliesOn(phrase);
			if (flag) tags_ans.add(t);
		}
		return tags_ans.toArray(new TagBase[0]);
	}
	
	// Render
	
	public void render(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		// renderer.dumpState();
		this.applyTags(TagBase.TAG_PHRASE_RENDER_PRE, ptick);
		if (this.vertsVisible()) {
			//renderer.setVertState();
			this.renderVert(ptick);
		}
		if (this.edgesVisible()) {
			//renderer.setEdgeState();
			this.renderEdge(ptick);
		}
		if (this.facesVisible()) {
			//renderer.setFaceState();
			this.renderFace(ptick);
		}
		if (renderer.isDebugging()) this.renderDebug(ptick);
		this.applyTags(TagBase.TAG_PHRASE_RENDER_POST, ptick);
		// renderer.resetState();
	}
	
	// Abstract Methods
	/**
	 * Returns the number of vertices of this Node.
	 * @return int, the number
	 */
	public abstract int getVertCount();
	/**
	 * Returns the number of edges of this Node.
	 * @return int, the number
	 */
	public abstract int getEdgeCount();
	/**
	 * Returns the number of faces of this Node.
	 * @return int, the number
	 */
	public abstract int getFaceCount();

	public abstract Vector getVertPos(int index);
	public abstract Vector getVertNrm(int index);
	public abstract Vector getVertUVM(int index);
	
	public abstract boolean isEdgeLooped(int index);
	public abstract int[] getEdgeIndices(int index);
	public abstract Triad[] getFaceIndices(int index);
	
	public abstract int getEdgeIndicesLength(int index);
	public abstract int getFaceIndicesLength(int index);
	
	public Vector[] getEdges(int index) {
		int[] indices = this.getEdgeIndices(index);
		Vector[] ans = new Vector[indices.length];
		for (int i = 0; i < ans.length; ++i) ans[i] = getVertPos(indices[i]);
		return ans;
	}
	
	public void renderVert(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_PRE, ptick);
		for (int i = 0; i < this.getVertCount(); ++i) {
			this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_VERTEX, i, ptick);
			Vector v = this.getVertPos(i);
			renderer.startDrawingVert();
			renderer.addVertex(v);
			renderer.endDrawing();
		}
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_POST, ptick);
	}
	public void renderEdge(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		this.applyTags(TagBase.TAG_PHRASE_RENDER_EDGES_PRE, ptick);
		for (int i = 0; i < this.getEdgeCount(); ++i) {
			this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_EDGE, i, ptick);
			int[] v_ = this.getEdgeIndices(i);
			renderer.startDrawingEdge(this.isEdgeLooped(i));
			for (int v: v_) {
				renderer.addVertex(this.getVertPos(v));
			}
			renderer.endDrawing();
		}
		this.applyTags(TagBase.TAG_PHRASE_RENDER_EDGES_POST, ptick);
	}
	/**
	 * TODO
	 * 
	 * Face:
	 * orig_points: (vec3, vec2)[]
	 * mats: (shader[], double)[]
	 * 
	 * Faces Categorized by Materials:
	 * deformed_points: (vec3[], vec2[])[k]
	 * mats:(shader[], double)[k]
	 * for i in range(k): load mats, render points.
	 * 
	 * pseudo code:
	 * build a selection record, calc the ultimate selections of each face
	 * categorize selection to several groups, for each group calc materials
	 * for each gruop render by materials
	 * 
	 * Extendability Disaster!!!!!
	 */
	public void renderFace(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		
		int face_count = this.getFaceCount();
		TagBase[] rpf_tags = this.findTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE);
		double[][] final_apply_rate;
		
		final_apply_rate = new double[face_count][rpf_tags.length];
		for (int i = 0; i < face_count; ++i) {
			for (int j = 0; j < rpf_tags.length; ++j) {
				if (!rpf_tags[j].isActivated()) continue;
				TagBase.ApplyReturn ret = rpf_tags[j].applyOn(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, this, i, ptick, false);
				if (ret.succeeded) {
					double rate = (Double) ret.data[0];
					if (rate > (1 - 1e-6)) rate = 1;
					else if (rate < 1e-6) rate = 0;
					final_apply_rate[i][j] = rate;
					for (int k = 0; k < j; ++k) final_apply_rate[i][k] *= (1 - rate);
				}
			}
		}
		
		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_PRE, rpf_tags, ptick, 0);
		
		for (int it = 0; it < rpf_tags.length; ++it) {
			TagBase t = rpf_tags[it];
			if (!t.isActivated()) continue; 
			int rerender_count = (Integer) (t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, -1).data[0]);
			for (int ir = 0; ir < rerender_count; ++ir) {
				boolean succeeded = t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, ir).succeeded;
				if (!succeeded) continue;
				for (int is = 0; is < face_count; ++is) {
					if (final_apply_rate[is][it] < 1e-6) continue;
					TagBase.ApplyReturn ret = t.applyOn(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, this, is, ptick, true);
					
					Triad[] t_ = this.getFaceIndices(is);
					
					renderer.startDrawingFace();
					for (Triad t1 : t_) {
						Vector v1, v2;
						v1 = this.getVertPos(t1.getX());
						v2 = this.getVertUVM(t1.getY());
						renderer.addVertex(v1, v2);
					}
					renderer.endDrawing();
				}
			}
			t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_POST, this, ptick);
		}

		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_POST, rpf_tags, ptick);
	}
	public void renderFace_(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		this.applyTags(TagBase.TAG_PHRASE_RENDER_FACES_PRE, ptick, 0);
		for (int i = 0; i < this.getFaceCount(); ++i) {
			this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, i, ptick, true);
			Triad[] t_ = this.getFaceIndices(i);
			
			renderer.startDrawingFace();
			for (Triad t : t_) {
				Vector v1, v2;
				v1 = this.getVertPos(t.getX());
				v2 = this.getVertUVM(t.getY());
				renderer.addVertex(v1, v2);
			}
			renderer.endDrawing();
		}
		this.applyTags(TagBase.TAG_PHRASE_RENDER_FACES_POST, ptick);
	}
	
	public void renderDebug(double ptick) {
		
		Shader.SHADER_DEBUG.applyState();
		
		GlHelper renderer = GlHelper.getInstance();
		renderer.setLineWidth(4);
		renderer.setAlpha(1.);
		
		/*
		Matrix mtemp = new Matrix(vtemp);
		Vector iscl = new Vector(1 / wscl.get(0), 1 / wscl.get(1), 1 / wscl.get(2));
		mtemp = Utils.zoom(mtemp, iscl);
		mtemp = Utils.rotate(mtemp, rot);
		mtemp = Utils.offset(mtemp, pos);
		vtemp = mtemp.toVecArray();
		*/
		
		Vector[] vtemp = Matrix.I4.toVecArray();
		//Vector[] vtemp = this.getLocalState().getState().toVecArray();
		
		renderer.setColor(255, 0, 0);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[0].add(vtemp[3]));
		renderer.endDrawing();
		
		renderer.setColor(0, 255, 0);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[1].add(vtemp[3]));
		renderer.endDrawing();
		
		renderer.setColor(0, 0, 255);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[2].add(vtemp[3]));
		renderer.endDrawing();
		
		renderer.setColor(255, 0, 255);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[3].subtract(vtemp[0].mult(0.5)));
		renderer.endDrawing();
		
		renderer.setColor(255, 255, 0);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[3].subtract(vtemp[1].mult(0.5)));
		renderer.endDrawing();
		
		renderer.setColor(0, 255, 255);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[3].subtract(vtemp[2].mult(0.5)));
		renderer.endDrawing();
		
		Shader.SHADER_DIFFUSE.applyState();
		
	}

	// Display Functions
	
	@Override
	public String toString() {
		String ans = "%s STATE:%s";
		ans = String.format(ans, super.toString(), this.getLocalState().toString());
		return ans;
	}
	
	protected String genBlank(int count) {
		char[] temp = new char[count];
		Arrays.fill(temp, ' ');
		return new String(temp);
	}
	protected void println(PrintStream out, String text, int blank) {
		out.print(genBlank(blank));
		out.println(text);
	}
	
	public String vec32String(Vector v) {return String.format("[%.3f, %.3f, %.3f]", v.get(0), v.get(1), v.get(2));}
	public String quat2String(Quaternion q) {
		return vec32String(Quaternion.quatToEuler(q).mult(180 / Math.PI));
	}
	
	public void dump() {dump(System.out, 0);}
	public void dump(PrintStream out) {dump(out, 0);}
	public void dump(PrintStream out, int level){
		println(out, super.toString(), level); 
		println(out, String.format("STATE_LOCAL%s: %s", (this.getLocalState() instanceof State3DIntegrated ? "*" : " "), this.getLocalState()), level + 1);
		println(out, "STATE_GLOBAL: " + this.getGlobalState(), level + 1);
		if (this.children.size() == 0) return;
		//println(out, "", level + 1);
		println(out, "CHILDREN:", level + 1);
		for (NodeBase i: this.children) {
			i.dump(out, level + 2);
		}
	}

}

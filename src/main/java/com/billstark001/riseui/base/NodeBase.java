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

import com.billstark001.riseui.base.TagBase.ApplyReturn;
import com.billstark001.riseui.base.fields.Field;
import com.billstark001.riseui.base.fields.FieldGen3D;
import com.billstark001.riseui.base.nodestate.State3DBase;
import com.billstark001.riseui.base.nodestate.State3DIntegrated;
import com.billstark001.riseui.base.nodestate.State3DPos;
import com.billstark001.riseui.base.nodestate.State3DRot;
import com.billstark001.riseui.base.nodestate.State3DSimple;
import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.render.GlHelper;

import scala.actors.threadpool.Arrays;

public abstract class NodeBase extends BaseContainer{
	
	protected Field<Matrix> local_state;
	protected State3DBase global_state; 
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
		NodeBase parent = new EmptyNode();
		if (this.parent != null) {
			this.parent.updateGlobalStateCheckDirty();
			parent = this.parent;
		}
		this.global_state = State3DBase.stateCompose(this.getLocalState(), parent.getGlobalState());
	}
	// Local Info. Getter & Setter
	
	public boolean isLocalStateTracked() {
		return this.local_state.isTracked();
	}

	public State3DBase getLocalState() {
		if (!this.isLocalStateTracked()) return ((State3DBase) this.local_state);
		else return ((FieldGen3D) this.local_state).getSimpleState(this.getFrameTime());
	}
	public Field<Matrix> getLocalStateRaw() {
		return this.local_state;
	}
	public boolean isLocalStateStandardized() {return (this.local_state instanceof State3DIntegrated);}
	public State3DSimple getLocalStateStandardized() {
		if (this.local_state instanceof State3DIntegrated)
			return (State3DIntegrated) this.local_state;
		else return null;
	}
	
	public void setLocalState(Field<Matrix> state) {
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

	public State3DSimple getGlobalState() {this.updateGlobalStateCheckDirty(); return (State3DSimple) this.global_state;}
	public State3DSimple getParentGlobalState() {
		if (this.getParent() == null) return State3DSimple.DEFAULT_STATE;
		else return this.getParent().getGlobalState();
	}
	
	public Vector getGlobalPos() {
		return this.global_state.get().getLine(3).get(0, 3);
	}

	// Tags
	
	public boolean addTag(TagBase tag) {
		boolean flag = (!tag.appliesOn(TagBase.TAG_PHRASE_ADDED)) || tag.onAdded(this).succeeded;
		if (flag) {
			tags.add(tag);
			this.onTagsAltered();
		}
		return flag;
	}
	
	public boolean removeTag(TagBase tag) {
		boolean flag = (!tag.appliesOn(TagBase.TAG_PHRASE_REMOVED)) || (tag.onRemoved(this).succeeded && tags.contains(tag));
		if (flag) {
			tags.remove(tag);
			this.onTagsAltered();
		}
		return flag;
	}
	
	public List<ApplyReturn> applyTags(int phrase, Object...extra) {
		TagBase.sortTags(tags);
		TagBase t;
		List<ApplyReturn> ans = new ArrayList<ApplyReturn>();
		for (int i = 0; i < tags.size(); ++i) {
			t = tags.get(i);
			if (t.isActivated() && t.appliesOn(phrase)) ans.add(t.applyOn(phrase, this, extra));
		}
		return ans;
	}
	
	public List<ApplyReturn> applyTagsWithExclusion(int phrase, TagBase[] excluded, Object...extra) {
		TagBase.sortTags(tags);
		TagBase t;
		List<ApplyReturn> ans = new ArrayList<ApplyReturn>();
		for (int i = 0; i < tags.size(); ++i) {
			t = tags.get(i);
			if (t.isActivated() && t.appliesOn(phrase) && !ArrayUtils.contains(excluded, t))
				ans.add(t.applyOn(phrase, this, extra));
		}
		return ans;
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
	
	protected void onTagsAltered() {
		state_tag_cache = new HashMap<Integer, TagBase[]>();
	}
	
	// Render
	public void onRender(double ptick, boolean reverse_normal) {}
	public void render(double ptick) {this.render(ptick, false);}
	public void render(double ptick, boolean reverse_normal) {
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
			this.renderFace(ptick, reverse_normal);
		}
		this.onRender(ptick, reverse_normal);
		//if (renderer.isDebugging()) this.renderDebug(ptick);
		this.applyTags(TagBase.TAG_PHRASE_RENDER_POST, ptick);
		// renderer.resetState();
	}
	
	public abstract void renderVert(double ptick);
	public abstract void renderEdge(double ptick);
	public abstract void renderFace(double ptick, boolean reverse_normal);
	
	public void renderFace(double ptick) {this.renderFace(ptick, false);}
	
	public void renderDebug(double ptick) {
		
		Shader.SHADER_DEBUG.applyState();
		
		GlHelper renderer = GlHelper.getInstance();
		renderer.setLineWidth(2);
		renderer.setAlpha(1.);
		
		/*
		Matrix mtemp = new Matrix(vtemp);
		Vector iscl = new Vector(1 / wscl.get(0), 1 / wscl.get(1), 1 / wscl.get(2));
		mtemp = Utils.zoom(mtemp, iscl);
		mtemp = Utils.rotate(mtemp, rot);
		mtemp = Utils.offset(mtemp, pos);
		vtemp = mtemp.toVecArray();
		*/
		
		State3DIntegrated stmp = this.getGlobalState().decomp();
		Matrix axis = Matrix.I4.mult(0.25).mult(Utils3D.sclToHomoState(stmp.getScl().power(-1).add(stmp.getScl().log(10).mult(0.25)))); //
		
		Vector[] vtemp = axis.toVecArray();
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
		renderer.addVertex(vtemp[3].subtract(vtemp[0].mult(0.25)));
		renderer.endDrawing();
		
		renderer.setColor(255, 255, 0);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[3].subtract(vtemp[1].mult(0.25)));
		renderer.endDrawing();
		
		renderer.setColor(0, 255, 255);
		renderer.startDrawingEdge(false);
		renderer.addVertex(vtemp[3]);
		renderer.addVertex(vtemp[3].subtract(vtemp[2].mult(0.25)));
		renderer.endDrawing();
		
		//Shader.SHADER_DIFFUSE.applyState();
		
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
	
	protected String getExtraDumpInfo() {
		return null;
	}
	
	public void dump() {dump(System.out, 0);}
	public void dump(PrintStream out) {dump(out, 0);}
	public void dump(PrintStream out, int level){
		println(out, super.toString(), level); 
		println(out, String.format("STATE_LOCAL%s: %s", (this.getLocalState() instanceof State3DIntegrated ? "*" : " "), this.getLocalState()), level + 1);
		println(out, "STATE_GLOBAL: " + this.getGlobalState(), level + 1);
		String extra = this.getExtraDumpInfo();
		if (extra != null && extra.length() > 0) {
			String[] extra_ = extra.split("\n");
			for (String e: extra_)
				println(out, e, level + 1);
		}
		if (this.children.size() == 0) return;
		//println(out, "", level + 1);
		println(out, "CHILDREN:", level + 1);
		for (NodeBase i: this.children) {
			i.dump(out, level + 2);
		}
	}

}

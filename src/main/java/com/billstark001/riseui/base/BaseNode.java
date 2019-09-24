package com.billstark001.riseui.base;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.base.shader.BaseMaterial;
import com.billstark001.riseui.base.state.ComplexState;
import com.billstark001.riseui.base.state.SimpleState;
import com.billstark001.riseui.base.state.State4;
import com.billstark001.riseui.base.state.StateRot;
import com.billstark001.riseui.base.state.StateStandard3D;
import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Triad;
import com.billstark001.riseui.math.Vector;

import scala.actors.threadpool.Arrays;

public abstract class BaseNode extends BaseObject{

	protected SimpleState local_state;
	protected ComplexState global_state; 
	// 4*4 homogeneous matrix
	// computation order: M=S*R*P
	// SVD order: M=R1*S*R2*P

	protected List<BaseTag> tags = new ArrayList<BaseTag>();
	protected List<BaseNode> children = new ArrayList<BaseNode>();
	protected BaseNode parent = null;
	
	protected boolean global_dirty;
	
	public static enum Visibility {
		DEFAULT,
		TRUE,
		FALSE;
	}
	
	private Visibility vis_vert, vis_edge, vis_face;
	public static final Visibility VIS_VERT_DEFAULT = Visibility.FALSE;
	public static final Visibility VIS_EDGE_DEFAULT = Visibility.FALSE;
	public static final Visibility VIS_FACE_DEFAULT = Visibility.TRUE;
	public void setVisVert(Visibility vis) {this.vis_vert = vis;}
	public void setVisEdge(Visibility vis) {this.vis_edge = vis;}
	public void setVisFace(Visibility vis) {this.vis_face = vis;}
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
	
	public static final SimpleState STATE_STANDARD = SimpleState.STATE_STANDARD;

	public BaseNode(StateStandard3D state, String name) {
		super(name);
		global_dirty = false;
		this.local_state = new StateStandard3D(state);
		this.global_state = new ComplexState(STATE_STANDARD, (StateStandard3D) this.local_state);
		this.parent = null;
	}
	
	public BaseNode(SimpleState state, String name) {
		super(name);
		global_dirty = false;
		this.local_state = new SimpleState(state);
		this.global_state = new ComplexState(STATE_STANDARD, this.local_state);
		this.parent = null;
	}
	
	public BaseNode(Vector pos, Quaternion rot, Vector scl, String name) {
		super(name);
		global_dirty = false;
		this.local_state = new StateStandard3D(pos, rot, scl);
		this.global_state = new ComplexState(STATE_STANDARD, (StateStandard3D) this.local_state);//Utils.getStateMat(pos, rot, scl);
		this.parent = null;
	}
	
	public BaseNode(Vector pos, Quaternion rot, Vector scl) {this(pos, rot, scl, DEFAULT_NAME);}
	public BaseNode(Vector pos, Quaternion rot, double scl) {this(pos, rot, new Vector(scl, scl, scl), DEFAULT_NAME);}
	public BaseNode(Vector pos, Vector rot, Vector scl) {this(pos, Quaternion.eulerToQuat(rot), scl, DEFAULT_NAME);}
	public BaseNode(Vector pos, Quaternion rot) {this(pos, rot, null, DEFAULT_NAME);}
	public BaseNode(Vector pos) {this(pos, new StateRot().getDefaultRepr(), null, DEFAULT_NAME);}
	public BaseNode() {this(null, new StateRot().getDefaultRepr(), null, DEFAULT_NAME);}
	
	public BaseNode(Vector pos, Quaternion rot, double scl, String name) {this(pos, rot, new Vector(scl, scl, scl), name);}
	public BaseNode(Vector pos, Vector rot, Vector scl, String name) {this(pos, Quaternion.eulerToQuat(rot), scl, name);}
	public BaseNode(Vector pos, Quaternion rot, String name) {this(pos, rot, null, name);}
	public BaseNode(Vector pos, String name) {this(pos, new StateRot().getDefaultRepr(), null, name);}
	public BaseNode(String name) {this(null, new StateRot().getDefaultRepr(), null, name);}
	
	public BaseNode getParent() {return parent;}
	public BaseNode[] getChildren() {return children.toArray(new BaseNode[0]);}
	public BaseNode getChild(int index) {
		try {
			return this.children.get(index);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	public BaseNode getChild(String name) {
		BaseNode ans = null;
		for (BaseNode obj: children) {
			if (obj.getName().equals(name)) {
				ans = obj;
				break;
			}
		}
		return ans;
	}
	
	// Tree Node Related
	
	public boolean isDescendant(BaseNode obj) {return obj.isAncestor(this);}
	public boolean isAncestor(BaseNode obj) {
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

	// TODO check codes
	public boolean addChild(BaseNode obj) {return obj.setParent(this);}
	public boolean setParent(BaseNode parent) {
		if (parent == null) return removeParent();
		if (parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (this.parent != null && this.parent.children.contains(this)) this.parent.children.remove(this);
		updateGlobalStateCheckDirty();
		this.parent = parent;
		if (!parent.children.contains(this)) parent.children.add(this);
		updateLocalState();
		return true;
	}
	public boolean addChildRemainLocal(BaseNode obj) {return obj.setParentRemainLocal(this);}
	public boolean setParentRemainLocal(BaseNode parent) {
		if (parent == null) return removeParent();
		if (parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (this.parent != null && this.parent.children.contains(this)) {
			this.parent.children.remove(this);
			updateLocalState();
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
		updateGlobalStateCheckDirty();
		this.parent = null;
		updateLocalState();
		return true;
	}

	public boolean removeChild(String name) {return removeChild(getChild(name));}
	public boolean removeChild(int index) {return removeChild(getChild(index));}
	public boolean removeChild(BaseNode obj) {
		if(obj == null) return false;
		if (children.contains(obj)) {
			obj.removeParent();
			return true;
		}
		return false;
	}
	public boolean removeAllChildren(String name) {
		boolean flag = false;
		while (children.size() > 0) {
			BaseNode cache = children.get(0);
			if (cache.getName().equals(name)) flag = removeChild(cache);
			// if (!flag) return false;
		}
		return true;
	}
	public boolean removeAllChildren() {
		boolean flag = false;
		while (children.size() > 0) {
			flag = removeChild(0);
			// if (!flag) return false;
		}
		return true;
	}
	
	// Information Maintaining
	
	public boolean GlobalDirty() {return global_dirty;}
	public void markGlobalDirty() {
		this.global_dirty = true;
		for (BaseNode obj: children) obj.markGlobalDirty();
	}
	public void clarifyGlobal() {this.global_dirty = false;}
	
	/**
	 * Called while a new global state was assigned.
	 * S_L = S_G * S_PG^-1
	 */
	protected void updateLocalState() {this.updateLocalStateWithoutTag(); this.applyTags(BaseTag.TAG_PHRASE_LOCAL_UPDATE);}
	protected void updateLocalStateWithoutTag() {
		SimpleState A = this.getParentGlobalStateSimplified();
		SimpleState C = this.getGlobalStateSimplified();
		SimpleState B = State4.stateDecomposeB(C, A);
		this.local_state = B;
	}
	
	protected void updateGlobalStateCheckDirty() {if (GlobalDirty()) updateGlobalState();}
	protected void updateGlobalStateCheckDirtyWithoutTag() {if (GlobalDirty()) updateGlobalStateWithoutTag();}
	protected void updateGlobalState() {this.updateGlobalStateWithoutTag(); this.applyTags(BaseTag.TAG_PHRASE_GLOBAL_UPDATE);}
	protected void updateGlobalStateWithoutTag() {
		BaseNode parent = new EmptyNode();
		if (this.parent != null) {
			this.parent.updateGlobalStateCheckDirty();
			parent = this.parent;
		}
		this.global_state = new ComplexState(this.getLocalState(), parent.getGlobalStateSimplified());
	}
	//Local Info. Getter & Setter

	public SimpleState getLocalState() {return this.local_state;}
	public boolean isLocalStateStandardized() {return (this.local_state instanceof StateStandard3D);}
	public SimpleState getLocalStateStandardized() {
		if (this.local_state instanceof StateStandard3D)
			return (StateStandard3D) this.local_state;
		else return null;
	}
	
	public void setLocalState(SimpleState state) {
		this.local_state = state;
		markGlobalDirty();
	}
	
	public Vector getLocalPos() {
		if (this.local_state instanceof StateStandard3D) return ((StateStandard3D) this.local_state).getPos();
		else return this.local_state.getState().getLine(4).get(0, 3);
	}

	// Global Info. Getter & Setter

	public ComplexState getGlobalState() {this.updateGlobalStateCheckDirty(); return this.global_state;}
	public SimpleState getGlobalStateSimplified() {this.updateGlobalStateCheckDirty(); return this.global_state.toSimpleState();}
	public ComplexState getParentGlobalState() {
		if (this.getParent() == null) return new ComplexState();
		else return this.getParent().getGlobalState();
	}
	public SimpleState getParentGlobalStateSimplified() {return getParentGlobalState().toSimpleState();}
	
	public Vector getGlobalPos() {
		return this.global_state.getState().getLine(4).get(0, 3);
	}
	
	/*
	public void setGlobalState(Matrix state) {
		this.global_state = state;
		updateLocalInfo();
		markGlobalDirty();
		clarifyGlobal();
	}
	public void setGlobalState(Vector s, Quaternion r, Vector p) {setGlobalState(this.ROT_UNIT, s, r, p);}
	public void setGlobalState(Quaternion r1, Vector s, Quaternion r2, Vector p) {
		Matrix m1 = Utils.rotToHomoState(r1);
		Matrix m2 = Utils.sclToHomoState(s);
		Matrix m3 = Utils.rotToHomoState(r2);
		Matrix m4 = Utils.posToHomoState(p);
		this.setGlobalState(m1.mult(m2).mult(m3).mult(m4));
	}
	
	public void offset(Vector v) {setPos(Utils.compOffset(pos, v));}
	public void rotate(Quaternion q) {setRot(Utils.compRotate(rot, q));}
	public void rotate(Vector v) {setRot(Utils.compRotate(rot, Quaternion.eulerToQuat(v)));}
	public void zoom(Vector v) {setScale(Utils.compZoom(scl, v));}
	public void zoom(double d) {setScale(Utils.compZoom(scl, new Vector(d, d, d)));}
	 */
	// Tags

	public boolean addTag(BaseTag tag) {
		boolean flag = tag.onAdded(this).succeed;
		if (flag) tags.add(tag);
		return flag;
	}
	
	public boolean removeTag(BaseTag tag) {
		boolean flag = tag.onRemoved(this).succeed && tags.contains(tag);
		if (flag) tags.remove(tag);
		return flag;
	}
	
	public void applyTags(int phrase) {this.applyTags(phrase, BaseTag.getDummyExtra());}
	public void applyTags(int phrase, BaseTag.ApplicationExtra extra) {
		BaseTag.sortTags(tags);
		BaseTag t;
		for (int i = 0; i < tags.size(); ++i) {
			t = tags.get(i);
			if (t.isActivated() && t.appliesOn(phrase)) t.applyOn(phrase, this, extra);
		}
	}
	
	// Render
	
	public void render(double ptick) {
		GlRenderHelper renderer = GlRenderHelper.getInstance();
		renderer.dumpState();
		this.applyTags(BaseTag.TAG_PHRASE_RENDER_PRE);
		if (renderer.isDebugging()) this.renderDebug(ptick);
		if (this.vertsVisible()) {
			renderer.setVertState();
			this.renderVert(ptick);
		}
		if (this.edgesVisible()) {
			renderer.setEdgeState();
			this.renderEdge(ptick);
		}
		if (this.facesVisible()) {
			renderer.setFaceState();
			this.renderFace(ptick);
		}
		
		this.applyTags(BaseTag.TAG_PHRASE_RENDER_POST);
		renderer.resetState();
	}
	
	// Abstract Methods
	public abstract int getVertCount();
	public abstract int getEdgeCount();
	public abstract int getFaceCount();

	public abstract Vector getVertPos(int index);
	public abstract Vector getVertNrm(int index);
	public abstract Vector getVertUVM(int index);
	
	public abstract boolean isEdgeLooped(int index);
	public abstract int[] getEdgeIndices(int index);
	public abstract Triad[] getFaceIndices(int index);
	
	public void renderVert(double ptick) {
		GlRenderHelper renderer = GlRenderHelper.getInstance();
		this.applyTags(BaseTag.TAG_PHRASE_RENDER_VERTICES, new BaseTag.ApplicationExtra(ptick));
		for (int i = 0; i < this.getVertCount(); ++i) {
			this.applyTags(BaseTag.TAG_PHRASE_RENDER_PARTICULAR_VERTEX, new BaseTag.ApplicationExtra(ptick, i));
			Vector v = this.getVertPos(i);
			renderer.startDrawingVert();
			renderer.addVertex(v);
			renderer.endDrawing();
		}
	}
	public void renderEdge(double ptick) {
		GlRenderHelper renderer = GlRenderHelper.getInstance();
		this.applyTags(BaseTag.TAG_PHRASE_RENDER_EDGES, new BaseTag.ApplicationExtra(ptick));
		for (int i = 0; i < this.getEdgeCount(); ++i) {
			this.applyTags(BaseTag.TAG_PHRASE_RENDER_PARTICULAR_EDGE, new BaseTag.ApplicationExtra(ptick, i));
			int[] v_ = this.getEdgeIndices(i);
			renderer.startDrawingEdge(this.isEdgeLooped(i));
			for (int v: v_) {
				renderer.addVertex(this.getVertPos(v));
			}
			renderer.endDrawing();
		}
	}

	public void renderFace(double ptick) {
		GlRenderHelper renderer = GlRenderHelper.getInstance();
		this.applyTags(BaseTag.TAG_PHRASE_RENDER_FACES, new BaseTag.ApplicationExtra(ptick));
		for (int i = 0; i < this.getFaceCount(); ++i) {
			this.applyTags(BaseTag.TAG_PHRASE_RENDER_PARTICULAR_FACE, new BaseTag.ApplicationExtra(ptick, i));
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
	}
	
	public void renderDebug(double ptick) {
		
		GlRenderHelper renderer = GlRenderHelper.getInstance();
		
		renderer.disableDepth();
		renderer.setEdgeState();
		renderer.setLineWidth(3);
		
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
		
		renderer.setFaceState();
		renderer.enableDepth();
	}

	// Display Functions
	
	@Override
	public String toString() {
		String ans = "%s %s(STATE:%s)";
		ans = String.format(ans, this.getClass().getSimpleName(), this.getName(), this.getLocalState().toString());
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
		println(out, String.format("%s %s", this.getClass().getSimpleName(), this.getName()), level); 
		println(out, String.format("STATE_LOCAL%s: %s", (this.getLocalState() instanceof StateStandard3D ? "*" : " "), this.getLocalState()), level + 1);
		println(out, "STATE_GLOBAL: " + this.getGlobalState(), level + 1);
		if (this.children.size() == 0) return;
		println(out, "", level + 1);
		println(out, "CHILDREN:", level + 1);
		for (BaseNode i: this.children) {
			i.dump(out, level + 2);
		}
	}

}

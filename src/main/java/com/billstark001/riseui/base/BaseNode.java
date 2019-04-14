package com.billstark001.riseui.base;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

import scala.actors.threadpool.Arrays;

public abstract class BaseNode extends BaseObject{

	protected Vector pos, wpos;
	protected Quaternion rot, wrot;
	protected Vector scale, wscale;

	protected List<BaseTag> tags = new ArrayList<BaseTag>();
	protected List<BaseNode> children = new ArrayList<BaseNode>();
	protected BaseNode parent = null;
	
	protected boolean global_dirty;
	
	public static final Vector POS_UNIT = Vector.UNIT0_D3;
	public static final Quaternion ROT_UNIT = Quaternion.UNIT;
	public static final Vector SCALE_UNIT = Vector.UNIT1_D3;

	public BaseNode(Vector pos, Quaternion rot, Vector scale, String name) {
		super(name);
		global_dirty = false;
		this.pos = this.wpos = pos;
		this.rot = this.wrot = rot;
		this.scale = this.wscale = scale;
		this.parent = null;
	}
	
	public BaseNode(Vector pos, Quaternion rot, Vector scale) {this(pos, rot, scale, DEFAULT_NAME);}
	public BaseNode(Vector pos, Quaternion rot, double scale) {this(pos, rot, new Vector(scale, scale, scale), DEFAULT_NAME);}
	public BaseNode(Vector pos, Vector rot, Vector scale) {this(pos, Quaternion.eulerToQuat(rot), scale, DEFAULT_NAME);}
	public BaseNode(Vector pos, Quaternion rot) {this(pos, rot, SCALE_UNIT, DEFAULT_NAME);}
	public BaseNode(Vector pos) {this(pos, ROT_UNIT, SCALE_UNIT, DEFAULT_NAME);}
	public BaseNode() {this(POS_UNIT, ROT_UNIT, SCALE_UNIT, DEFAULT_NAME);}
	
	public BaseNode(Vector pos, Quaternion rot, double scale, String name) {this(pos, rot, new Vector(scale, scale, scale), name);}
	public BaseNode(Vector pos, Vector rot, Vector scale, String name) {this(pos, Quaternion.eulerToQuat(rot), scale, name);}
	public BaseNode(Vector pos, Quaternion rot, String name) {this(pos, rot, SCALE_UNIT, name);}
	public BaseNode(Vector pos, String name) {this(pos, ROT_UNIT, SCALE_UNIT, name);}
	public BaseNode(String name) {this(POS_UNIT, ROT_UNIT, SCALE_UNIT, name);}
	
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

	public boolean addChild(BaseNode obj) {return obj.setParent(this);}
	public boolean setParent(BaseNode parent) {
		if (parent == null) return removeParent();
		if (parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (this.parent != null && this.parent.children.contains(this)) this.parent.children.remove(this);
		updateGlobalInfo();
		this.parent = parent;
		if (!parent.children.contains(this)) parent.children.add(this);
		updateLocalInfo();
		return true;
	}
	public boolean removeParent() {
		if (this.parent == null) return false;
		if (this.parent.children.contains(this)) this.parent.children.remove(this);
		updateGlobalInfo();
		this.parent = null;
		updateLocalInfo();
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
	protected void updateLocalInfo() {
		if (parent == null) {
			pos = wpos;
			rot = wrot;
			scale = wscale;
			
			for (BaseTag t: tags) t.onLocalUpdate(this);
			return;
		}
		scale = Utils.splitZoom(getGlobalScale(), parent.getGlobalScale());
		rot = Utils.splitRotate(getGlobalRot(), parent.getGlobalRot());
		pos = Utils.splitOffset(getGlobalPos(), parent.getGlobalPos());
		pos = pos.div(parent.getGlobalScale());
		
		for (BaseTag t: tags) t.onLocalUpdate(this);
	}
	
	protected void updateGlobalInfo() {if (GlobalDirty()) updateGlobalInfoForced();}
	protected void updateGlobalInfoForced() {
		if (parent == null) {
			wpos = pos;
			wrot = rot;
			wscale = scale;
			clarifyGlobal();
			
			for (BaseTag t: tags) t.onGlobalUpdate(this);
			return;
		}
		parent.updateGlobalInfo();
		wpos = Utils.compOffset(parent.getGlobalPos(), getPos().mult(parent.getScale()));
		wscale = Utils.compZoom(parent.getGlobalScale(), getScale());
		wrot = Utils.compRotate(parent.getGlobalRot(), getRot());
		clarifyGlobal();
		
		for (BaseTag t: tags) t.onGlobalUpdate(this);
	}
	//Local Info. Getter & Setter

	public Vector getPos() {return pos;}
	public Quaternion getRot() {return rot;}
	public Vector getScale() {return scale;}
	
	public void setPos(Vector pos) {
		this.pos = pos;
		markGlobalDirty();
	}

	public void setRot(Vector rot) {setRot(Quaternion.eulerToQuat(rot));}
	public void setRot(Quaternion rot) {
		this.rot = rot;
		markGlobalDirty();
	}
	
	public void setScale(double scale) {setScale(new Vector(scale, scale, scale));}
	public void setScale(Vector scale) {
		this.scale = scale;
		markGlobalDirty();
	}
	
	// Global Info. Getter & Setter

	public Vector getGlobalPos() {
		updateGlobalInfo();
		return wpos;
	}
	public Quaternion getGlobalRot() {
		updateGlobalInfo();
		return wrot;
	}
	public Vector getGlobalScale() {
		updateGlobalInfo();
		return wscale;
	}
	
	public void setGlobalPos(Vector wpos) {
		this.wpos = wpos;
		updateLocalInfo();
		markGlobalDirty();
		clarifyGlobal();
		
	}
	public void setGlobalRot(Vector wrot) {setGlobalRot(Quaternion.eulerToQuat(wrot));}
	public void setGlobalRot(Quaternion wrot) {
		this.wrot = wrot;
		updateLocalInfo();
		markGlobalDirty();
		clarifyGlobal();
	}
	public void setGlobalScale(double wscale) {setGlobalScale(new Vector(wscale, wscale, wscale));}
	public void setGlobalScale(Vector wscale) {
		this.wscale = wscale;
		updateLocalInfo();
		markGlobalDirty();
		clarifyGlobal();
	}
	
	public void setGlobalPosSolely(Vector wpos) {
		for (BaseNode obj: children) obj.updateGlobalInfo();
		this.wpos = wpos;
		updateLocalInfo();
		for (BaseNode obj: children) obj.updateLocalInfo();
	}
	public void setGlobalRotSolely(Vector wrot) {setGlobalRotSolely(Quaternion.eulerToQuat(wrot));}
	public void setGlobalRotSolely(Quaternion wrot) {
		for (BaseNode obj: children) obj.updateGlobalInfo();
		this.wrot = wrot;
		updateLocalInfo();
		for (BaseNode obj: children) obj.updateLocalInfo();
	}
	public void setGlobalScaleSolely(double wscale) {setGlobalScaleSolely(new Vector(wscale, wscale, wscale));}
	public void setGlobalScaleSolely(Vector wscale) {
		for (BaseNode obj: children) obj.updateGlobalInfo();
		this.wscale = wscale;
		updateLocalInfo();
		for (BaseNode obj: children) obj.updateLocalInfo();
	}
	
	public void offset(Vector v) {setPos(Utils.compOffset(pos, v));}
	public void rotate(Quaternion q) {setRot(Utils.compRotate(rot, q));}
	public void rotate(Vector v) {setRot(Utils.compRotate(rot, Quaternion.eulerToQuat(v)));}
	public void zoom(Vector v) {setScale(Utils.compZoom(scale, v));}
	public void zoom(double d) {setScale(Utils.compZoom(scale, new Vector(d, d, d)));}

	// Handle Tags

	public boolean addTag(BaseTag tag) {
		tags.add(tag);
		return true;
	}
	
	public boolean removeTag(BaseTag tag) {
		if (tags.contains(tag)) {
			tags.remove(tag);
			return true;
		}
		return false;
	}
	
	public void render() {
		for (BaseTag t: tags) t.onRenderPre(this);
		this.onRender();
		this.onRenderDebug();
		for (BaseTag t: tags) t.onRenderPost(this);
	}
	
	public abstract void onRender();
	public void onRenderDebug() {
		
	}

	// Display Functions
	
	@Override
	public String toString() {
		String ans = "%s %s:\n LOCAL:\n  POS%s\n  ROT%s\n  SCL%s\n GLOBAL:\n  POS%s\n  ROT%s\n  SCL%s";
		ans = String.format(ans, this.getClass().getSimpleName(), this.getName(), pos.toString(), rot.toString(), scale.toString(), getGlobalPos().toString(), getGlobalRot().toString(), getGlobalScale().toString());
		return ans;
	}
	
	private String genBlank(int count) {
		char[] temp = new char[count];
		Arrays.fill(temp, ' ');
		return new String(temp);
	}
	private void println(PrintStream out, String text, int blank) {
		out.print(genBlank(blank));
		out.println(text);
	}
	
	public void dump() {dump(System.out, 0);}
	public void dump(PrintStream out) {dump(out, 0);}
	private void dump(PrintStream out, int level){
		println(out, String.format("%s %s", this.getClass().getSimpleName(), this.getName()), level); 
		println(out, "LOCAL:", level + 1);
		println(out, "POS" + this.getPos(), level + 2);
		println(out, "ROT" + this.getRot(), level + 2);
		println(out, "SCL" + this.getScale(), level + 2);
		println(out, "GLOBAL:", level + 1);
		println(out, "POS" + this.getGlobalPos(), level + 2);
		println(out, "ROT" + this.getGlobalRot(), level + 2);
		println(out, "SCL" + this.getGlobalScale(), level + 2);
		if (this.children.size() == 0) return;
		println(out, "CHILDREN:", level + 1);
		for (BaseNode i: this.children) {
			i.dump(out, level + 2);
		}
	}

}

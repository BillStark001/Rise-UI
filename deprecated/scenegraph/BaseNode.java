package com.billstark001.riseui.math.scenegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.base.tag.ITag;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

import scala.actors.threadpool.Arrays;

public class BaseNode{

	protected Vector pos, wpos;
	protected Quaternion rot, wrot;
	protected Vector scale, wscale;

	protected List<ITag> tags = new ArrayList<ITag>();
	protected List<BaseNode> children = new ArrayList<BaseNode>();
	protected BaseNode parent = null;
	
	protected boolean pd, sd, rd, wpd, wsd, wrd;
	protected boolean local_dirty, global_dirty;

	public static final BaseNode ROOT_NODE = new BaseNode(true);

	private BaseNode(boolean b) {
		// Initi
		pd = sd = rd = wpd = wsd = wrd = false;
		this.pos = this.wpos = new Vector(0, 0, 0);
		this.rot = this.wrot = Quaternion.UNIT;
		this.scale = this.wscale = new Vector(1, 1, 1);
		this.parent = this;
	}

	public BaseNode(Vector pos, Quaternion rot, Vector scale) {
		pd = sd = rd = wpd = wsd = wrd = false;
		this.pos = this.wpos = pos;
		this.rot = this.wrot = rot;
		this.scale = this.wscale = scale;
		this.parent = ROOT_NODE;
		ROOT_NODE.children.add(this);
	}
	
	public BaseNode(Vector pos, Quaternion rot, double scale) {this(pos, rot, new Vector(scale, scale, scale));}
	public BaseNode(Vector pos, Vector rot, Vector scale) {this(pos, Quaternion.eulerToQuat(rot), scale);}
	public BaseNode(Vector pos, Quaternion rot) {this(pos, rot, new Vector(1, 1, 1));}
	public BaseNode(Vector pos) {this(pos, Quaternion.UNIT, new Vector(1, 1, 1));}
	public BaseNode() {this(new Vector(0, 0, 0), Quaternion.UNIT, new Vector(1, 1, 1));}
	
	public BaseNode getParent() {return parent;}
	public BaseNode getChild(int index) {return children.get(index);}
	public BaseNode[] getChildren() {return children.toArray(new BaseNode[0]);}
	
	@Override
	public String toString() {
		String ans = "%s@%x: LOCAL: POS%s, ROT%s, SCALE%s; GLOBAL: POS%s, ROT%s, SCALE%s";
		ans = String.format(ans, this.getClass().getSimpleName(), this.hashCode(), pos.toString(), rot.toString(), scale.toString(), getGlobalPos().toString(), getGlobalRot().toString(), getGlobalScale().toString());
		return ans;
	}
	
	// Tree Node Related
	
	public boolean isAncestor(BaseNode obj) {
		boolean flag = false;
		while (!flag) {
			obj = obj.getParent();
			if (obj == this) flag = true;
			if (obj == ROOT_NODE) break;
		}
		return flag;
	}
	public boolean isDescendant(BaseNode obj) {
		return obj.isAncestor(this);
	}

	public boolean addChild(BaseNode obj) {
		return obj.setParent(this);
	}
	public boolean setParent(BaseNode parent) {
		if (this == ROOT_NODE || parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (parent == null) parent = ROOT_NODE;
		if (this.parent.children.contains(this)) this.parent.children.remove(this);
		this.parent = parent;
		if (!parent.children.contains(this)) parent.children.add(this);
		pd = sd = rd = true;
		updateLocalInfo();
		return true;
	}
	public boolean removeParent() {
		if (this == ROOT_NODE) return false; 
		return setParent(ROOT_NODE);
	}
	public boolean removeChild(int index) {
		if (this == ROOT_NODE) return false; 
		try {
			this.children.get(index).setParent(ROOT_NODE);
			return true;
		} catch(IndexOutOfBoundsException e) {
			return false;
		}
	}
	public boolean removeChild(BaseNode obj) {
		if (this == ROOT_NODE) return false; 
		if (children.contains(obj)) {
			obj.setParent(ROOT_NODE);
			return true;
		}
		return false;
	}
	public boolean removeAllChildren() {
		if (this == ROOT_NODE) return false; 
		boolean flag = false;
		while (children.size() > 0) {
			flag = removeChild(0);
			if (!flag) return false;
		}
		return true;
	}
	
	// Information Maintaining
	
	//Local Info. Getter & Setter

	public Vector getPos() {
		//updateLocalInfo();
		return pos;
	}

	public Quaternion getRot() {
		//updateLocalInfo();
		return rot;
	}

	public Vector getScale() {
		//updateLocalInfo();
		return scale;
	}
	
	public void setPos(Vector pos) {
		if (this == ROOT_NODE) return; 
		this.pos = pos;
		wpd = true;
		updateGlobalInfo();
		downgradeGlobalMark(0);
	}

	public void setRot(Quaternion rot) {
		if (this == ROOT_NODE)return;
		this.rot = rot;
		wrd = true;
		updateGlobalInfo();
		downgradeGlobalMark(1);
	}

	public void setRot(Vector rot) {
		if (this == ROOT_NODE)return;
		this.rot = Quaternion.eulerToQuat(rot);
		wrd = true;
		updateGlobalInfo();
		downgradeGlobalMark(1);
	}
	
	public void setScale(Vector scale) {
		if (this == ROOT_NODE)return;
		this.scale = scale;
		wsd = true;
		updateGlobalInfo();
		downgradeGlobalMark(2);
	}

	public void setScale(double scale) {
		if (this == ROOT_NODE)return;
		this.scale = new Vector(scale, scale, scale);
		wsd = true;
		updateGlobalInfo();
		downgradeGlobalMark(2);
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
		if (this == ROOT_NODE)return;
		this.wpos = wpos;
		pd = true;
		updateLocalInfo();
		downgradeGlobalMark(0);
	}

	public void setGlobalRot(Quaternion wrot) {
		if (this == ROOT_NODE)return;
		this.wrot = wrot;
		rd = true;
		updateLocalInfo();
		downgradeGlobalMark(1);
	}
	
	public void setGlobalRot(Vector wrot) {
		if (this == ROOT_NODE)return;
		this.wrot = Quaternion.eulerToQuat(wrot);
		rd = true;
		updateLocalInfo();
		downgradeGlobalMark(1);
	}

	public void setGlobalScale(Vector wscale) {
		if (this == ROOT_NODE)return;
		this.wscale = wscale;
		sd = true;
		updateLocalInfo();
		downgradeGlobalMark(2);
	}
	
	public void setGlobalScale(double wscale) {
		if (this == ROOT_NODE)return;
		this.wscale = new Vector(wscale, wscale, wscale);
		sd = true;
		updateLocalInfo();
		downgradeGlobalMark(2);
	}
	
	public void offset(Vector v) {setPos(Utils.compOffset(pos, v));}
	public void rotate(Quaternion q) {setRot(Utils.compRotate(rot, q));}
	public void rotate(Vector v) {setRot(Utils.compRotate(rot, Quaternion.eulerToQuat(v)));}
	public void zoom(Vector v) {setScale(Utils.compZoom(scale, v));}
	public void zoom(double d) {setScale(Utils.compZoom(scale, new Vector(d, d, d)));}

	protected void updateLocalInfo() {
		if (sd) scale = Utils.splitZoom(wscale, parent.wscale);
		if (rd) rot = Utils.splitRotate(wrot, parent.wrot);
		if (pd) {
			pos = Utils.splitOffset(wpos, parent.wpos);
			pos = pos.div(parent.scale);
		}
		pd = sd = rd = false;
	}
	
	protected void updateGlobalInfo() {
		if (wpd) wpos = Utils.compOffset(parent.getGlobalPos(), pos.mult(parent.getScale()));
		if (wsd) wscale = Utils.compZoom(parent.getGlobalScale(), scale);
		if (wrd) wrot = Utils.compRotate(parent.getGlobalRot(), rot);
		wpd = wsd = wrd = false;
	}
	
	private final int MARK_DIRTY_POS = 0;
	private final int MARK_DIRTY_ROT = 1;
	private final int MARK_DIRTY_SCALE = 2;
	protected void downgradeGlobalMark(int type) {
		switch (type) {
		case 1:
			for (BaseNode obj: children) {
				obj.wpd = true;
				obj.downgradeGlobalMark(1);
			}
			break;
		case 2:
			for (BaseNode obj: children) {
				obj.wpd = true;
				obj.downgradeGlobalMark(2);
			}
			break;
		default:
			for (BaseNode obj: children) {
				obj.wpd = true;
				obj.downgradeGlobalMark(0);
			}
		}
	}
	
	public static void printTree(BaseNode obj) {
		printTree(obj, "");
	}
	private static void printTree(BaseNode obj, String s){
		System.out.print(s);
		System.out.println(obj);
		for (BaseNode i: obj.children) {
			String s_ = s + " ";
			printTree(i, s_);
		}
	}

}

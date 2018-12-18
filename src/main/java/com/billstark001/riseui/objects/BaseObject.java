package com.billstark001.riseui.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.tags.ITag;

import scala.actors.threadpool.Arrays;

public class BaseObject{

	protected Vector pos, wpos;
	protected Quaternion rot, wrot;
	protected Vector scale, wscale;

	protected List<ITag> tags = new ArrayList<ITag>();
	protected List<BaseObject> children = new ArrayList<BaseObject>();
	protected BaseObject parent = null;
	
	protected boolean pd, sd, rd, wpd, wsd, wrd;

	public static final BaseObject ROOT_OBJECT = new BaseObject(true);

	private BaseObject(boolean b) {
		pd = sd = rd = wpd = wsd = wrd = false;
		this.pos = this.wpos = new Vector(0, 0, 0);
		this.rot = this.wrot = Quaternion.UNIT;
		this.scale = this.wscale = new Vector(1, 1, 1);
		this.parent = this;
	}

	public BaseObject(Vector pos, Quaternion rot, Vector scale) {
		pd = sd = rd = wpd = wsd = wrd = false;
		this.pos = this.wpos = pos;
		this.rot = this.wrot = rot;
		this.scale = this.wscale = scale;
		this.parent = ROOT_OBJECT;
		ROOT_OBJECT.children.add(this);
	}
	
	public BaseObject(Vector pos, Quaternion rot, double scale) {this(pos, rot, new Vector(scale, scale, scale));}
	public BaseObject(Vector pos, Vector rot, Vector scale) {this(pos, Quaternion.eulerToQuat(rot), scale);}
	public BaseObject(Vector pos, Quaternion rot) {this(pos, rot, new Vector(1, 1, 1));}
	public BaseObject(Vector pos) {this(pos, Quaternion.UNIT, new Vector(1, 1, 1));}
	public BaseObject() {this(new Vector(0, 0, 0), Quaternion.UNIT, new Vector(1, 1, 1));}
	
	public BaseObject getParent() {return parent;}
	public BaseObject getChild(int index) {return children.get(index);}
	public BaseObject[] getChildren() {return children.toArray(new BaseObject[0]);}
	
	@Override
	public String toString() {
		String ans = "%s@%x: LOCAL: POS%s, ROT%s, SCALE%s; GLOBAL: POS%s, ROT%s, SCALE%s";
		ans = String.format(ans, this.getClass().getSimpleName(), this.hashCode(), pos.toString(), rot.toString(), scale.toString(), getGlobalPos().toString(), getGlobalRot().toString(), getGlobalScale().toString());
		return ans;
	}
	
	// Tree Node Related
	
	public boolean isAncestor(BaseObject obj) {
		boolean flag = false;
		while (!flag) {
			obj = obj.getParent();
			if (obj == this) flag = true;
			if (obj == ROOT_OBJECT) break;
		}
		return flag;
	}
	public boolean isDescendant(BaseObject obj) {
		return obj.isAncestor(this);
	}

	public boolean addChild(BaseObject obj) {
		return obj.setParent(this);
	}
	public boolean setParent(BaseObject parent) {
		if (this == ROOT_OBJECT || parent == this) return false;
		if (parent.isDescendant(this)) return false;
		if (parent == null) parent = ROOT_OBJECT;
		if (this.parent.children.contains(this)) this.parent.children.remove(this);
		this.parent = parent;
		if (!parent.children.contains(this)) parent.children.add(this);
		pd = sd = rd = true;
		updateLocalInfo();
		return true;
	}
	public boolean removeParent() {
		if (this == ROOT_OBJECT) return false; 
		return setParent(ROOT_OBJECT);
	}
	public boolean removeChild(int index) {
		if (this == ROOT_OBJECT) return false; 
		try {
			this.children.get(index).setParent(ROOT_OBJECT);
			return true;
		} catch(IndexOutOfBoundsException e) {
			return false;
		}
	}
	public boolean removeChild(BaseObject obj) {
		if (this == ROOT_OBJECT) return false; 
		if (children.contains(obj)) {
			obj.setParent(ROOT_OBJECT);
			return true;
		}
		return false;
	}
	public boolean removeAllChildren() {
		if (this == ROOT_OBJECT) return false; 
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
		if (this == ROOT_OBJECT) return; 
		this.pos = pos;
		wpd = true;
		updateGlobalInfo();
		downgradeGlobalMark(0);
	}

	public void setRot(Quaternion rot) {
		if (this == ROOT_OBJECT)return;
		this.rot = rot;
		wrd = true;
		updateGlobalInfo();
		downgradeGlobalMark(1);
	}

	public void setRot(Vector rot) {
		if (this == ROOT_OBJECT)return;
		this.rot = Quaternion.eulerToQuat(rot);
		wrd = true;
		updateGlobalInfo();
		downgradeGlobalMark(1);
	}
	
	public void setScale(Vector scale) {
		if (this == ROOT_OBJECT)return;
		this.scale = scale;
		wsd = true;
		updateGlobalInfo();
		downgradeGlobalMark(2);
	}

	public void setScale(double scale) {
		if (this == ROOT_OBJECT)return;
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
		if (this == ROOT_OBJECT)return;
		this.wpos = wpos;
		pd = true;
		updateLocalInfo();
		downgradeGlobalMark(0);
	}

	public void setGlobalRot(Quaternion wrot) {
		if (this == ROOT_OBJECT)return;
		this.wrot = wrot;
		rd = true;
		updateLocalInfo();
		downgradeGlobalMark(1);
	}
	
	public void setGlobalRot(Vector wrot) {
		if (this == ROOT_OBJECT)return;
		this.wrot = Quaternion.eulerToQuat(wrot);
		rd = true;
		updateLocalInfo();
		downgradeGlobalMark(1);
	}

	public void setGlobalScale(Vector wscale) {
		if (this == ROOT_OBJECT)return;
		this.wscale = wscale;
		sd = true;
		updateLocalInfo();
		downgradeGlobalMark(2);
	}
	
	public void setGlobalScale(double wscale) {
		if (this == ROOT_OBJECT)return;
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
			for (BaseObject obj: children) {
				obj.wpd = true;
				obj.downgradeGlobalMark(1);
			}
			break;
		case 2:
			for (BaseObject obj: children) {
				obj.wpd = true;
				obj.downgradeGlobalMark(2);
			}
			break;
		default:
			for (BaseObject obj: children) {
				obj.wpd = true;
				obj.downgradeGlobalMark(0);
			}
		}
	}
	
	// Handle Tags

	public boolean addTag(ITag tag) {
		if (this == ROOT_OBJECT) return false; 
		if (tag.suits(this)) {
			tags.add(tag);
			return true;
		}
		return false;
	}
	
	public boolean removeTag(ITag tag) {
		if (this == ROOT_OBJECT) return false; 
		if (tags.contains(tag)) {
			tags.remove(tag);
			return true;
		}
		return false;
	}

	public void handleTags() {
		if (this == ROOT_OBJECT) return; 
		for (int i = 0; i < tags.size(); ++i) tags.get(i).callOnHandle(this);
	}
	
	public static void printTree(BaseObject obj) {
		printTree(obj, "");
	}
	private static void printTree(BaseObject obj, String s){
		System.out.print(s);
		System.out.println(obj);
		for (BaseObject i: obj.children) {
			String s_ = s + " ";
			printTree(i, s_);
		}
	}

}

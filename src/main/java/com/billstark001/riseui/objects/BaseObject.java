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
	public BaseObject[] getChildren() {return (BaseObject[]) children.toArray();}
	
	@Override
	public String toString() {
		String ans = "%s@%x: POS%s, ROT%s, SCALE%s";
		ans = String.format(ans, this.getClass().getSimpleName(), this.hashCode(), pos.toString(), rot.toString(), scale.toString());
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

	public Vector getPos() {return pos;}
	public void setPos(Vector pos) {
		if (this == ROOT_OBJECT) return; 
		this.pos = pos;
		wpd = true;
	}

	public Quaternion getRot() {return rot;}
	public void setRot(Quaternion rot) {
		if (this == ROOT_OBJECT)return;
		this.rot = rot;
		wrd = true;
	}

	public void setRot(Vector rot) {
		if (this == ROOT_OBJECT)return;
		this.rot = Quaternion.eulerToQuat(rot);
		wrd = true;
	}

	public Vector getScale() {return scale;}
	public void setScale(Vector scale) {
		if (this == ROOT_OBJECT)return;
		this.scale = scale;
		wsd = true;
	}

	public void setScale(double scale) {
		if (this == ROOT_OBJECT)return;
		this.scale = new Vector(scale, scale, scale);
		wsd = true;
	}

	public Vector getGlobalPos() {return wpos;}
	public Quaternion getGlobalRot() {return wrot;}
	public Vector getGlobalScale() {return wscale;}
	
	protected void updateLocalInfo() {
		scale = Utils.splitZoom(wscale, parent.wscale);
		rot = Utils.splitRotate(wrot, parent.wrot);
		pos = Utils.splitOffset(wpos, parent.wpos);
		pos = pos.div(scale);
		pd = sd = rd = false;
	}
	
	protected void updateGlobalInfo() {
		if (wpd) wpos = Utils.compOffset(parent.wpos, pos.mult(scale));
		if (wsd) wscale = Utils.compZoom(parent.wscale, scale);
		if (wrd) wrot = Utils.compRotate(parent.wrot, rot);
		wpd = wsd = wrd = false;
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

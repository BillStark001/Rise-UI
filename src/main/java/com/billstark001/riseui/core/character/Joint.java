package com.billstark001.riseui.core.character;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.base.IGridable;
import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class Joint extends BaseNode implements IGridable {

	public Joint(Vector pos, Quaternion rot, Vector scale, String name) {super(pos, rot, scale, name);}
	public Joint(Vector pos, Quaternion rot, Vector scale) {super(pos, rot, scale);}
	public Joint(Vector pos, Quaternion rot, double scale) {super(pos, rot, scale);}
	public Joint(Vector pos, Vector rot, Vector scale) {super(pos, rot, scale);}
	public Joint(Vector pos, Quaternion rot) {super(pos, rot);}
	public Joint(Vector pos) {super(pos);}
	public Joint() {}
	public Joint(Vector pos, Quaternion rot, double scale, String name) {super(pos, rot, scale, name);}
	public Joint(Vector pos, Vector rot, Vector scale, String name) {super(pos, rot, scale, name);}
	public Joint(Vector pos, Quaternion rot, String name) {super(pos, rot, name);}
	public Joint(Vector pos, String name) {super(pos, name);}
	public Joint(String name) {super(name);}

	private List<Joint> inferior = new ArrayList<Joint>();
	private Joint superior = null;
	private double length;
	private double[][] d = {{0, 0, 0}, {0, 1, 0}, {0.25, 0.75, 0}, {0, 0.75, 0.25}, {-0.25, 0.75, 0}, {0, 0.75, -0.25}};
	private Matrix vertices = new Matrix(d);
	private Matrix vcur;
	
	public Joint getSuperior() {return superior;}
	public Joint[] getInferiors() {return inferior.toArray(new Joint[0]);}
	
	public double getLength() {
		if (this.parent == null || !(this.parent instanceof Joint)) return 0;
		return this.getGlobalPos().add(this.parent.getGlobalPos().mult(-1)).getLength();
	}

	// Maintainence
	
	@Override
	public boolean addChild(BaseNode obj) {
		boolean flag = super.addChild(obj);
		if (flag && obj instanceof Joint) {this.inferior.add((Joint) obj);}
		return flag;
	}
	
	@Override
	public boolean setParent(BaseNode parent) {
		boolean flag = super.setParent(parent);
		if (flag && parent != null && parent instanceof Joint) {
			this.superior = (Joint) parent;
		}
		return flag;
	}
	
	@Override
	public boolean removeParent() {
		if (this.parent instanceof Joint) {
			if (((Joint) this.parent).inferior.contains(this)) ((Joint) this.parent).inferior.remove(this); 
			this.superior = null;
			this.length = 0;
		}
		return super.removeParent();
	}
	
	@Override
	public void dump(PrintStream out, int level){
		println(out, String.format("%s %s", this.getClass().getSimpleName(), this.getName()), level); 
		println(out, "LOCAL:", level + 1);
		println(out, "POS" + vec32String(this.getPos()), level + 2);
		println(out, "ROT" + quat2String(this.getRot()), level + 2);
		println(out, "SCL" + vec32String(this.getScale()), level + 2);
		println(out, "GLOBAL:", level + 1);
		println(out, "POS" + vec32String(this.getGlobalPos()), level + 2);
		println(out, "ROT" + quat2String(this.getGlobalRot()), level + 2);
		println(out, "SCL" + vec32String(this.getGlobalScale()), level + 2);
		println(out, "LENGTH: " + this.getLength(), level + 1);
		if (this.children.size() == 0) return;
		println(out, "", level + 1);
		println(out, "CHILDREN:", level + 1);
		for (BaseNode i: this.children) {
			i.dump(out, level + 2);
		}
	}
	
	// Render
	
	@Override
	public void onRender(double ptick) {
		
	}
	
	public void onRenderDebug() {
		GlRenderHelper.getInstance().disableDepth();
		refreshGrid();
		GlRenderHelper.getInstance().renderGrid(this);
		GlRenderHelper.getInstance().enableDepth();
	}

	@Override
	public boolean getLooped() {return false;}

	@Override
	public int getSegmentCount() {return 15;}

	@Override
	public int[] getSegment(int index) {
		int[] ans = new int[2];
		if (index < 0 || index >= 15) index = 0;
		if (index == 0) {
			ans[0] = 0; ans[1] = 1;
		} else if (index <= 4) {
			ans[0] = 0; ans[1] = index + 1;
		} else if (index <= 8) {
			ans[0] = 1; ans[1] = index - 3;
		} else {
			int[][] anst = {{2, 3}, {3, 4}, {4, 5}, {5, 2}, {2, 4}, {3, 5}};
			ans = anst[index - 9];
		}
		return ans;
	}

	public void refreshGrid() {
		vcur = Utils.zoom(vertices, length);
		vcur = Utils.rotate(vertices, rot.inverse());
	}
	@Override
	public Vector getVertex(int index) {return vcur.getLine(index);}

}

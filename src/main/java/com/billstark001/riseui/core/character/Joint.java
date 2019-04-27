package com.billstark001.riseui.core.character;

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
	
	public Joint getSuperior() {
		if (superior == null) return null;
		else return superior;
	}
	
	public Joint getInferior(int index) {return inferior.get(index);}
	public Joint[] getInferiors() {return inferior.toArray(new Joint[0]);}
	
	public double getLength() {return length;}

	// Maintainence
	
	@Override
	public boolean addChild(BaseNode obj) {
		if (obj instanceof Joint) {this.addInferior((Joint) obj);}
		return super.addChild(obj);
	}
	public boolean addInferior(Joint j) {
		return j.setSuperior(this);
	}
	
	@Override
	public boolean setParent(BaseNode parent) {
		boolean flag = super.setParent(parent);
		if (flag && parent instanceof Joint) {this.setSuperior((Joint) parent);}
		return flag;
	}
	public boolean setSuperior(Joint j) {
		if (j == this || j == null || j == superior) return false;
		if (superior != null && superior.inferior.contains(this))superior.inferior.remove(this);
		this.superior = j;
		if (!j.inferior.contains(this)) j.inferior.add(this);
		this.length = this.pos.getLength();
		return true;
	}
	
	// Render
	
	@Override
	public void onRender(double ptick) {
		
	}
	
	public void onRenderDebug() {
		GlRenderHelper.getInstance().clearAccumCache();
		refreshGrid();
		GlRenderHelper.getInstance().renderGrid(this);
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

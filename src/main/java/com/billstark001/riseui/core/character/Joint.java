package com.billstark001.riseui.core.character;

import java.util.ArrayList;
import java.util.List;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.TagBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.render.GlHelper;

public class Joint extends NodeBase {

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

	public Joint(State3DIntegrated c, String name) {super(c, name);}
	public Joint(State3DIntegrated c) {super(c, null);}

	private static final double[][] d = {{0, 1, 0, 1}, {0, 0, 0, 1}, {0.25, 0.25, 0, 1}, {0, 0.25, 0.25, 1}, {-0.25, 0.25, 0, 1}, {0, 0.25, -0.25, 1}};
	private static final Matrix vertices = new Matrix(d);
	private static Matrix vcur = vertices;
	
	public Joint getSuperior() {return this.parent instanceof Joint? (Joint) this.parent: null;}
	public Joint[] getInferiors() {
		ArrayList<Joint> ans = new ArrayList<Joint>();
		for (NodeBase n: this.children) {
			if (n instanceof Joint) ans.add((Joint) n);
		}
		return ans.toArray(new Joint[0]);
	}
	
	public double getLength() {
		if (this.parent == null || !(this.parent instanceof Joint)) return 0;
		return this.getGlobalPos().add(this.parent.getGlobalPos().mult(-1)).getLength();
	}

	// Render

	@Override
	public boolean isEdgeLooped(int i) {return false;}

	@Override
	public int getVertCount() {return 0;}//6;}
	@Override
	public int getEdgeCount() {return 0;}//15;}
	@Override
	public int getFaceCount() {return 0;}

	@Override
	public int[] getEdgeIndices(int index) {
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
		//vcur = Utils.zoom(vertices, length);
		//vcur = Utils.rotate(vertices, rot.inverse());
	}
	@Override
	public Vector getVertPos(int index) {return vcur.getLine(index);}


	@Override
	public Vector getVertNrm(int index) {
		return new Vector(0, 0, 0);
	}
	@Override
	public Vector getVertUVM(int index) {
		return new Vector(0, 0, 0);
	}
	@Override
	public Triad[] getFaceIndices(int index) {
		return null;
	}
	@Override
	public int getEdgeIndicesLength(int index) {
		return 0;
	}
	public int getFaceIndicesLength(int index) {
		return 0;
	}

	@Override
	public void renderDebug(double ptick) {
		super.renderDebug(ptick);
		if (!(this.parent instanceof Joint)) return;
		GlHelper renderer = GlHelper.getInstance();
		renderer.setColor(186, 231, 241);
		
		Matrix parent_state = Matrix.inverse(this.getLocalState().get());
		
		
		vcur = Matrix.I4.mult(parent_state);
		
		Vector v1 = new Vector(0, -1, 0);
		Vector v2 = vcur.getLine(3).mult(-1).get(0, 3);
		
		Vector axis = v1.cross(v2);
		double angle = Math.acos(v1.dot(v2) / (v1.getLength() * v2.getLength()));
		//double angle2 = Math.asin(axis.getLength() / (v1.getLength() * v2.getLength()));
		
		Quaternion rot = Quaternion.getQuatByAA(axis, -angle/2);
		
		vcur = vertices;
		vcur = Utils3D.rotate(vcur, rot);
		vcur = vcur.mult(Utils3D.sclToHomoState(this.getGlobalState().decomp().getScl().power(-1).mult(0.4*Math.min(1, this.getLength()))));
		vcur = vcur.mult(parent_state);
		
		for (int i = 0; i < 15; ++i) {
			int[] v_ = this.getEdgeIndices(i);
			renderer.startDrawingEdge(this.isEdgeLooped(i));
			Vector vpos;
			for (int v: v_) {
				if (v == 0) {
					vpos = Vector.UNIT0_D3;
				} else {
					vpos = vcur.getLine(v);
				}
				renderer.addVertex(vpos);
			}
			renderer.endDrawing();
		}
	}
	
}

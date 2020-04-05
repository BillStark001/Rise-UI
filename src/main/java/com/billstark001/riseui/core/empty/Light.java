package com.billstark001.riseui.core.empty;

import java.util.Arrays;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Quaternion;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.Utils3D;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.character.Joint;
import com.billstark001.riseui.render.GlHelper;

import net.minecraft.util.math.MathHelper;

public class Light extends NodeBase {
	
	// Type Related
	
	public enum Type {
		AMBIENT(0),
		POINT(1),
		SPOT(2),
		DIRECTIONAL(3);
		private final int index;
		private Type(int index) {
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
	}
	protected Type type;
	public Type getType() {if (this.type == null) return Type.AMBIENT; return type;}
	public void setType(Type type) {if (type == null) type = Type.AMBIENT; this.type = type;}
	public void setType(String type) {
		if (type.equals("ambient")) {
			this.setType(Type.AMBIENT);
		} else if (type.equals("point")) {
			this.setType(Type.POINT);
		} else if (type.equals("spot")) {
			this.setType(Type.SPOT);
		} else if (type.equals("directional")) {
			this.setType(Type.DIRECTIONAL);
		} else {
			System.out.println("Unknown type: " + type);
		}
	}
	
	// Color Related

	protected int color;
	public int getColor() {return color;}
	public void setColor(int color) {this.color = color;}
	public void setColorARGB(int a, int r, int g, int b) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorARGB(float a, float r, float g, float b) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorARGB(double a, double r, double g, double b) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorRGBA(int r, int g, int b, int a) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorRGBA(float r, float g, float b, float a) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorRGBA(double r, double g, double b, double a) {this.color = UtilsTex.color(r, g, b, a);}
	public void setColorRGB(int r, int g, int b) {this.color = UtilsTex.color(r, g, b);}
	public void setColorRGB(float r, float g, float b) {this.color = UtilsTex.color(r, g, b);}
	public void setColorRGB(double r, double g, double b) {this.color = UtilsTex.color(r, g, b);}
	
	// Attenuations
	
	public static final double[] DEFAULT_ATTENUATIONS = {1, 0, 0};
	protected double[] attenuations = DEFAULT_ATTENUATIONS;
	
	public static final int ATT_CONSTANT = 0;
	public static final int ATT_LINEAR = 1;
	public static final int ATT_QUADRATIC = 2;
	
	public double getAttenuation(int type) {return this.attenuations[type];}
	public void setAttenuation(int type, double val) {this.attenuations[type] = val;}
	
	public Vector getAttenuations() {return new Vector(this.attenuations);}
	public void setAttenuations(int type, Vector val) {
		this.attenuations = DEFAULT_ATTENUATIONS;
		if (val == null) return;
		for (int i = 0; i < Math.min(3, val.getDimension()); ++i)
			this.attenuations[i] = val.get(i);
	}
	
	// Spots
	
	public static final Vector DEFAULT_DIRECTION = new Vector(0, 0, -1, 0);
	protected static final Vector DEFAULT_DIRTMP = new Vector(1, 0, 0, 0);
	protected Vector direction = DEFAULT_DIRECTION;
    protected Vector dirtmp = DEFAULT_DIRTMP;
	
	public Vector getInnerDirection() {return direction;}
	public void setInnerDirection(Vector dir) {
		if (dir == null) dir = DEFAULT_DIRECTION; 
		this.direction = dir;
		this.dirtmp = Utils3D.genVerticalVector(direction);
	}
	
	protected Vector getDirTmp() {
		if (this.getInnerDirection() == null) return null;
		if (this.dirtmp == null) {
			this.dirtmp = Utils3D.genVerticalVector(this.getInnerDirection());
		}
		return this.dirtmp;
	}
	
	protected double spot_exponent;
	protected double spot_cutoff;
	
	public double getSpotExponent() {return spot_exponent;}
	public void setSpotExponent(double exp) {this.spot_exponent = exp;}
	public double getSpotCutoff() {return spot_cutoff;}
	public void setSpotCutoff(double cof) {this.spot_cutoff = cof;}
	
	protected String extra_dump_template = "TYPE: %s, COLOR: %s, ATTENUATION(C, L, Q): %s, SPOT_ARGS: D%s EXP%g COF%g";
	@Override
	protected String getExtraDumpInfo() {
		return String.format(extra_dump_template, this.getType(), Arrays.toString(UtilsTex.colorToRGBA(this.getColor())), this.getAttenuations(), this.getInnerDirection(), this.getSpotExponent(), this.getSpotCutoff());
	}
	
	public Light(State3DIntegrated state, String name) {
		super(state, name);
		// TODO 自动生成的构造函数存根
	}

	public Light(State3DSimple state, String name) {
		super(state, name);
		// TODO 自动生成的构造函数存根
	}

	public Light(Vector pos, Quaternion rot, Vector scl, String name) {
		super(pos, rot, scl, name);
		// TODO 自动生成的构造函数存根
	}

	public Light(Vector pos, Quaternion rot, Vector scl) {
		super(pos, rot, scl);
		// TODO 自动生成的构造函数存根
	}

	public Light(Vector pos, Quaternion rot, double scl) {
		super(pos, rot, scl);
		// TODO 自动生成的构造函数存根
	}

	public Light(Vector pos, Vector rot, Vector scl) {
		super(pos, rot, scl);
		// TODO 自动生成的构造函数存根
	}

	public Light(Vector pos, Quaternion rot) {
		super(pos, rot);
		// TODO 自动生成的构造函数存根
	}

	public Light(Vector pos) {
		super(pos);
	}

	public Light() {
	}

	public Light(Vector pos, Quaternion rot, double scl, String name) {
		super(pos, rot, scl, name);
	}

	public Light(Vector pos, Vector rot, Vector scl, String name) {
		super(pos, rot, scl, name);
	}

	public Light(Vector pos, Quaternion rot, String name) {
		super(pos, rot, name);
	}

	public Light(Vector pos, String name) {
		super(pos, name);
	}

	public Light(String name) {
		super(name);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public int getVertCount() {return 0;}
	@Override
	public int getEdgeCount() {return 0;}
	@Override
	public int getFaceCount() {return 0;}

	@Override
	public Vector getVertPos(int index) {return null;}
	@Override
	public Vector getVertNrm(int index) {return null;}
	@Override
	public Vector getVertUVM(int index) {return null;}
	@Override
	public boolean isEdgeLooped(int index) {return false;}
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
	@Override
	public Triad[] getFaceIndices(int index) {return null;}
	@Override
	public int getEdgeIndicesLength(int index) {return 0;}
	@Override
	public int getFaceIndicesLength(int index) {return 0;}
	
	
	private static final double[][] d = {{0, 1, 0, 1}, {0, -1, 0, 1}, {1, 0, 0, 1}, {0, 0, 1, 1}, {-1, 0, 0, 1}, {0, 0, -1, 1}};
	private static final Matrix vertices = new Matrix(d).mult(0.1);
	private Matrix vcur = vertices;
	
	@Override
	public void onRender(double ptick, boolean reverse_normal) {
		
	}
	
	@Override
	public void renderDebug(double ptick) {
		super.renderDebug(ptick);
		
		GlHelper renderer = GlHelper.getInstance();
		renderer.setColor(this.getColor());
		
		Vector resize_vec = this.getGlobalState().decomp().getScl().power(-1);
		Matrix resize_mat = Utils3D.sclToHomoState(resize_vec);
		
		
		vcur = vertices;
		vcur = vcur.mult(resize_mat);
		if (this.getType() == Type.AMBIENT) {
			renderer.setLineWidth(3);
			vcur = Utils3D.zoom(vcur, 4);
		} else if (this.getType() == Type.DIRECTIONAL) {
			vcur = Utils3D.zoom(vcur, 2);
		}
		
		for (int i = 0; i < 15; ++i) {
			int[] v_ = this.getEdgeIndices(i);
			renderer.startDrawingEdge(this.isEdgeLooped(i));
			Vector vpos;
			for (int v: v_) {
				vpos = vcur.getLine(v);
				renderer.addVertex(vpos);
			}
			renderer.endDrawing();
		}
		
		if (this.getType() == Type.DIRECTIONAL) {
			renderer.setLineWidth(3);
			Vector vdir = this.getInnerDirection().get(0, 3).mult(resize_vec.get(0, 3));
			renderer.startDrawingEdge(false);
			renderer.addVertex(vdir.mult(-0.1));
			renderer.addVertex(vdir);
			renderer.endDrawing();
		}
		double scale_factor = 0.5;
		if (this.getType() == Type.POINT || this.getType() == Type.SPOT) {
			if (this.getAttenuation(ATT_CONSTANT) > 1e-6) {
				Matrix mtmp = Matrix.I4.mult(this.getAttenuation(ATT_CONSTANT)).mult(resize_mat).mult(scale_factor);
				Matrix mtpm = mtmp.mult(-1);
				for (int i = 0; i < 3; ++i) {
					renderer.startDrawingEdge(false);
					renderer.addVertex(mtmp.getLine(i));
					renderer.addVertex(mtpm.getLine(i));
					renderer.endDrawing();
				}
			}
			if (this.getAttenuation(ATT_LINEAR) > 1e-6) {
				Matrix mtmp = Matrix.I4.mult(this.getAttenuation(ATT_LINEAR)).mult(resize_mat).mult(scale_factor);
				Matrix mtpm = mtmp.mult(-1);
				renderer.startDrawingEdge(true);
				renderer.addVertex(mtmp.getLine(0));
				renderer.addVertex(mtmp.getLine(1));
				renderer.addVertex(mtpm.getLine(0));
				renderer.addVertex(mtpm.getLine(1));
				renderer.endDrawing();
				renderer.startDrawingEdge(true);
				renderer.addVertex(mtmp.getLine(0));
				renderer.addVertex(mtmp.getLine(2));
				renderer.addVertex(mtpm.getLine(0));
				renderer.addVertex(mtpm.getLine(2));
				renderer.endDrawing();
				renderer.startDrawingEdge(true);
				renderer.addVertex(mtmp.getLine(2));
				renderer.addVertex(mtmp.getLine(1));
				renderer.addVertex(mtpm.getLine(2));
				renderer.addVertex(mtpm.getLine(1));
				renderer.endDrawing();
			}
			if (this.getAttenuation(ATT_QUADRATIC) > 1e-6) {
				Matrix mtmp = Matrix.I4.mult(this.getAttenuation(ATT_QUADRATIC)).mult(resize_mat).mult(scale_factor);
				Matrix mtpm = mtmp.mult(-1);
				renderer.startDrawingEdge(true);
				renderer.addVertex(mtmp.getLine(0));
				renderer.addVertex(mtmp.getLine(1).add(mtmp.getLine(0)).mult(0.7071));
				renderer.addVertex(mtmp.getLine(1));
				renderer.addVertex(mtmp.getLine(1).add(mtpm.getLine(0)).mult(0.7071));
				renderer.addVertex(mtpm.getLine(0));
				renderer.addVertex(mtpm.getLine(1).add(mtpm.getLine(0)).mult(0.7071));
				renderer.addVertex(mtpm.getLine(1));
				renderer.addVertex(mtpm.getLine(1).add(mtmp.getLine(0)).mult(0.7071));
				renderer.endDrawing();
				renderer.startDrawingEdge(true);
				renderer.addVertex(mtmp.getLine(0));
				renderer.addVertex(mtmp.getLine(2).add(mtmp.getLine(0)).mult(0.7071));
				renderer.addVertex(mtmp.getLine(2));
				renderer.addVertex(mtmp.getLine(2).add(mtpm.getLine(0)).mult(0.7071));
				renderer.addVertex(mtpm.getLine(0));
				renderer.addVertex(mtpm.getLine(2).add(mtpm.getLine(0)).mult(0.7071));
				renderer.addVertex(mtpm.getLine(2));
				renderer.addVertex(mtpm.getLine(2).add(mtmp.getLine(0)).mult(0.7071));
				renderer.endDrawing();
				renderer.startDrawingEdge(true);
				renderer.addVertex(mtmp.getLine(2));
				renderer.addVertex(mtmp.getLine(1).add(mtmp.getLine(2)).mult(0.7071));
				renderer.addVertex(mtmp.getLine(1));
				renderer.addVertex(mtmp.getLine(1).add(mtpm.getLine(2)).mult(0.7071));
				renderer.addVertex(mtpm.getLine(2));
				renderer.addVertex(mtpm.getLine(1).add(mtpm.getLine(2)).mult(0.7071));
				renderer.addVertex(mtpm.getLine(1));
				renderer.addVertex(mtpm.getLine(1).add(mtmp.getLine(2)).mult(0.7071));
				renderer.endDrawing();
			}
		}
		scale_factor = 3;
		if (this.getType() == Type.SPOT) {
			Vector vdir = this.getInnerDirection().get(0, 3).mult(resize_vec.get(0, 3));
			renderer.startDrawingEdge(false);
			renderer.addVertex(Vector.UNIT0_D3);
			renderer.addVertex(vdir.mult(scale_factor));
			renderer.endDrawing();
			Vector v1 = this.getDirTmp().get(0, 3).normalize().mult(resize_vec.get(0, 3));
			Vector v2 = this.getInnerDirection().get(0, 3).cross(v1).normalize().mult(resize_vec.get(0, 3));
			
			float cutoff = (float) ((this.getSpotCutoff() * Math.PI / 180) / 2);
			Vector vdir_tmp = vdir.mult(scale_factor);
			Vector vdir_tmp_ = vdir.mult(MathHelper.cos(cutoff) * scale_factor);
			Vector vdir_oxt1 = v1.mult(scale_factor);
			Vector vdir_oxt2 = v2.mult(scale_factor);
			Vector v1t = v1.mult(MathHelper.sin(cutoff) * scale_factor);
			Vector v2t = v2.mult(MathHelper.sin(cutoff) * scale_factor);
			
			renderer.startDrawingEdge(true);
			for (float angle = 0; angle < Math.PI * 2; angle += Math.PI / 16) {
				renderer.addVertex(vdir_tmp_.add(v1t.mult(MathHelper.cos(angle)).add(v2t.mult(MathHelper.sin(angle)))));
			}
			renderer.endDrawing();
			
			renderer.startDrawingEdge(true);
			renderer.addVertex(Vector.UNIT0_D3);
			for (float angle = -cutoff; angle < cutoff * 1.1; angle += cutoff / 10) {
				renderer.addVertex(vdir_tmp.mult(MathHelper.cos(angle)).add(vdir_oxt1.mult(MathHelper.sin(angle))));
			}
			renderer.endDrawing();
			renderer.startDrawingEdge(true);
			renderer.addVertex(Vector.UNIT0_D3);
			for (float angle = -cutoff; angle < cutoff * 1.1; angle += cutoff / 10) {
				renderer.addVertex(vdir_tmp.mult(MathHelper.cos(angle)).add(vdir_oxt2.mult(MathHelper.sin(angle))));
			}
			renderer.endDrawing();
		}
		
	}
	
}

package com.billstark001.riseui.core.particles;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.base.object.BaseObject;
import com.billstark001.riseui.base.object.IRenderable;
import com.billstark001.riseui.base.tag.ITag;
import com.billstark001.riseui.core.polygon.Presets;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Utils;
import com.billstark001.riseui.math.Vector;

public class Particle implements IRenderable{
	
	private IRenderable obj;
	private ParticleGroup gparent;
	
	private Vector pos, posa;
	private Quaternion rot, rota;
	private Vector scale, scalea;
	
	public Vector getPos() {return pos;}
	public Quaternion getRot() {return rot;}
	public Vector getScale() {return scale;}	

	public Vector getPosA() {return posa;}
	public Quaternion getRotA() {return rota;}
	public Vector getScaleA() {return scalea;}
	
	public void setPos(Vector pos) {this.pos = pos;}
	public void setRot(Quaternion rot) {this.rot = rot;}
	public void setRot(Vector rot) {this.rot = Quaternion.eulerToQuat(rot);}
	public void setScale(Vector scale) {this.scale = scale;}
	public void setScale(double scale) {this.scale = new Vector(scale, scale, scale);}
	
	public void setPosA(Vector posa) {this.posa = posa;}
	public void setRotA(Quaternion rota) {
		if (rota.isAxisAndAngle()) this.rota = rota;
		else this.rota = Quaternion.reverseAxisRotate(rota);
	}
	public void setRotA(Vector rota) {this.rota = Quaternion.reverseAxisRotate(Quaternion.eulerToQuat(rota));}
	public void setScaleA(Vector scalea) {this.scalea = scalea;}
	public void setScaleA(double scalea) {this.scalea = new Vector(scalea, scalea, scalea);}
	
	public void offset(Vector v) {setPos(Utils.compOffset(pos, v));}
	public void rotate(Quaternion q) {setRot(Utils.compRotate(rot, q));}
	public void rotate(Vector v) {setRot(Utils.compRotate(rot, Quaternion.reverseAxisRotate(Quaternion.eulerToQuat(v))));}
	public void zoom(Vector v) {setScale(Utils.compZoom(scale, v));}
	public void zoom(double d) {setScale(Utils.compZoom(scale, new Vector(d, d, d)));}
	
	public void offsetA(Vector v) {setPosA(Utils.compOffset(posa, v));}
	public void rotateA(Quaternion q) {setRotA(Utils.compRotate(rota, q));}
	public void rotateA(Vector v) {setRotA(Utils.compRotate(rota, Quaternion.eulerToQuat(v)));}
	public void zoomA(Vector v) {setScaleA(Utils.compZoom(scalea, v));}
	public void zoomA(double d) {setScaleA(Utils.compZoom(scalea, new Vector(d, d, d)));}
	
	private void set1(IRenderable obj, ParticleGroup gparent) {
		//super();
		this.obj = obj;
		if (obj == null) obj = Presets.getMesh("sphere_low_lod");
		((BaseObject) obj).setScale(0.125);
		this.gparent = gparent;
	}
	
	private void initInfo(Vector pos, Quaternion rot, Vector scale) {
		posa = Vector.Zeros(3);
		rota = Quaternion.AxisAndAngle(new Vector(0, 1, 0), 0);
		scalea = new Vector(1, 1, 1);
		if (pos == null) pos = posa;
		if (rot == null) rot = Quaternion.UNIT;
		if (scale == null) scale = scalea;
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
	}
	
	public Particle(IRenderable obj, ParticleGroup gparent) {
		set1(obj, gparent);
		initInfo(null, null, null);
	}

	public Particle(Vector pos, Quaternion rot, Vector scale, IRenderable obj, ParticleGroup gparent) {
		set1(obj, gparent);
		initInfo(pos, rot, scale);
	}
	
	public Particle(Vector pos, Quaternion rot, IRenderable obj, ParticleGroup gparent) {
		set1(obj, gparent);
		initInfo(pos, rot, null);
	}

	public Particle(Vector pos, IRenderable obj, ParticleGroup gparent) {
		set1(obj, gparent);
		initInfo(pos, null, null);
	}
	
	public Particle(ParticleGroup gparent) {this(null, gparent);}

	public void setParent(ParticleGroup p) {this.gparent = p;}
	public BaseObject getParent() {return gparent;}
	
	//Physics Simulation
	
	public void simulate(double delta) {
		scale = Utils.compZoom(scale, scalea.mult(delta));
		rot = Utils.compRotate(rot, Quaternion.axisRotate(rota.getImaginary(), rota.getReal() * delta));
		pos = Utils.compOffset(pos, posa.mult(delta));
	}
	
	//Render
	
	public IRenderable getObject() {
		return obj;
	}

	public void setObject(IRenderable obj) {
		if (obj == gparent || obj instanceof Particle) return;
		this.obj = obj;
	}

	@Override
	public void render() {
		Vector r = this.rot.getImaginary();
		GL11.glPushMatrix();
		GL11.glTranslated(this.pos.get(0), this.pos.get(1), this.pos.get(2));
		GL11.glRotated(this.rot.getReal(), r.get(0), r.get(1), r.get(2));
		GL11.glScaled(this.scale.get(0), this.scale.get(1), this.scale.get(2));
		this.obj.render();
		GL11.glPopMatrix();
	}

}

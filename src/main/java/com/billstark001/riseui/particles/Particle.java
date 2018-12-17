package com.billstark001.riseui.particles;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.IRenderable;
import com.billstark001.riseui.objects.Presets;
import com.billstark001.riseui.tags.ITag;

public class Particle extends BaseObject implements IRenderable{
	
	private IRenderable obj;
	private ParticleGroup gparent;
	
	public static final Particle PARTICLE_ROOT = new Particle(true);

	private void set1(IRenderable obj, ParticleGroup gparent) {
		//super();
		this.obj = obj;
		this.parent = PARTICLE_ROOT;
		if (obj == null) obj = Presets.getMesh("sphere_low_lod");
		this.gparent = gparent;
	}
	
	public Particle(IRenderable obj, ParticleGroup gparent) {
		super();
		set1(obj, gparent);
	}

	public Particle(Vector pos, Quaternion rot, Vector scale, IRenderable obj, ParticleGroup gparent) {
		super(pos, rot, scale);
		set1(obj, gparent);
	}
	
	public Particle(Vector pos, Quaternion rot, IRenderable obj, ParticleGroup gparent) {
		super(pos, rot);
		set1(obj, gparent);
	}

	public Particle(Vector pos, IRenderable obj, ParticleGroup gparent) {
		super(pos);
		set1(obj, gparent);
	}
	
	public Particle(ParticleGroup gparent) {this(null, gparent);}
	
	public Particle(boolean b) {
		this.pos = new Vector(0, 0, 0);
		this.rot = Quaternion.UNIT;
		this.scale = new Vector(1, 1, 1);
		this.parent = this;
	}

	@Override
	public boolean addTag(ITag t) {return false;}
	@Override
	public boolean addChild(BaseObject obj) {return false;}
	@Override
	public boolean setParent(BaseObject parent) {return false;}
	@Override
	public boolean removeParent() {return false;}
	@Override
	public boolean removeChild(int index) {return false;}
	@Override
	public boolean removeChild(BaseObject obj) {return false;}
	@Override
	public boolean removeAllChildren() {return false;}
	@Override
	public BaseObject getParent() {return gparent;}
	public void setParent(ParticleGroup p) {this.gparent = p;}
	
	//Physics Simulation

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

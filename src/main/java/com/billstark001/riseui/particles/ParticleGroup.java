package com.billstark001.riseui.particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.IRenderable;
import com.billstark001.riseui.tags.ITag;

public class ParticleGroup extends BaseObject implements IRenderable {
	
	private ArrayList<Particle> parts = new ArrayList<Particle>();
	protected List<ParticleGeneratorBase> gens = new ArrayList<ParticleGeneratorBase>();
	protected List<ParticleFieldBase> fields = new ArrayList<ParticleFieldBase>();

	//Construction
	
	public ParticleGroup() {super(new Vector(0, 0, 0), Quaternion.UNIT, new Vector(1, 1, 1));}
	public ParticleGroup(Vector pos, Quaternion rot, Vector scale) {super(pos, rot, scale);}
	public ParticleGroup(Vector pos, Quaternion rot, double scale) {super(pos, rot, scale);}
	public ParticleGroup(Vector pos, Vector rot, Vector scale) {super(pos, rot, scale);}
	public ParticleGroup(Vector pos, Quaternion rot) {super(pos, rot);}
	public ParticleGroup(Vector pos) {super(pos);}
	
	//Tags, Generators and Force Fields' Handling
	
	@Override
	public boolean addTag(ITag tag) {
		if (tag instanceof ParticleGeneratorBase) {gens.add((ParticleGeneratorBase) tag); return true;}
		else if(tag instanceof ParticleFieldBase) {fields.add((ParticleFieldBase) tag); return true;}
		else return super.addTag(tag);
	}
	public boolean addGenerator(ParticleGeneratorBase gen) {return addTag(gen);}
	public boolean addForceField(ParticleFieldBase field) {return addTag(field);}
	
	@Override
	public boolean removeTag(ITag tag) {
		if (tag instanceof ParticleGeneratorBase && gens.contains(tag)) {gens.remove(tag); return true;}
		else if (tag instanceof ParticleFieldBase && fields.contains(tag)) {fields.remove(tag); return true;}
		else return super.removeTag(tag);
	}
	public boolean removeGenerator(ParticleGeneratorBase gen) {return removeTag(gen);}
	public boolean removeForceField(ParticleFieldBase field) {return removeTag(field);}
	
	public void handleTags() {
		for (int i = 0; i < gens.size(); ++i) gens.get(i).reactOn(this);
		for (int i = 0; i < fields.size(); ++i) fields.get(i).reactOn(this);
		for (int i = 0; i < tags.size(); ++i) tags.get(i).reactOn(this);
	}
	
	//Particles' operation for Tags
	
	public void generateParticle(Particle part) {
		this.parts.add(part);
	}
	
	public void generateParticles(ArrayList<Particle> parts) {
		this.parts.addAll(parts);
	}
	
	public void killParticle(Particle part) {
		this.parts.remove(part);
	}
	
	public void killParticles(ArrayList<Particle> parts) {
		this.parts.removeAll(parts);
	}
	
	public Particle[] getParticles() {
		return this.parts.toArray(new Particle[0]);
	}
	
	@Override
	public void render() {
		Vector r = this.rot.getImaginary();
		GL11.glPushMatrix();
		GL11.glTranslated(this.pos.get(0), this.pos.get(1), this.pos.get(2));
		GL11.glRotated(this.rot.getReal(), r.get(0), r.get(1), r.get(2));
		GL11.glScaled(this.scale.get(0), this.scale.get(1), this.scale.get(2));
		for (Particle p: this.parts) p.render();
		GL11.glPopMatrix();
	}
}

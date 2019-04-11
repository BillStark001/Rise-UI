package com.billstark001.riseui.core.particles;

import com.billstark001.riseui.base.object.BaseObject;
import com.billstark001.riseui.base.object.ITickable;
import com.billstark001.riseui.base.tag.ITag;

public abstract class ParticleFieldBase implements ITag{

	/*
	 * The base class of particle fields.
	 * A field is at least capable of changing particles' accelerations and killing them.
	 * It can also do something fascinating like generating B-Spline tracks. 
	 */
	
	public ParticleFieldBase() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean suits(BaseObject object) {
		if (object instanceof ParticleGroup) return true;
		else return false;
	}

	public abstract void affect(ParticleGroup group);

	@Override
	public final void callOnTick(ITickable object) {
		affect((ParticleGroup) object);
	}

}

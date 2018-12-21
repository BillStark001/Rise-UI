package com.billstark001.riseui.particles;

import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.ITickable;
import com.billstark001.riseui.tags.ITag;

public abstract class ParticleGeneratorBase implements ITag{

	public ParticleGeneratorBase() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean suits(BaseObject object) {
		if (object instanceof ParticleGroup) return true;
		else return false;
	}
	
	public abstract void generate(ParticleGroup group);

	@Override
	public final void callOnTick(ITickable object) {
		generate((ParticleGroup) object);
	}

}

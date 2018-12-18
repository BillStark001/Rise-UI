package com.billstark001.riseui.tags;

import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.IRenderable;
import com.billstark001.riseui.particles.ParticleGroup;
import com.billstark001.riseui.particles.Particle;

public class TowardsTarget implements ITag {

	public TowardsTarget() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean suits(BaseObject object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void callOnHandle(BaseObject object) {
		if (object instanceof ParticleGroup) {
			//for (Particle p: ((ParticleGroup) object).getParticles()) callOnHandle(p);
			return;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void callOnRender(IRenderable object) {
		// TODO Auto-generated method stub
		
	}

}

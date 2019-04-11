package com.billstark001.riseui.base.tag;

import com.billstark001.riseui.base.object.BaseObject;
import com.billstark001.riseui.base.object.ICompilable;
import com.billstark001.riseui.base.object.IRenderable;
import com.billstark001.riseui.base.object.ITickable;
import com.billstark001.riseui.core.particles.Particle;
import com.billstark001.riseui.core.particles.ParticleGroup;

public class TagTowardsTarget implements ITag {

	public TagTowardsTarget() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean suits(BaseObject object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void callOnTick(ITickable object) {
		if (object instanceof ParticleGroup) {
			//for (Particle p: ((ParticleGroup) object).getParticles()) callOnHandle(p);
			return;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void callBeforeRender(IRenderable object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callOnCompile(ICompilable object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callAfterRender(IRenderable object) {
		// TODO Auto-generated method stub
		
	}

}

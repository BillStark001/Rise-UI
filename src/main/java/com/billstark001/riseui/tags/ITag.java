package com.billstark001.riseui.tags;

import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.ICompilable;
import com.billstark001.riseui.objects.IRenderable;
import com.billstark001.riseui.objects.ITickable;

public interface ITag {
	
	public boolean suits(BaseObject object);
	
	public void callOnCompile(ICompilable object);
	public void callOnTick(ITickable object);
	public void callBeforeRender(IRenderable object);
	public void callAfterRender(IRenderable object);
	
}

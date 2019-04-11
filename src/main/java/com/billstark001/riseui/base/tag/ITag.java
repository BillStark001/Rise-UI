package com.billstark001.riseui.base.tag;

import com.billstark001.riseui.base.object.BaseObject;
import com.billstark001.riseui.base.object.ICompilable;
import com.billstark001.riseui.base.object.IRenderable;
import com.billstark001.riseui.base.object.ITickable;

public interface ITag {
	
	public boolean suits(BaseObject object);
	
	public void callOnCompile(ICompilable object);
	public void callOnTick(ITickable object);
	public void callBeforeRender(IRenderable object);
	public void callAfterRender(IRenderable object);
	
}

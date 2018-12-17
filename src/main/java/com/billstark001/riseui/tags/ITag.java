package com.billstark001.riseui.tags;

import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.IRenderable;

public interface ITag {
	
	public boolean suits(BaseObject object);
	public void callOnHandle(BaseObject object);
	public void callOnRender(IRenderable object);
	
}

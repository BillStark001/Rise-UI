package com.billstark001.riseui.tags;

import com.billstark001.riseui.objects.BaseObject;

public interface ITag {
	
	public boolean suits(BaseObject object);
	public void reactOn(BaseObject object);
	
}

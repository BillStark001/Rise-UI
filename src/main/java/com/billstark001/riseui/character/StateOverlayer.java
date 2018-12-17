package com.billstark001.riseui.character;

import java.util.ArrayList;
import java.util.List;

import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.tags.ITag;

public class StateOverlayer implements ITag{
	
	private List<IState> states = new ArrayList<IState>(); 
	
	public void addState(IState s) {states.add(s);}
	public void removeState(IState s) {states.remove(s);}
	@Override
	public boolean suits(BaseObject object) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void reactOn(BaseObject object) {
		// TODO Auto-generated method stub
		
	}
	
}

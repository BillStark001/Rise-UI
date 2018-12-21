package com.billstark001.riseui.character;

import java.util.ArrayList;
import java.util.List;

import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.objects.BaseObject;
import com.billstark001.riseui.objects.ICompilable;
import com.billstark001.riseui.objects.IRenderable;
import com.billstark001.riseui.objects.ITickable;
import com.billstark001.riseui.tags.ITag;

public class TagApplyState implements ITag{
	
	private List<IState> states = new ArrayList<IState>(); 
	
	public void addState(IState s) {states.add(s);}
	public void removeState(IState s) {states.remove(s);}
	@Override
	public boolean suits(BaseObject object) {
		// TODO Auto-generated method stub
		return false;
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
	public void callOnTick(ITickable object) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void callAfterRender(IRenderable object) {
		// TODO Auto-generated method stub
		
	}
	
}

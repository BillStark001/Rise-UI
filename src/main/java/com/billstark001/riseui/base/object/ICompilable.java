package com.billstark001.riseui.base.object;

public interface ICompilable {

	public void compileList();
	public int getDisplayList();
	
	public boolean isCompiled();
	public void markRecompile();
	
}

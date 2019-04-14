package com.billstark001.riseui.base;

public interface ICompilable {

	public void compileList();
	public int getDisplayList();
	
	public boolean isCompiled();
	public void markRecompile();
	
}

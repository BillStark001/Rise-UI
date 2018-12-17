package com.billstark001.riseui.objects;

public interface ICompilable {

	public void compileList();
	public int getDisplayList();
	
	public boolean isCompiled();
	public void markRecompile();
	
}

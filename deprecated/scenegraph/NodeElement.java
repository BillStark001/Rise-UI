package com.billstark001.riseui.math.scenegraph;

public abstract class NodeElement<T> {

	private T global;
	private T local;
	
	private boolean global_dirty;
	private boolean local_dirty;
	
	private final Node node;
	private final int index;
	
	public NodeElement(Node node, int index) {
		this.node = node;
		this.index = index;
	}
	
	public abstract void initVar();
	
	public final void setGlobalDirty() {global_dirty = true;}
	public final void setLocalDirty() {local_dirty = true;}
	public final boolean getGlobalDirty() {return global_dirty;}
	public final boolean getLocalDirty() {return local_dirty;}
	
	public void setGlobal(T global) {
		this.global = global;
		this.global_dirty = false;
		this.local = clarifyLocal(global);
	}
	public void setLocal(T local) {
		this.local = local;
		this.local_dirty = false;
		this.global = clarifyGlobal(local);
		this.downgradeGlobal();
	}
	public T getGlobal() {
		clarifyGlobal(local);
		return global;
	}
	public T getLocal() {
		return local;
	}
	
	public abstract T clarifyGlobal(T local);
	public abstract T clarifyLocal(T global);
	
	public final void downgradeGlobal() {
		
	}
	
}

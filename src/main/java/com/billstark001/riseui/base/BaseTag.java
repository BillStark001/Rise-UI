package com.billstark001.riseui.base;

public abstract class BaseTag extends BaseObject{
	
	protected int hierarchy;
	protected boolean activated;
	
	public BaseTag() {this(0, true);}
	public BaseTag(boolean activated) {this(0, activated);}
	public BaseTag(int hierarchy) {this(hierarchy, true);}
	public BaseTag(int hierarchy, boolean activated) {
		this.hierarchy = hierarchy;
		this.activated = activated;
	}
	
	public int getHierarchy() {return hierarchy;}
	public void setHierarchy(int hierarchy) {this.hierarchy = hierarchy;}
	public boolean isActivated() {return activated;}
	public void setActivated(boolean activated) {this.activated = activated;}
	
	public abstract void onGlobalUpdate(BaseNode node);
	public abstract void onLocalUpdate(BaseNode node);
	public abstract void onRenderPre(BaseNode object);
	public abstract void onRenderPost(BaseNode object);
	
}

package com.billstark001.riseui.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.billstark001.riseui.math.Pair;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class BaseTag extends BaseObject {
	protected int hierarchy;
	protected boolean activated;
	
	public BaseTag() {this(0, true);}
	public BaseTag(boolean activated) {this(0, activated);}
	public BaseTag(int hierarchy) {this(hierarchy, true);}
	public BaseTag(int hierarchy, boolean activated) {
		this.hierarchy = hierarchy;
		this.activated = activated;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public int getHierarchy() {return hierarchy;}
	public void setHierarchy(int hierarchy) {this.hierarchy = hierarchy;}
	public boolean isActivated() {return activated;}
	public void setActivated(boolean activated) {this.activated = activated;}
	
	@SubscribeEvent
	public abstract void update(TickEvent e);
	
	public abstract void onAdd(BaseNode node);
	public abstract void onRemove(BaseNode node);
	public abstract StateContainer onGlobalUpdate(Vector p, Quaternion r, Vector s);
	public abstract StateContainer onLocalUpdate(Vector p, Quaternion r, Vector s);
	public abstract void onRenderPre(BaseNode object, double ptick);
	public abstract void onRenderPost(BaseNode object, double ptick);
	
	public static void sortTags(List<BaseTag> tags) {tags.sort(tagcomp);}
	private static Comparator<BaseTag> tagcomp = new Comparator<BaseTag>() {
		@Override
		public int compare(BaseTag t1, BaseTag t2) {
			return t1.hierarchy - t2.hierarchy;
		}
	};
	
}

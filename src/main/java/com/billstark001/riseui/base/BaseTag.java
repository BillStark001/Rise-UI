package com.billstark001.riseui.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.billstark001.riseui.base.BaseTag.ApplicationExtra;
import com.billstark001.riseui.base.state.SimpleState;
import com.billstark001.riseui.base.state.StateStandard3D;
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
	
	public static final int TAG_PHRASE_ADDED = -2;
	public static final int TAG_PHRASE_REMOVED = -1;
	
	public static final int TAG_PHRASE_RENDER_PRE = 0;
	public static final int TAG_PHRASE_RENDER_POST = 4;
	
	public static final int TAG_PHRASE_RENDER_VERTICES = 1;
	public static final int TAG_PHRASE_RENDER_EDGES = 2;
	public static final int TAG_PHRASE_RENDER_FACES = 3;
	
	public static final int TAG_PHRASE_RENDER_PARTICULAR_VERTEX = 9;
	public static final int TAG_PHRASE_RENDER_PARTICULAR_EDGE = 10;
	public static final int TAG_PHRASE_RENDER_PARTICULAR_FACE = 11;
	
	public static final int TAG_PHRASE_GLOBAL_UPDATE = 5;
	public static final int TAG_PHRASE_LOCAL_UPDATE = 6;
	public static final int TAG_PHRASE_EFFECTOR_ACTIVATED = 7;
	public static final int TAG_PHRASE_GENERATOR_ACTIVATED = 8;
	
	public abstract boolean appliesOn(int phrase);
	public ApplicationReturn applyOn(int phrase, BaseNode object) {return applyOn(phrase, object, new ApplicationExtra());}
	public ApplicationReturn applyOn(int phrase, BaseNode object, ApplicationExtra extra) {
		if (extra == null) extra = BaseTag.getDummyExtra();
		switch (phrase) {
		case BaseTag.TAG_PHRASE_RENDER_PRE:
			return this.onRenderPre(object, extra.patrial_ticks);
		case BaseTag.TAG_PHRASE_RENDER_POST:
			return this.onRenderPost(object, extra.patrial_ticks);
			
		case BaseTag.TAG_PHRASE_RENDER_VERTICES:
			return this.onRenderVerts(object, extra.patrial_ticks);
		case BaseTag.TAG_PHRASE_RENDER_EDGES:
			return this.onRenderEdges(object, extra.patrial_ticks);
		case BaseTag.TAG_PHRASE_RENDER_FACES:
			return this.onRenderFaces(object, extra.patrial_ticks);
			
		case BaseTag.TAG_PHRASE_RENDER_PARTICULAR_VERTEX:
			return this.onRenderVert(object, extra.element_index, extra.patrial_ticks);
		case BaseTag.TAG_PHRASE_RENDER_PARTICULAR_EDGE:
			return this.onRenderEdge(object, extra.element_index, extra.patrial_ticks);
		case BaseTag.TAG_PHRASE_RENDER_PARTICULAR_FACE:
			return this.onRenderFace(object, extra.element_index, extra.patrial_ticks);
			
		case BaseTag.TAG_PHRASE_GLOBAL_UPDATE:
			return this.onGlobalUpdate(object);
		case BaseTag.TAG_PHRASE_LOCAL_UPDATE:
			return this.onGlobalUpdate(object);
		case BaseTag.TAG_PHRASE_ADDED:
			return this.onAdded(object);
		case BaseTag.TAG_PHRASE_REMOVED:
			return this.onRemoved(object);
		default:
			return new ApplicationReturn();
		}
	}
	public static class ApplicationExtra {
		public double patrial_ticks;
		public int element_index;
		public ApplicationExtra() {}
		public ApplicationExtra(double pt) {this.patrial_ticks = pt;}
		public ApplicationExtra(int i) {this.element_index = i;}
		public ApplicationExtra(double pt, int i) {
			this.patrial_ticks = pt;
			this.element_index = i;
		}
	}
	public static class ApplicationReturn {
		public boolean succeed = false;
		public ApplicationReturn() {}
		public ApplicationReturn(boolean succ) {this.succeed = succ;}
	}
	
	
	public abstract ApplicationReturn onAdded(BaseNode node);
	public abstract ApplicationReturn onRemoved(BaseNode node);
	public abstract ApplicationReturn onGlobalUpdate(BaseNode state);
	public abstract ApplicationReturn onLocalUpdate(BaseNode state);
	public abstract ApplicationReturn onRenderPre(BaseNode object, double ptick);
	public abstract ApplicationReturn onRenderPost(BaseNode object, double ptick);
	public abstract ApplicationReturn onRenderVerts(BaseNode object, double ptick);
	public abstract ApplicationReturn onRenderEdges(BaseNode object, double ptick);
	public abstract ApplicationReturn onRenderFaces(BaseNode object, double ptick);
	public abstract ApplicationReturn onRenderVert(BaseNode object, int index, double ptick);
	public abstract ApplicationReturn onRenderEdge(BaseNode object, int index, double ptick);
	public abstract ApplicationReturn onRenderFace(BaseNode object, int index, double ptick);
	
	public static void sortTags(List<BaseTag> tags) {tags.sort(tagcomp);}
	private static Comparator<BaseTag> tagcomp = new Comparator<BaseTag>() {
		@Override
		public int compare(BaseTag t1, BaseTag t2) {
			return t1.hierarchy - t2.hierarchy;
		}
	};

	public static ApplicationExtra getDummyExtra() {return new ApplicationExtra();}
	public static ApplicationExtra getExtraWithPTick(double ptick) {return new ApplicationExtra(ptick);}
	public static ApplicationReturn getDummyReturn() {return new ApplicationReturn();}
	
}

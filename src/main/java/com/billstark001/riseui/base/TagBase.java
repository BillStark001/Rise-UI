package com.billstark001.riseui.base;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.billstark001.riseui.base.fields.Operator;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class TagBase extends BaseContainer {
	protected int hierarchy;
	protected boolean activated;
	
	public abstract boolean onMat();
	public abstract boolean onShader();
	public abstract boolean onData();
	
	public abstract Operator getShaderOpr();
	public abstract Operator getDataOpr();
	
	public TagBase() {this(0, true);}
	public TagBase(boolean activated) {this(0, activated);}
	public TagBase(int hierarchy) {this(hierarchy, true);}
	public TagBase(int hierarchy, boolean activated) {
		this.hierarchy = hierarchy;
		this.activated = activated;
		//MinecraftForge.EVENT_BUS.register(this);
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
	
	public static final int TAG_PHRASE_RENDER_VERTICES_PRE = 1;
	public static final int TAG_PHRASE_RENDER_EDGES_PRE = 2;
	public static final int TAG_PHRASE_RENDER_FACES_PRE = 3;
	
	public static final int TAG_PHRASE_RENDER_VERTICES_POST = 12;
	public static final int TAG_PHRASE_RENDER_EDGES_POST = 13;
	public static final int TAG_PHRASE_RENDER_FACES_POST = 14;
	
	public static final int TAG_PHRASE_RENDER_PARTICULAR_VERTEX = 9;
	public static final int TAG_PHRASE_RENDER_PARTICULAR_EDGE = 10;
	public static final int TAG_PHRASE_RENDER_PARTICULAR_FACE = 11;
	
	// public static final int TAG_PHRASE_GLOBAL_UPDATE = 5;
	// public static final int TAG_PHRASE_LOCAL_UPDATE = 6;
	// public static final int TAG_PHRASE_EFFECTOR_ACTIVATED = 7;
	// public static final int TAG_PHRASE_GENERATOR_ACTIVATED = 8;
	
	public abstract boolean appliesOn(int phrase);
	
	public ApplyReturn applyOn(int phrase, NodeBase object, Object...args) {
		if (!this.appliesOn(phrase)) return null;
		double ptick = 0;
		int index = 0;
		int inform = -1;
		boolean inform_ = true;
		switch (phrase) {
		case TagBase.TAG_PHRASE_RENDER_PRE:
			ptick = (Double) args[0];
			return this.onRenderPre(object, ptick);
		case TagBase.TAG_PHRASE_RENDER_POST:
			ptick = (Double) args[0];
			return this.onRenderPost(object, ptick);
			
		case TagBase.TAG_PHRASE_RENDER_VERTICES_PRE:
			ptick = (Double) args[0];
			return this.onRenderVertsPre(object, ptick);
		case TagBase.TAG_PHRASE_RENDER_EDGES_PRE:
			ptick = (Double) args[0];
			return this.onRenderEdgesPre(object, ptick);
		case TagBase.TAG_PHRASE_RENDER_FACES_PRE:
			ptick = (Double) args[0];
			inform = (Integer) args[1];
			return this.onRenderFacesPre(object, ptick, inform);
		
		case TagBase.TAG_PHRASE_RENDER_VERTICES_POST:
			return this.onRenderVertsPost(object);
		case TagBase.TAG_PHRASE_RENDER_EDGES_POST:
			return this.onRenderEdgesPost(object);
		case TagBase.TAG_PHRASE_RENDER_FACES_POST:
			return this.onRenderFacesPost(object);
			
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_VERTEX:
			index = (Integer) args[0];
			ptick = (Double) args[1];
			return this.onRenderVert(object, index, ptick);
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_EDGE:
			index = (Integer) args[0];
			ptick = (Double) args[1];
			return this.onRenderEdge(object, index, ptick);
		case TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE:
			index = (Integer) args[0];
			ptick = (Double) args[1];
			inform_ = (Boolean) args[2];
			return this.onRenderFace(object, index, ptick, inform_);
			
		//case TagBase.TAG_PHRASE_GLOBAL_UPDATE:
		//	return this.onGlobalUpdate(object);
		//case TagBase.TAG_PHRASE_LOCAL_UPDATE:
		//	return this.onLocalUpdate(object);
		case TagBase.TAG_PHRASE_ADDED:
			return this.onAdded(object);
		case TagBase.TAG_PHRASE_REMOVED:
			return this.onRemoved(object);
		default:
			return new ApplyReturn();
		}
	}

	public static class ApplyReturn {
		public final boolean succeeded;
		public final Object[] data;
		public ApplyReturn(boolean succ, Object...datas) {
			this.succeeded = succ;
			this.data = datas;
		}
		public ApplyReturn() {this(true);}
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 */
	public abstract ApplyReturn onAdded(NodeBase node);
	/**
	 * 
	 * @param node
	 * @return
	 */
	public abstract ApplyReturn onRemoved(NodeBase node);
	/**
	 * 
	 * @param state
	 * @return
	 */
	//public abstract ApplyReturn onGlobalUpdate(NodeBase state);
	/**
	 * 
	 * @param state
	 * @return
	 */
	//public abstract ApplyReturn onLocalUpdate(NodeBase state);
	/**
	 * 
	 * @param object
	 * @param ptick
	 * @return
	 */
	public abstract ApplyReturn onRenderPre(NodeBase object, double ptick);
	/**
	 * 
	 * @param object
	 * @param ptick
	 * @return
	 */
	public abstract ApplyReturn onRenderPost(NodeBase object, double ptick);
	/**
	 * 
	 * @param object
	 * @param ptick
	 * @return
	 */
	public abstract ApplyReturn onRenderVertsPre(NodeBase object, double ptick);
	/**
	 * 
	 * @param object
	 * @param ptick
	 * @return
	 */
	public abstract ApplyReturn onRenderEdgesPre(NodeBase object, double ptick);
	/**
	 * 
	 * @param object
	 * @param ptick
	 * @param inform
	 * @return boolean succeeded, int paircount, shader[paircount] shaders, mat[paircount] mats
	 */
	public abstract ApplyReturn onRenderFacesPre(NodeBase object, double ptick, int inform);
	/**
	 * 
	 * @param object
	 * @return
	 */
	public abstract ApplyReturn onRenderVertsPost(NodeBase object);
	/**
	 * 
	 * @param object
	 * @return
	 */
	public abstract ApplyReturn onRenderEdgesPost(NodeBase object);
	/**
	 * 
	 * @param object
	 * @return
	 */
	public abstract ApplyReturn onRenderFacesPost(NodeBase object);
	/**
	 * 
	 * @param object
	 * @param index
	 * @param ptick
	 * @return boolean succeeded, boolean cancelled, (if not cancelled) boolean altered, (if altered) Vector[3] pos, Vector[2] uv, Vector[3] normal
	 */
	public abstract ApplyReturn onRenderVert(NodeBase object, int index, double ptick);
	/**
	 * 
	 * @param object
	 * @param index
	 * @param ptick
	 * @return boolean succeeded, boolean cancelled, (if not cancelled) boolean altered, (if altered) Vector[3] pos, Vector[2] uv, Vector[3] normal
	 */
	public abstract ApplyReturn onRenderEdge(NodeBase object, int index, double ptick);
	/**
	 * 
	 * @param object
	 * @param index
	 * @param ptick
	 * @param inform
	 * @return boolean succeeded, double alter_rate
	 */
	public abstract ApplyReturn onRenderFace(NodeBase object, int index, double ptick, boolean inform);
	
	public static void sortTags(List<TagBase> tags) {tags.sort(tagcomp);}
	private static Comparator<TagBase> tagcomp = new Comparator<TagBase>() {
		@Override
		public int compare(TagBase t1, TagBase t2) {
			return t1.hierarchy - t2.hierarchy;
		}
	};

	public static ApplyReturn getDummyReturn() {return new ApplyReturn();}
	
}

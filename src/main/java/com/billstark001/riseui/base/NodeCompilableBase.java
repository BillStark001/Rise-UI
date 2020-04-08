package com.billstark001.riseui.base;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.billstark001.riseui.base.TagBase.ApplyReturn;
import com.billstark001.riseui.base.states.StateBase;
import com.billstark001.riseui.base.states.simple3d.State3DSimple;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.Triad;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.render.GlHelper;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;

public abstract class NodeCompilableBase extends NodeBase {

	private boolean compiled;
	private int vao_id;
	private int vbo_vvert_id;
	private int vbo_fvert_id;
	private int vbo_evert_id;
	private int vbo_color_id;
	private int vbo_edge_index_id;
	private Map<TagBase, Integer> vbo_face_index_ids = null;
	private Map<TagBase, Integer> vbo_face_index_counts = null;
	
	public static final int VERTEX_VERT_STRIDE = 3;
	public static final int VERTEX_EDGE_STRIDE = 3;
	public static final int VERTEX_FACE_STRIDE = 3 + 2 + 3;
	public static final int COLOR_STRIDE = 4;
	
	public static final int EDGE_INDEX_STRIDE = 1;
	public static final int FACE_INDEX_STRIDE = 3;
	
	private double[][] face_cache_final_apply_rate = null;
	private TagBase[] face_cache_rpf_tags = null;
	private Map<Triad, Integer> face_cache_triad_mapping_map = null;
	
	public NodeCompilableBase () {
		super();
		this.markRecompile();
	}
	
	@Override
	public void setLocalState(StateBase<Matrix> state) {
		//this.markRecompile();
		super.setLocalState(state);
	}
	
	public boolean isCompiled() {return this.compiled;}
	public void markRecompile() {
		this.compiled = false;
		this.face_cache_final_apply_rate = null;
		this.face_cache_rpf_tags = null;
		this.face_cache_triad_mapping_map = null;
		if (this.vbo_color_id > 0) GL15.glDeleteBuffers(this.vbo_color_id);
		if (this.vbo_fvert_id > 0) GL15.glDeleteBuffers(this.vbo_fvert_id);
		if (this.vbo_vvert_id > 0) GL15.glDeleteBuffers(this.vbo_vvert_id);
		if (this.vbo_evert_id > 0) GL15.glDeleteBuffers(this.vbo_evert_id);
		if (this.vbo_edge_index_id > 0) GL15.glDeleteBuffers(this.vbo_edge_index_id);
		if (this.vbo_face_index_ids != null) {
			for (int id: this.vbo_face_index_ids.values()) 
				GL15.glDeleteBuffers(id);
			this.vbo_face_index_ids = null;
		}
		if (this.vbo_face_index_counts != null) {
			this.vbo_face_index_counts = null;
		}
		this.vao_id = this.vbo_vvert_id = this.vbo_fvert_id = this.vbo_evert_id = this.vbo_color_id = this.vbo_edge_index_id = 0;
	}
	
	@Override
	public void onTagsAltered() {
		super.onTagsAltered();
		this.face_cache_final_apply_rate = null;
		this.face_cache_rpf_tags = null;
	}
	
	// Abstract Methods
	/**
	 * Returns the number of vertices of this Node.
	 * @return int, the number
	 */
	public abstract int getVertCount();
	/**
	 * Returns the number of edges of this Node.
	 * @return int, the number
	 */
	public abstract int getEdgeCount();
	/**
	 * Returns the number of faces of this Node.
	 * @return int, the number
	 */
	public abstract int getFaceCount();
	
	public abstract int getPosMaxIndex();
	public abstract int getNormalMaxIndex();
	public abstract int getTexUVMaxIndex();

	public abstract Vector getVertPos(int index);
	public abstract Vector getVertNrm(int index);
	public abstract Vector getVertUVM(int index);
	
	public abstract boolean isEdgeLooped(int index);
	public abstract int[] getEdgeIndices(int index);
	/**
	 * 
	 * @param index
	 * @return [Triad(Pos, UV, Nrm)...]
	 */
	public abstract Triad[] getFaceIndices(int index);
	
	public abstract int getEdgeIndicesLength(int index);
	public abstract int getFaceIndicesLength(int index);
	
	public Vector[] getEdges(int index) {
		int[] indices = this.getEdgeIndices(index);
		Vector[] ans = new Vector[indices.length];
		for (int i = 0; i < ans.length; ++i) ans[i] = getVertPos(indices[i]);
		return ans;
	}
	
	
	
	@FunctionalInterface
	public interface InfoIndicator{public Vector getInfo(int index);}
	
	public static FloatBuffer storeVectorBuffer(int stride, int nr_element, InfoIndicator indic, Vector default_info) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(nr_element * stride);
		for (int i = 0; i < nr_element; ++i) {
			Vector info = indic.getInfo(i);
			info = (info == null ? default_info : info).get(0, stride);
			info.storeBufferF(buffer);
		}
		buffer.flip();
		return buffer;
	}
	public FloatBuffer storeVertBuffer() {return storeVectorBuffer(VERTEX_VERT_STRIDE, this.getPosMaxIndex(), this::getVertPos, Vector.UNIT0_D3);}
	//public FloatBuffer storeUVBuffer() {return storeVectorBuffer(VERTEX_FACE_STRIDE, this.getTexUVMaxIndex(), this::getVertUVM, Vector.UNIT0_D2);}
	//public FloatBuffer storeNormBuffer() {return storeVectorBuffer(VERTEX_EDGE_STRIDE, this.getNormalMaxIndex(), this::getVertNrm, Vector.ORTHO_Z3);}
	
	protected double buffered_vert_size;
	public ByteBuffer storeColorBuffer() {
		double ptick = 0;
		int nr_vert = this.getVertCount();
		ByteBuffer color_buffer = BufferUtils.createByteBuffer(nr_vert * COLOR_STRIDE);
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_PRE, ptick);
		buffered_vert_size = 1;
		int succeeded_count = 0;
		for (int i = 0; i < nr_vert; ++i) {
			List<ApplyReturn> rets = this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_VERTEX, i, ptick);
			int color = UtilsTex.color(255, 255, 255);
			double size = -1;
			for (ApplyReturn r: rets) {
				if (r.succeeded) {
					++succeeded_count;
					color = (Integer) r.data[0];
					size = (Double) r.data[1];
				}
			}
			if (size >= 0) buffered_vert_size += size;
			color_buffer.put(UtilsTex.colorToRGBAByte(color));
		}
		if (succeeded_count > 0) 
			buffered_vert_size /= succeeded_count;
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_POST, ptick);
		color_buffer.flip();
		return color_buffer;
	}
	/*
	protected double buffered_edge_size;
	public IntBuffer storeEdgeIndexBuffer() {
		double ptick = 0;
		int nr_edge = this.getVertCount();
		IntBuffer index_buffer = BufferUtils.createIntBuffer(nr_edge * EDGE_INDEX_STRIDE);
		this.applyTags(TagBase.TAG_PHRASE_RENDER_EDGES_PRE, ptick);
		buffered_edge_size = -1;
		for (int i = 0; i < nr_edge; ++i) {
			List<ApplyReturn> rets = this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_EDGE, i, ptick);
			int color = UtilsTex.color(255, 255, 255);
			double size = -1;
			for (ApplyReturn r: rets) {
				if (r.succeeded) {
					color = (Integer) r.data[0];
					size = (Double) r.data[1];
				}
			}
			if (size >= 0) buffered_edge_size += size;
			index_buffer.put(UtilsTex.colorToRGBAByte(color));
		}
		if (buffered_edge_size < 0) buffered_edge_size = 1; 
		else buffered_edge_size /= nr_edge;
		this.applyTags(TagBase.TAG_PHRASE_RENDER_EDGES_POST, ptick);
		index_buffer.flip();
		return index_buffer;
	}
	*/
	
	protected int getVAOId() {
		if (vao_id <= 0) vao_id = GL30.glGenVertexArrays();
		return vao_id;
	}
	
	protected void createVertBuffer() {
		vao_id = this.getVAOId();
        GL30.glBindVertexArray(vao_id);
        
        FloatBuffer vertices_buffer = this.storeVertBuffer();
        
        vbo_vvert_id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_vvert_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices_buffer, GL15.GL_STATIC_DRAW);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, VERTEX_VERT_STRIDE * 4, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        GL30.glBindVertexArray(0);
	}
	
	public void prepareVertRender() {
		if (vao_id <= 0 || vbo_vvert_id <= 0) this.createVertBuffer();
        GL30.glBindVertexArray(vao_id);
        
        ByteBuffer color_buffer = this.storeColorBuffer();
        
        vbo_color_id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_color_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, color_buffer, GL15.GL_STATIC_DRAW);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, COLOR_STRIDE, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        GL30.glBindVertexArray(0);
	}
	public void renderVertWithBuffer(double ptick) {
		if (vao_id <= 0 || vbo_vvert_id <= 0 || vbo_color_id <= 0) 
			this.prepareVertRender();
		
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_PRE, ptick);
		
        GlHelper.getInstance().setPointSize(this.buffered_vert_size);
		
        GL30.glBindVertexArray(vao_id);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        
        GL11.glDrawArrays(GL11.GL_POINTS, 0, this.getVertCount());
         
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL30.glBindVertexArray(0);
         
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_POST, ptick);
	}
	public void renderVertNaive(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_PRE, ptick);
		for (int i = 0; i < this.getVertCount(); ++i) {
			List<ApplyReturn> rets = this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_VERTEX, i, ptick);
			int color = UtilsTex.color(255, 255, 255);
			double size = 1;
			for (ApplyReturn r: rets) {
				if (r.succeeded) {
					color = (Integer) r.data[0];
					size = (Double) r.data[1];
				}
			}
			renderer.pushColors(color);
			renderer.pushSizes(size);
			Vector v = this.getVertPos(i);
			renderer.startDrawingVert();
			renderer.addVertex(v);
			renderer.endDrawing();
		}
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_POST, ptick);
	}
	public void renderVert(double ptick) {
		this.renderVertWithBuffer(ptick);
	}
	
	public void prepareEdgeRender() {
		if (vao_id <= 0 || vbo_vvert_id <= 0) this.createVertBuffer();
        GL30.glBindVertexArray(vao_id);
        
        ByteBuffer color_buffer = this.storeColorBuffer();
        
        vbo_color_id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_color_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, color_buffer, GL15.GL_STATIC_DRAW);
        GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, COLOR_STRIDE, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        GL30.glBindVertexArray(0);
	}
	public void renderEdgeWithBuffer(double ptick) {
		if (vao_id <= 0 || vbo_vvert_id <= 0 || vbo_color_id <= 0) 
			this.prepareVertRender();
		
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_PRE, ptick);
		
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        GL30.glBindVertexArray(vao_id);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        
        GL11.glDrawArrays(GL11.GL_POINTS, 0, this.getVertCount());
         
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL30.glBindVertexArray(0);
         
		this.applyTags(TagBase.TAG_PHRASE_RENDER_VERTICES_POST, ptick);
	}
	public void renderEdge(double ptick) {
		GlHelper renderer = GlHelper.getInstance();
		this.applyTags(TagBase.TAG_PHRASE_RENDER_EDGES_PRE, ptick);
		for (int i = 0; i < this.getEdgeCount(); ++i) {
			List<ApplyReturn> rets = this.applyTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_EDGE, i, ptick);
			int[] colors = null;
			double[] widths = null;
			for (ApplyReturn r: rets) {
				if (r.succeeded) {
					colors = (int[]) (r.data[0]);
					widths = (double[]) (r.data[1]);
				}
			}
			if (colors != null) renderer.pushColors(colors);
			if (widths != null) renderer.pushWidths(widths);
			int[] v_ = this.getEdgeIndices(i);
			renderer.startDrawingEdge(this.isEdgeLooped(i));
			for (int v: v_) {
				renderer.addVertex(this.getVertPos(v));
			}
			renderer.endDrawing();
		}
		this.applyTags(TagBase.TAG_PHRASE_RENDER_EDGES_POST, ptick);
	}
	
	
	public Vector concatPTN(Triad t) {
		Vector v1, v2, v3;
		v1 = this.getVertPos(t.getX());
		v2 = this.getVertUVM(t.getY());
		v3 = this.getVertNrm(t.getZ());
		v1 = (v1 == null ? Vector.UNIT0_D3 : v1).get(0, 3);
		v2 = (v2 == null ? Vector.UNIT0_D2 : v2).get(0, 2);
		v3 = (v3 == null ? Vector.ORTHO_Z3 : v3).get(0, 3);
		return v1.concatenate(v2).concatenate(v3);
	}
	public void prepareFaceRender(boolean reverse_normal) {
		double ptick = 0;
		int face_count = this.getFaceCount();
		TagBase[] rpf_tags = this.findTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE);
		double[][] final_apply_rate = new double[face_count][rpf_tags.length];
		for (int i = 0; i < face_count; ++i) {
			for (int j = 0; j < rpf_tags.length; ++j) {
				if (!rpf_tags[j].isActivated()) continue;
				TagBase.ApplyReturn ret = rpf_tags[j].applyOn(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, this, i, ptick, false);
				if (ret.succeeded) {
					double rate = (Double) ret.data[0];
					if (rate > (1 - 1e-6)) rate = 1;
					else if (rate < 1e-6) rate = 0;
					final_apply_rate[i][j] = rate;
					for (int k = 0; k < j; ++k) final_apply_rate[i][k] *= (1 - rate);
				}
			}
		}
		this.face_cache_rpf_tags = rpf_tags;
		this.face_cache_final_apply_rate = final_apply_rate;
		
		Map<Triad, Integer> map_tmp = new HashMap<Triad, Integer>();
		int current_count = 0;
		for (int is = 0; is < face_count; ++is) {
			
			Triad[] t_ = this.getFaceIndices(is);
			for (Triad t1: t_) {
				if (!map_tmp.containsKey(t1))
					map_tmp.put(t1, current_count++);
			}
		}
		
		Map<Integer, Triad> reverse_map_tmp = new HashMap<Integer, Triad>();
		for (Triad t: map_tmp.keySet())
			reverse_map_tmp.put(map_tmp.get(t), t);
		
		Vector default_vec_tmp = new Vector(0, VERTEX_FACE_STRIDE, true);
		FloatBuffer vertex_buffer = BufferUtils.createFloatBuffer(current_count * VERTEX_FACE_STRIDE);
		for (int i = 0; i < current_count; ++i) {
			Vector info = this.concatPTN(reverse_map_tmp.get(i));
			info = (info == null ? default_vec_tmp : info).get(0, VERTEX_FACE_STRIDE);
			info.storeBufferF(vertex_buffer);
		}
		vertex_buffer.flip();
		this.face_cache_triad_mapping_map = map_tmp;
		
		vao_id = this.getVAOId();
		GL30.glBindVertexArray(vao_id);
		
		vbo_fvert_id = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo_fvert_id);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertex_buffer, GL15.GL_STATIC_DRAW);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, VERTEX_FACE_STRIDE * 4, 0);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, VERTEX_FACE_STRIDE * 4, 3 * 4);
        GL11.glNormalPointer(GL11.GL_FLOAT, VERTEX_FACE_STRIDE * 4, 5 * 4);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_PRE, rpf_tags, ptick, 0);
		
		this.vbo_face_index_ids = new HashMap<TagBase, Integer>();
		this.vbo_face_index_counts = new HashMap<TagBase, Integer>();
		
		for (int it = 0; it < rpf_tags.length; ++it) {
			TagBase t = rpf_tags[it];
			if (!t.isActivated()) continue; 
			//boolean succeeded = t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, 0).succeeded;
			//if (!succeeded) continue;
			
			ArrayList<Integer> render_indices = new ArrayList<Integer>();
			
			int index_count = 0;
			for (int is = 0; is < face_count; ++is) {
				if (final_apply_rate[is][it] < 1e-6) continue;
				// TODO Alternation of coords?
				TagBase.ApplyReturn ret = t.applyOn(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, this, is, ptick, true);
				
				Triad[] t_ = this.getFaceIndices(is);
				int a = 2, b = t_.length, c = 1;
				if (reverse_normal) {
					a = t_.length - 1 - 2;
					b = -1;
					c = -1;
				}
				if (reverse_normal) {
					for (int i_ = t_.length - 1 - 2; i_ > -1; --i_) {
						render_indices.add(map_tmp.get(t_[t_.length - 1]));
						render_indices.add(map_tmp.get(t_[i_ + 1]));
						render_indices.add(map_tmp.get(t_[i_]));
					}
				} else {
					for (int i_ = 0 + 2; i_ < t_.length; ++i_) {
						render_indices.add(map_tmp.get(t_[0]));
						render_indices.add(map_tmp.get(t_[i_ - 1]));
						render_indices.add(map_tmp.get(t_[i_]));
					}
				}
				index_count += 3 * 4;
			}
			IntBuffer index_buffer = BufferUtils.createIntBuffer(render_indices.size());
			for (int i: render_indices) index_buffer.put(i);
			index_buffer.flip();
			
			int vboi_id = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi_id);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, index_buffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			this.vbo_face_index_ids.put(t, vboi_id);
			this.vbo_face_index_counts.put(t, index_count);
			
			t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_POST, this, ptick);
		}

		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_POST, rpf_tags, ptick);
		GL30.glBindVertexArray(0);
	}
	public void renderFaceWithBuffer(double ptick, boolean reverse_normal) {
		if (this.vbo_fvert_id <= 0) {
			this.prepareFaceRender(reverse_normal);
		}
		
		GL30.glBindVertexArray(this.getVAOId());
        
		TagBase[] rpf_tags = this.face_cache_rpf_tags;
		for (int it = 0; it < rpf_tags.length; ++it) {
			TagBase t = rpf_tags[it];
			if (!t.isActivated()) continue; 
			int rerender_count = (Integer) (t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, -1).data[0]);
			for (int ir = 0; ir < rerender_count; ++ir) {
				boolean succeeded = t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, ir).succeeded;
				if (!succeeded) continue;

				GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		         
		        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.vbo_face_index_ids.get(t));
		         
		        GL11.glDrawElements(GL11.GL_TRIANGLES, this.vbo_face_index_counts.get(t), GL11.GL_UNSIGNED_INT, 0);
		         
		        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

		        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
				GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
		        
			}
			t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_POST, this, ptick);
		}

		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_POST, rpf_tags, ptick);
		GL30.glBindVertexArray(0);
	}
	
	public void renderFaceNaive(double ptick, boolean reverse_normal) {
		GlHelper renderer = GlHelper.getInstance();
		
		int face_count = this.getFaceCount();
		TagBase[] rpf_tags = this.findTags(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE);
		double[][] final_apply_rate = this.face_cache_final_apply_rate;
		
		if (final_apply_rate == null) {
			final_apply_rate = new double[face_count][rpf_tags.length];
			for (int i = 0; i < face_count; ++i) {
				for (int j = 0; j < rpf_tags.length; ++j) {
					if (!rpf_tags[j].isActivated()) continue;
					TagBase.ApplyReturn ret = rpf_tags[j].applyOn(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, this, i, ptick, false);
					if (ret.succeeded) {
						double rate = (Double) ret.data[0];
						if (rate > (1 - 1e-6)) rate = 1;
						else if (rate < 1e-6) rate = 0;
						final_apply_rate[i][j] = rate;
						for (int k = 0; k < j; ++k) final_apply_rate[i][k] *= (1 - rate);
					}
				}
			}
		}
		
		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_PRE, rpf_tags, ptick, 0);
		
		for (int it = 0; it < rpf_tags.length; ++it) {
			TagBase t = rpf_tags[it];
			if (!t.isActivated()) continue; 
			int rerender_count = (Integer) (t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, -1).data[0]);
			for (int ir = 0; ir < rerender_count; ++ir) {
				boolean succeeded = t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_PRE, this, ptick, ir).succeeded;
				if (!succeeded) continue;
				for (int is = 0; is < face_count; ++is) {
					if (final_apply_rate[is][it] < 1e-6) continue;
					TagBase.ApplyReturn ret = t.applyOn(TagBase.TAG_PHRASE_RENDER_PARTICULAR_FACE, this, is, ptick, true);
					
					Triad[] t_ = this.getFaceIndices(is);
					
					renderer.startDrawingFace();
					if (reverse_normal) {
						for (int i_ = t_.length - 1; i_ > -1; --i_) {
							Vector v1, v2;
							v1 = this.getVertPos(t_[i_].getX());
							v2 = this.getVertUVM(t_[i_].getY());
							renderer.addVertex(v1, v2);
						}
					} else {
						for (Triad t1 : t_) {
							Vector v1, v2;
							v1 = this.getVertPos(t1.getX());
							v2 = this.getVertUVM(t1.getY());
							renderer.addVertex(v1, v2);
						}
					}
					renderer.endDrawing();
				}
			}
			t.applyOn(TagBase.TAG_PHRASE_RENDER_FACES_POST, this, ptick);
		}

		this.applyTagsWithExclusion(TagBase.TAG_PHRASE_RENDER_FACES_POST, rpf_tags, ptick);
	}
	
	public void renderFace(double ptick, boolean reverse_normal) {
		this.renderFaceWithBuffer(ptick, reverse_normal);
	}
	
	/*
	
	@Deprecated
	public void checkAndCompile() {if (!this.isCompiled()) this.compileList(0, false);}
	public void compileList(double ptick, boolean reverse_normal) {
		GlHelper helper = GlHelper.getInstance();
		this.displayList = helper.genDispList();
        helper.startCompileList(this.displayList);
        super.render(ptick, reverse_normal);
        helper.endList();
        this.compiled = true;
	}
	
	public int getDisplayList() {return this.displayList;}
	public int clearDisplayList() {
		int ans = this.displayList;
		this.displayList = -1;
		this.compiled = false;
		return ans;
	}
	*/
	
	@Override
	public void render(double ptick) {this.render(ptick, false);}
	@Override
	public void render(double ptick, boolean reverse_normal) {
		super.render(ptick, reverse_normal);
		if (1 < 2) return;
		/*
		if (!this.isCompiled()) {
			//super.render(ptick, reverse_normal);
			//checkAndCompile();
			this.compileList(ptick, reverse_normal);
		} else {
			GlStateManager.callList(this.getDisplayList());
		}
		*/
	}
	
}

package com.billstark001.riseui.develop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Iterator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.xml.sax.SAXException;

import com.billstark001.riseui.base.NodeBase;
import com.billstark001.riseui.base.shader.MaterialEdgeSimple;
import com.billstark001.riseui.base.shader.MaterialVertSimple;
import com.billstark001.riseui.base.shader.TagApplyMaterialEdge;
import com.billstark001.riseui.base.shader.TagApplyMaterialVert;
import com.billstark001.riseui.base.states.simple3d.State3DIntegrated;
import com.billstark001.riseui.client.GlHelper;
import com.billstark001.riseui.computation.Matrix;
import com.billstark001.riseui.computation.UtilsInteract;
import com.billstark001.riseui.computation.UtilsTex;
import com.billstark001.riseui.computation.Vector;
import com.billstark001.riseui.core.empty.EmptyNode;
import com.billstark001.riseui.core.empty.TagTowardsTarget;
import com.billstark001.riseui.core.polygon.Polygon;
import com.billstark001.riseui.core.polygon.Presets;
import com.billstark001.riseui.io.CharResourceLoader;
import com.billstark001.riseui.io.ColladaFile;
import com.billstark001.riseui.io.IOUtils;
import com.billstark001.riseui.io.MtlFile;
import com.billstark001.riseui.io.ObjFile;
import com.dddviewr.collada.Collada;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderTestObject{
	
	private static long tstart = System.currentTimeMillis();
	private static long tend;
	private static double anim_cycle = 10;
	
	private static CharResourceLoader res = CharResourceLoader.getInstance();
	private static GlHelper render = GlHelper.getInstance();
	private static EntityPlayer player = Minecraft.getMinecraft().player;
	private static World world = Minecraft.getMinecraft().world;
	
	private static ObjFile h_obj, t_obj;
	private static MtlFile h_mtl, t_mtl;
	private static ColladaFile sp_dae;
	public static Polygon horse, table, sphere;
	private static Polygon hgrid;
	private static EmptyNode spider;
	
	private static final ResourceLocation lobj = new ResourceLocation("riseui:models/skh.obj");
	private static final ResourceLocation lmtl = new ResourceLocation("riseui:models/skh.mtl");
	private static final ResourceLocation tobj = new ResourceLocation("riseui:models/table.obj");
	private static final ResourceLocation tmtl = new ResourceLocation("riseui:models/table.mtl");
	private static final ResourceLocation spic = new ResourceLocation("riseui:models/motive_spider.dae");
	
	public static void prepareRender() {
		
		TagTowardsTarget t = new TagTowardsTarget();
		
		render.setDebugState(true);
		
		res.loadRes(lobj);
		res.loadRes(lmtl);
		res.loadRes(tobj);
		res.loadRes(tmtl);
		h_obj = new ObjFile(res.getRes(lobj));
		h_mtl = new MtlFile(res.getRes(lmtl));
		t_obj = new ObjFile(res.getRes(tobj));
		t_mtl = new MtlFile(res.getRes(tmtl));
		h_obj.linkMtlFile(h_mtl);
		t_obj.linkMtlFile(t_mtl);
		
		horse = h_obj.genPoly("skh");
		horse.setRenderState(new State3DIntegrated(null, null, 0.01));
		horse.rasterize();
		horse.setLocalState(new State3DIntegrated(new Vector(0, 0.5, 0)));
		//horse.rasterize();
		//horse.compileList();
		//System.out.println(horse);
		
		table = t_obj.genPoly("table");
		table.setRenderState(new State3DIntegrated(null, null, 0.01));
		table.rasterize();
		table.setLocalState(new State3DIntegrated(new Vector(0, 2.5, 0)));
		//cube.rasterize();
		//table.compileList();
		//System.out.println(cube);
		
		hgrid = h_obj.genPoly("skh");
		hgrid.setRenderState(new State3DIntegrated(null, null, 0.005));
		hgrid.rasterize();
		hgrid.setLocalState(new State3DIntegrated(new Vector(0, 3.5, 0)));
		//cube_.rasterize();
		//hgrid.compileList();
		
		sphere = Presets.getPolygon("sphere_high_lod");
		sphere.setRenderState(new State3DIntegrated(null, null, new Vector(3, 1, 3)));
		//cube.setRot(Quaternion.axisRotate(new Vector(0, 0, 1), Math.PI * 0.25));
		//terrain.rasterize();
		//cube.setPos(new Vector(0, 0.5, 0));
		//terrain.rasterize();
		//sphere.compileList();
		//System.out.println(terrain);
		
		//hgrid.setParent(horse);
		table.setParentRemainGlobalState(sphere);
		sphere.setParentRemainGlobalState(horse);

		horse.dump();
		
		
		// Spider
		Collada stemp = null;
		try {
			stemp = Collada.readFile(IOUtils.getInputStream(spic));
			//stemp = Collada.readFile("C:\\Users\\zhaoj\\Desktop\\gltf_test\\spider.dae");
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		sp_dae = new ColladaFile(stemp);
		sp_dae.parse();
		spider = (EmptyNode) sp_dae.getNodeByName("spider");
		spider.setLocalState(new State3DIntegrated(new Vector(2, 1, 2), null, 0.01));
		spider.setVisEdge(NodeBase.Visibility.TRUE);
		spider.setVisVert(NodeBase.Visibility.TRUE);
		TagApplyMaterialEdge tme = new TagApplyMaterialEdge(new MaterialEdgeSimple(UtilsTex.color(0.8, 0, 1, 0.3), 1));
		TagApplyMaterialVert tmv = new TagApplyMaterialVert(new MaterialVertSimple(UtilsTex.color(1, 0, 0.8, 0.6), 2));
		NodeBase sppppp = spider;
		for (Iterator<NodeBase> it = spider.getTreeIteratorBreadthFirst(); it.hasNext();) {
			NodeBase nb = it.next();
			nb.addTag(tme);
			nb.addTag(tmv);
		}
		
	}
	
	public static Matrix getGlMatrix(int mat) {
		FloatBuffer mdata = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(mat, mdata);
		Matrix4f m = new Matrix4f();
		m.load(mdata);
		return UtilsInteract.transMat(m);
	}
	
	public static void doRender(double delta) {
		
		tend = System.currentTimeMillis();
		
		//GlStateManager.pushMatrix();
		
		//tend = System.currentTimeMillis() - tstart;
		//cube.setRot(Quaternion.axisRotate(new Vector(0, 0, 1), Math.PI * 0.00025 * tend));
		//render.renderGrid(cube_);
		//render.disableGridState();
		//render.renderObject(horse);
		
		//render.renderMesh(horse);
		//render.renderMesh(table);
		//render.renderMesh(sphere);
		
		//GL11.glTranslated(0, 5, 0);
		//render.renderGrid(cube_);
		render.setDebugState(true);
		double cur_time = ((tend - tstart) / 1000.) % anim_cycle;
		//System.out.println(cur_time);
		
		horse.setLocalState(new State3DIntegrated(new Vector(0, cur_time, 0)));
		render.renderObject(horse, delta);
		//System.out.println(horse.getUVMap(0));

		spider.setChildrenFrameTime(cur_time);
		NodeBase spider_body = spider.getChild(0).getChild(0);
		render.renderObject(spider, delta);
		/*
		if (DevelopProxy.mark1 == 1)
			
		else if (DevelopProxy.mark1 == 2) 
			render.renderCompiled(cube);
		else
			render.renderMesh(cube);
			//render.renderCompiled(horse);
			//render.renderCompiled(cube);
		*/
		
		//GlStateManager.popMatrix();
		
	}	
	
		/*
		//Something done first
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(1.5F);
        
        
        
        //Draw Opaque Elements
        //if(ClientProxy.renderGrid) drawTileOverlay(CurrentBlockPos, RenderPos, 1.004, 10, DEFAULT_COLOR, 0.6);
        //p.setPos(new Vector(2, 0, 0));
        //p.calcRender();
		//ve.renderPolygon(p, DEFAULT_COLOR, (int) (255 * 0.6));
		//ve.renderPolygon(p_, DEFAULT_COLOR, (int) (255 * 0.6));
        
        //Something done at last
        GL11.glLineWidth(2F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		
		//Something done first
		GL11.glPushMatrix();
		GL11.glTranslated(-x+0.5, -y+0.5, -z+0.5);
		
		
		
		//Something done at last
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(1.5F);
        GL11.glTranslated(-x+0.5, -y+0.5, -z+0.5);
        GL11.glClear(GL11.GL_ACCUM);
        
        //Draw X-Ray Elements
        //drawTest((double)tend / 2000);
        
        //if(ClientProxy.renderOre) drawOre(CurrentBlockPos, RenderPos, LookPos, 20, 0.6, 1);
        //if(ClientProxy.renderMob) drawMob(RenderPos, 40, 0xFFFF80, 0xFF5050, 0.8, delta);
        
        //Something done at last
        GL11.glLineWidth(2F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		*/

	
	/*
	private static void drawTest() {
		me.assignTex(b1, m1);
		for (int i = 0; i < t1.getFaceCount(); ++i) {
			me.renderSurface(t1.getFaceVertex(i), t1.getFaceUV(i));
		}
		me.assignTex(b2, m2);
		for (int i = 0; i < t2.getFaceCount(); ++i) {
			me.renderSurface(t2.getFaceVertex(i), t2.getFaceUV(i));
		}
		me.assignTex(b3, m3);
		for (int i = 0; i < t3.getFaceCount(); ++i) {
			me.renderSurface(t3.getFaceVertex(i), t3.getFaceUV(i));
		}
		me.assignTex(b4, m4);
		for (int i = 0; i < t4.getFaceCount(); ++i) {
			me.renderSurface(t4.getFaceVertex(i), t4.getFaceUV(i));
		}
		
	}
	*/
	/*
	private static Block getBlock(int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		return Block.getBlockById(Block.getStateId(world.getBlockState(pos)));
	}
	private static Block getBlock(BlockPos pos) {
		return Block.getBlockById(Block.getStateId(world.getBlockState(pos)));
	}
	
	private static boolean necessaryToDraw(BlockPos pos) {
		int tx = pos.getX();
		int ty = pos.getY();
		int tz = pos.getZ();
		
    	boolean bi = getBlock(tx,	 ty,	 tz		).getMaterial(world.getBlockState(pos)).isSolid();
    	bi = bi && (
    					 
    			//!getBlock(tx-1,	 ty-1,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx-1,	 ty-1,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			//!getBlock(tx-1,	 ty-1,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty-1,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty-1,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty-1,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			//!getBlock(tx+1,	 ty-1,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx+1,	 ty-1,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			//!getBlock(tx+1,	 ty-1,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			
    			!getBlock(tx-1,	 ty,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx-1,	 ty,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx-1,	 ty,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			
    			!getBlock(tx,	 ty,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx+1,	 ty,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx+1,	 ty,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx+1,	 ty,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			
    			//!getBlock(tx-1,	 ty+1,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx-1,	 ty+1,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			//!getBlock(tx-1,	 ty+1,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty+1,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty+1,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx,	 ty+1,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			//!getBlock(tx+1,	 ty+1,	 tz-1	).getMaterial(world.getBlockState(pos)).isSolid()||
    			!getBlock(tx+1,	 ty+1,	 tz		).getMaterial(world.getBlockState(pos)).isSolid()//||
    			//!getBlock(tx+1,	 ty+1,	 tz+1	).getMaterial(world.getBlockState(pos)).isSolid()
    			
    			);
		
		return bi;
	}
	
	private static boolean tooDark(BlockPos pos, int threshold) {
		if(getBlock(pos).getMaterial(world.getBlockState(pos)) != Material.AIR) return false;
		return world.getLight(pos) < threshold ? true : false;
	}
	
	private static void drawTileOverlay(Vec3d CurrentBlockPos, Vec3d RenderPos, double scale, double range, int color, double opacity) {
        //int cubec = 0;
		for(double i=-range-1;i<range;i+=1)
        for(double j=-range-1;j<range;j+=1)
        for(double k=-range-1;k<range;k+=1) {
        	
        	Vec3d cur = new Vec3d(i, j, k);
        	double dis = (range - cur.add(CurrentBlockPos).distanceTo(RenderPos)) / range;
        	if(dis < 0) dis = 0;
        	dis *= dis;
        	
        	if(necessaryToDraw(player.getPosition().add(new Vec3i(i, j, k)))){
        		ve.renderCube(CurrentBlockPos.add(cur), new Vec3d(scale, scale, scale), new Vec3d(1, 0, 0), 0, color, (int)(dis * 255 * opacity));
        		//cubec++;
        	}
        	
        	//if(tooDark(player.getPosition().add(new Vec3i(i, j, k)), 8))
        		//ve.renderCube(CurrentBlockPos.add(cur), new Vec3d(0.1, 0.1, 0.1), new Vec3d(1, 0, 0), 0, 0xFF0000, (int)(dis * 255 * opacity));
        }
        //System.out.println(cubec);
	}
	
	private static int getOreColor(Block block) {
		Map<Block, Integer> OreMap= new HashMap<Block, Integer>();
		//Ores
		OreMap.put(Blocks.EMERALD_ORE, 	0x00FF00);
		OreMap.put(Blocks.COAL_ORE, 	0x808080);
		OreMap.put(Blocks.DIAMOND_ORE, 	0x00FFFF);
		OreMap.put(Blocks.LAPIS_ORE, 	0x0000FF);
		OreMap.put(Blocks.QUARTZ_ORE, 	0xFFFFFF);
		OreMap.put(Blocks.IRON_ORE, 	0xFFCCCC);
		OreMap.put(Blocks.GOLD_ORE, 	0xFFFF00);
		OreMap.put(Blocks.REDSTONE_ORE,	0xFF0000);
		OreMap.put(Blocks.MOB_SPAWNER,	0xFF00FF);

		return OreMap.getOrDefault(block, 0x1000000);
	}

	private static void drawOre(Vec3d CurrentBlockPos, Vec3d RenderPos, Vec3d LookPos, double range, double cubesize, double opacity) {
        for(double i=-range-1;i<range;i+=1)
        for(double j=-range-1;j<range;j+=1)
        for(double k=-range-1;k<range;k+=1) {
        	
        	Vec3d cur = new Vec3d(i, j, k);
        	double dis = (range - cur.add(CurrentBlockPos).distanceTo(RenderPos)) / range;
        	if(dis < 0) dis = 0;
        	dis *= dis;
        	
        	int color = getOreColor(getBlock(player.getPosition().add(Utils.transDI(cur))));
        	if(color < 0x1000000)
        		ve.renderCube(CurrentBlockPos.add(cur), new Vec3d(cubesize, cubesize, cubesize), new Vec3d(1, 0, 0), 0, color, (int)(dis * 255 * opacity));
        }
	}
	
	private static void drawMob(Vec3d CurrentPos, double range, int bbox_color, int health_color, double opacity, double delta) {
		List list = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(player.posX-range, player.posY-range, player.posZ-range, player.posX+range, player.posY+range, player.posZ+range));
        for(Iterator iterator = list.iterator();iterator.hasNext();)
        {
            EntityLiving entity = (EntityLiving)iterator.next();
            if(entity.equals(player))continue;
            
            Vec3d pos = entity.getPositionVector().subtract(0.5, 0.5, 0.5);
            Vec3d ltpos = new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
            //pos = ltpos.add(e.numMult(pos.subtract(ltpos), delta));
            Vec3d looking = entity.getLookVec();
            
        	double dis = (range - pos.distanceTo(CurrentPos)) / range;
        	if(dis < 0) dis = 0;
        	dis *= dis;
        	int alpha = (int)(dis * 255 * opacity);
        	
        	ve.renderCircle(pos, entity.width/1.5, new Vec3d(0, 1, 0), 72, bbox_color, alpha);
        	ve.renderCircle(pos.addVector(0, entity.height*0.1, 0), entity.width/1.5, new Vec3d(0, 1, 0), 72, bbox_color, alpha);
        	ve.renderCircle(pos.addVector(0, entity.height, 0), entity.width/1.5, new Vec3d(0, 1, 0), 72, bbox_color, alpha);

        	ve.renderCircle(pos.addVector(0, entity.getEyeHeight(), 0), entity.width*(entity.getHealth()/entity.getMaxHealth())/1.5, new Vec3d(0, 1, 0), 72, health_color, alpha);
        	ve.renderLine(pos.addVector(0, entity.getEyeHeight(), 0), pos.addVector(0, entity.getEyeHeight(), 0).add(looking), health_color, alpha, true);
        	ve.renderLine(pos.addVector(0, entity.getEyeHeight(), 0), pos.addVector(0, entity.getEyeHeight()+(entity.height-entity.getEyeHeight())*(entity.getHealth()/entity.getMaxHealth()), 0), health_color, alpha, true);
        	ve.renderLine(pos.addVector(0, entity.getEyeHeight(), 0), pos.addVector(0, entity.getEyeHeight()-(entity.getEyeHeight())*(entity.getHealth()/entity.getMaxHealth()), 0), health_color, alpha, true);
        }
	}
	*/
}

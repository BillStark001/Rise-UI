package com.billstark001.riseui.develop;

import com.billstark001.riseui.client.GlRenderHelper;
import com.billstark001.riseui.math.Vector;
import com.billstark001.riseui.objects.PolygonMesh;
import com.billstark001.riseui.objects.Presets;
import com.billstark001.riseui.resources.MtlFile;
import com.billstark001.riseui.resources.ObjFile;
import com.billstark001.riseui.resources.ResourceLoader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderTestObject{
	
	private static long tstart = System.currentTimeMillis();
	private static long tend;
	private static final int DEFAULT_COLOR = 0x00FFFF;
	
	private static ResourceLoader res = ResourceLoader.getInstance();
	private static GlRenderHelper render = GlRenderHelper.getInstance();
	private static EntityPlayer player = Minecraft.getMinecraft().player;
	private static World world = Minecraft.getMinecraft().world;
	
	private static ObjFile t_obj;
	private static MtlFile t_mtl;
	private static PolygonMesh horse, cube, terrain;
	
	private static final ResourceLocation lobj = new ResourceLocation("riseui:models/skh.obj");
	private static final ResourceLocation lmtl = new ResourceLocation("riseui:models/skh.mtl");
	
	public static void prepareRender() {
		res.loadRes(lobj);
		res.loadRes(lmtl);
		if (t_obj == null) t_obj = new ObjFile(res.getRes(lobj));
		if (t_mtl == null) t_mtl = new MtlFile(res.getRes(lmtl));
		
		render.assignMtlFile(t_mtl);
		horse = t_obj.genMesh("skh");
		horse.setScale(0.01);
		horse.setPos(new Vector(0, 0.5, 0));
		horse.rasterize();
		horse.compileList();
		
		render.assignMtlFile(null);
		cube = Presets.getMesh("cube");
		cube.setScale(0.5);
		cube.setPos(new Vector(0, 2.5, 0));
		cube.rasterize();
		cube.compileList();
		
		terrain = Presets.getMesh("terrain_high_lod");
		terrain.setScale(new Vector(5, 1, 5));
		terrain.setPos(new Vector(0, 0.5, 0));
		terrain.rasterize();
		terrain.compileList();
		
		cube.setParent(horse);
		horse.setParent(terrain);
		
	}
	
	public static void doRender(double delta) {
		
		GlStateManager.pushMatrix();
		
		tend = System.currentTimeMillis() - tstart;
		
		render.renderObject(terrain);
		
		GlStateManager.popMatrix();
		
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

	}
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

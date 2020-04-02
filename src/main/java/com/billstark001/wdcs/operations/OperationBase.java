package com.billstark001.wdcs.operations;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class OperationBase implements IOperation{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "base";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "operations.base.desc";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "operations.base.usage";
	}

	public void speakToPlayer(EntityPlayer player, String words) {
		player.sendMessage(new TextComponentString("[WDCS | OP | " + getName().toUpperCase() + "] " + words));
	}

	@Override
	public boolean execute(MinecraftServer server, EntityPlayer player, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String checkArgsValidity(String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

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

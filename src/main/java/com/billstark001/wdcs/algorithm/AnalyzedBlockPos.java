package com.billstark001.wdcs.algorithm;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;

import com.billstark001.riseui.computation.UtilsInteract;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class AnalyzedBlockPos
{
    public int x;
    public int y;
    public int z;
    
    private final Minecraft mc = Minecraft.getMinecraft();

    public AnalyzedBlockPos() {}

    public AnalyzedBlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public AnalyzedBlockPos(AnalyzedBlockPos o) {this(o.x, o.y, o.z);}
    public AnalyzedBlockPos(AnalyzedBlockPos o, int dx, int dy, int dz) {this(o.x + dx, o.y + dy, o.z + dz);}

    /**
     * Gets the ID of a block relative to this block.
     * @param dx x location relative to this block
     * @param dy y location relative to this block
     * @param dz z location relative to this block
     * @return
     */
    public Block getBlock(int dx, int dy, int dz)
    {
    	return UtilsInteract.getBlock(x + dx, y + dy, z + dz);
    }

    /**
     * Checks if mobs can spawn ON the block at a location.
     * @param dx x location relative to this block
     * @param dy y location relative to this block
     * @param dz z location relative to this block
     * @return true if mobs can spawn ON this block
     */
    public boolean CanMobsSpawnOnBlock(int dx, int dy, int dz)
    {
        Block block = getBlock(dx, dy, dz);

        if (block == null
        	|| block == Blocks.AIR
        	|| block instanceof BlockBarrier
        	|| block instanceof BlockSlime)
        {
            return false;
        }
        
        if (block.isOpaqueCube(null)
        	//|| mc.theWorld.doesBlockHaveSolidTopSurface(mc.theWorld, new BlockPos(x + dx, y + dy, z + dz))
        	|| block instanceof BlockFarmland)	//the one exception to the isOpaqueCube and doesBlockHaveSolidTopSurface rules
        {
            return true;
        }
        
        return false;
    }

    /**
     * Checks if mobs can spawn IN the block at a location.
     * @param dx x location relative to this block
     * @param dy y location relative to this block
     * @param dz z location relative to this block
     * @return true if mobs can spawn ON this block
     */
    public boolean CanMobsSpawnInBlock(int dx, int dy, int dz)
    {
        Block block = getBlock(dx, dy, dz);

        if (block == null)	//air block
        {
            return true;
        }

        if (block.isOpaqueCube(null))	//majority of blocks: dirt, stone, etc.
        {
            return false;
        }
        
        //list of transparent blocks mobs can NOT spawn inside of.
        //for example, they cannot spawn inside of leaves even though they are transparent.
        //  (I wonder if the list shorter for blocks that mobs CAN spawn in?
        //   lever, button, redstone  torches, reeds, rail, plants, crops, etc.)
        return !(block instanceof BlockAnvil
                || block instanceof BlockBarrier
                || block instanceof BlockBed
                || block instanceof BlockCactus
                || block instanceof BlockCake
                || block instanceof BlockChest
                || block instanceof BlockFarmland
                || block instanceof BlockFence
                || block instanceof BlockFenceGate
				|| block instanceof BlockGlass
                || block instanceof BlockHopper
                || block instanceof BlockIce
                || block instanceof BlockLeaves
                || block instanceof BlockLiquid
                || block instanceof BlockPane
                || block instanceof BlockPistonBase
                || block instanceof BlockPistonExtension
                || block instanceof BlockPistonMoving
                || block instanceof BlockSlab
                || block instanceof BlockSlime
                || (block instanceof BlockSnow && block.getMetaFromState(UtilsInteract.getBlockState(x+dx, y+dy, z+dz)) > 0)	//has 1 out of 8 snow layers
                || block instanceof BlockStainedGlass
                || block instanceof BlockStairs
                || block instanceof BlockWall
                || block instanceof BlockWeb);
    }
    
    /**
     * Checks if a block is an opqaue cube.
     * @param dx x location relative to this block
     * @param dy y location relative to this block
     * @param dz z location relative to this block
     * @return true if the block is opaque (like dirt, stone, etc.)
     */
    public boolean IsOpaqueBlock(int dx, int dy, int dz)
    {
        Block block = getBlock(dx, dy, dz);
        
        if (block == null)	//air block
        {
            return false;
        }

        return block.isOpaqueCube(null);
    }

    /**
     * Checks if a block is air.
     * @param dx x location relative to this block
     * @param dy y location relative to this block
     * @param dz z location relative to this block
     * @return true if the block is opaque (like dirt, stone, etc.)
     */
    public boolean IsAirBlock(int dx, int dy, int dz)
    {
        Block block = getBlock(dx, dy, dz);
        
        if (block == Blocks.AIR)
        {
            return true;
        }

        return false;
    }

    /**
     * Gets the light level of the spot above this block. Does not take into account sunlight.
     * @return 0-15
     */
    public int GetLightLevelWithoutSky()
    {
        return mc.world.getLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y + 1, z));
    }

    /**
     * Gets the light level of the spot above this block. Take into account sunlight.
     * @return 0-15
     */
    public int GetLightLevelWithSky()
    {
        return mc.world.getLightFor(EnumSkyBlock.SKY, new BlockPos(x, y + 1, z));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        AnalyzedBlockPos that = (AnalyzedBlockPos) o;

        if (x != that.x)
        {
            return false;
        }

        if (y != that.y)
        {
            return false;
        }

        if (z != that.z)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = (x ^ (x >>> 16));
        result = 31 * result + (y ^ (y >>> 16));
        result = 31 * result + (z ^ (z >>> 16));
        return result;
    }
}

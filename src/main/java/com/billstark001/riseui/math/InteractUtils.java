package com.billstark001.riseui.math;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public final class InteractUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	//Linear Algebra Related
	public static final Vec3d[] transMat(Matrix M) {
		int x = (Integer) M.getSize().getX();
		Vec3d[] temp = new Vec3d[x];
		for(int i = 0; i < x; ++i) {
			Vector v = M.getLine(i);
			temp[i] = new Vec3d(v.get(0), v.get(1), v.get(2));
		}
		return temp;
	}
	
	public static final Vector transVec(Vec3d v) {return new Vector(v.x, v.y, v.z);}
	public static final Vec3d transVec(Vector v) {return new Vec3d(v.get(0), v.get(1), v.get(2));}
	
	public static final Vec3i transDI(Vec3d v) {return new Vec3i(v.x, v.y, v.z);}
	public static final Vec3d transDI(Vec3i v) {return new Vec3d(v.getX(), v.getY(), v.getZ());}
	
	public static org.lwjgl.util.vector.Quaternion transQuat(Quaternion q) {
		float x, y, z, w;
		w = q.getF(0);
		x = q.getF(1); y = q.getF(2); z = q.getF(3);
		return new org.lwjgl.util.vector.Quaternion(x, y, z, w);
    }
	
	public static final Vector getAirOrderAngle(Vector v, double roll) {return Utils.getAirOrderAngle(v, roll);}
	public static final Matrix getRotateAffine(Vector v, double roll) {return Utils.getRotateAffine(v, roll);}
	
	@Deprecated
	public static Vec3d numMult(Vec3d v, double n) {
		return new Vec3d(v.x * n, v.y * n, v.z * n);
	}
	
	@Deprecated
	public static Vec3d getPlaneVec(Vec3d v, double length) {
		if(length <= 0) length = v.lengthVector();
		if(v.x <= 1e-12 && v.z <= 1e-12) return new Vec3d(length, 0, 0);
		Vec3d ans = new Vec3d(-v.y/v.x, 1, 0);
		return numMult(ans, length / ans.lengthVector());
	}
	
	@Deprecated
	public static Vec3d rotateByAxis(Vec3d v, Vec3d axis, double angle) {
		Vec3d a = numMult(v, MathHelper.cos((float) angle));
		Vec3d b = numMult(axis.crossProduct(v), MathHelper.sin((float) angle));
		return numMult(a.add(b), v.lengthVector() / a.add(b).lengthVector());
	}
	
	//Blocks Related
    public static final BlockPos getMouseOveredBlockPos() {return mc.objectMouseOver.getBlockPos();}
    public static final Block getMouseOveredBlock() {return getBlock(getMouseOveredBlockPos());}

    public static final Block getBlock(int x, int y, int z) {return getBlock(new BlockPos(x, y, z));}
    public static final Block getBlock(BlockPos pos) {
    	IBlockState blockState = getBlockState(pos);
    	if(blockState == null) return null;
    	else return blockState.getBlock();
    }
    
    public static final IBlockState getBlockState(int x, int y, int z) {return getBlockState(new BlockPos(x, y, z));}
    public static final IBlockState getBlockState(BlockPos pos) {
    	if(mc.world != null) return mc.world.getBlockState(pos);
    	else return null;
    }
    
	
}

package com.billstark001.wdcs.algorithm;

import net.minecraft.util.math.Vec3i;

public class Storage3<T> {
	
	private Vec3i dim;
	private T [][][] elements;
	
	public Storage3(int range) {
		dim = new Vec3i(range, range, range);
		elements = (T[][][]) new Object[range][range][range];
	}
	
	public Storage3(int x, int y, int z) {
		dim = new Vec3i(x, y, z);
		elements = (T[][][]) new Object[x][y][z];
	}
	
	public Storage3(Storage3<T> tensor) {
		dim = tensor.getDimension();
		elements = (T[][][]) new Object[dim.getX()][dim.getY()][dim.getZ()];
		for(int i = 0; i < dim.getX(); ++i)
		for(int j = 0; j < dim.getX(); ++j)
		for(int k = 0; k < dim.getX(); ++k) 
			elements[i][j][k] = tensor.getElementByPos(i, j, k);
	}

	public Vec3i getDimension() {
		return dim;
	}
	
	public void changeElementByPos(Vec3i pos, T value) {
		elements[pos.getX()][pos.getY()][pos.getZ()] = value;
	}
	
	public void changeElementByPos(int i, int j, int k, T value) {
		elements[i][j][k] = value;
	}
	
	public T getElementByPos(int i, int j, int k) {
		return elements[i][j][k];
	}
	
	public T getElementByPos(Vec3i pos) {
		return elements[pos.getX()][pos.getY()][pos.getZ()];
	}
}

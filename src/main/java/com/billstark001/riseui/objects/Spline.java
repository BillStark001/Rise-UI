/**
 * 
 */
package com.billstark001.riseui.objects;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

/**
 * @author Zhao
 *
 */
public class Spline extends BaseObject implements IGridable, IRenderable, ICompilable {

	/**
	 * @param pos
	 * @param rot
	 * @param scale
	 */
	public Spline(Vector pos, Quaternion rot, Vector scale) {
		super(pos, rot, scale);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pos
	 * @param rot
	 * @param scale
	 */
	public Spline(Vector pos, Quaternion rot, double scale) {
		super(pos, rot, scale);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pos
	 * @param rot
	 * @param scale
	 */
	public Spline(Vector pos, Vector rot, Vector scale) {
		super(pos, rot, scale);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pos
	 * @param rot
	 */
	public Spline(Vector pos, Quaternion rot) {
		super(pos, rot);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param pos
	 */
	public Spline(Vector pos) {
		super(pos);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public Spline() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.ICompilable#compileList()
	 */
	@Override
	public void compileList() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.ICompilable#getDisplayList()
	 */
	@Override
	public int getDisplayList() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.ICompilable#isCompiled()
	 */
	@Override
	public boolean isCompiled() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.ICompilable#markRecompile()
	 */
	@Override
	public void markRecompile() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.IRenderable#render()
	 */
	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.IGridable#getSegmentCount()
	 */
	@Override
	public int getSegmentCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.IGridable#getSegmentLooped(int)
	 */
	@Override
	public boolean getSegmentLooped(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.IGridable#getSwitchStripLoop(int)
	 */
	@Override
	public boolean getSwitchStripLoop(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.billstark001.riseui.objects.IGridable#getSegmentByIndex(int)
	 */
	@Override
	public Matrix getSegmentByIndex(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}

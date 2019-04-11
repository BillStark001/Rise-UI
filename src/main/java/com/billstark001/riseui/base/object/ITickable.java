/**
 * 
 */
package com.billstark001.riseui.base.object;

/**
 * @author Zhao
 *
 */
public interface ITickable {
	
	public void callOnTick();
	public void recallLastTick();
	public void markLastTickInfo();
	
}

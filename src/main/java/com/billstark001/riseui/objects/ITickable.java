/**
 * 
 */
package com.billstark001.riseui.objects;

/**
 * @author Zhao
 *
 */
public interface ITickable {
	
	public void callOnTick();
	public void recallLastTick();
	public void markLastTickInfo();
	
}

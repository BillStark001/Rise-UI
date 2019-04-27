package com.billstark001.riseui.core.empty;

import com.billstark001.riseui.base.BaseNode;
import com.billstark001.riseui.math.Quaternion;
import com.billstark001.riseui.math.Vector;

public class EmptyNode extends BaseNode {

	public EmptyNode(Vector pos, Quaternion rot, Vector scale, String name) {super(pos, rot, scale, name);}
	public EmptyNode(Vector pos, Quaternion rot, Vector scale) {super(pos, rot, scale);}
	public EmptyNode(Vector pos, Quaternion rot, double scale) {super(pos, rot, scale);}
	public EmptyNode(Vector pos, Vector rot, Vector scale) {super(pos, rot, scale);}
	public EmptyNode(Vector pos, Quaternion rot) {super(pos, rot);}
	public EmptyNode(Vector pos) {super(pos);}
	public EmptyNode() {}
	public EmptyNode(Vector pos, Quaternion rot, double scale, String name) {super(pos, rot, scale, name);}
	public EmptyNode(Vector pos, Vector rot, Vector scale, String name) {super(pos, rot, scale, name);}
	public EmptyNode(Vector pos, Quaternion rot, String name) {super(pos, rot, name);}
	public EmptyNode(Vector pos, String name) {super(pos, name);}
	public EmptyNode(String name) {super(name);}
	
	@Override
	public void onRender(double ptick) {
		// TODO 自动生成的方法存根

	}

}

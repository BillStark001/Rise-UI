package test;

import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.base.ObjectStatedBase;
import com.billstark001.riseui.base.states.BaseState;
import com.billstark001.riseui.base.states.BaseState.DataType;

public class Yasi extends ObjectStatedBase {
	
	@Override
	protected BaseState.DataType[] getNewStateTypes() {
		BaseState.DataType[] new_types = {DataType.FLOAT, DataType.FLOAT};
		return new_types;
	}
	
	public Yasi() {
		// TODO 自动生成的构造函数存根
	}

	public Yasi(String name) {
		super(name);
		// TODO 自动生成的构造函数存根
	}

	public Yasi(Layer layer) {
		super(layer);
		// TODO 自动生成的构造函数存根
	}

	public Yasi(String name, Layer layer) {
		super(name, layer);
		// TODO 自动生成的构造函数存根
	}

}

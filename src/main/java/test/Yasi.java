package test;

import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.base.ObjectStatedBase;

public class Yasi extends ObjectStatedBase {
	
	@Override
	protected Class[] getNewStateTypes() {
		Class[] new_types = {Double.class, Double.class};
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

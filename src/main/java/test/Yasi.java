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
		// TODO �Զ����ɵĹ��캯�����
	}

	public Yasi(String name) {
		super(name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public Yasi(Layer layer) {
		super(layer);
		// TODO �Զ����ɵĹ��캯�����
	}

	public Yasi(String name, Layer layer) {
		super(name, layer);
		// TODO �Զ����ɵĹ��캯�����
	}

}

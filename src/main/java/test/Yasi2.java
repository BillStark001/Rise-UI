package test;

import com.billstark001.riseui.base.Layer;
import com.billstark001.riseui.base.ObjectStatedBase;
import com.billstark001.riseui.base.states.BaseState;
import com.billstark001.riseui.base.states.BaseState.DataType;

public class Yasi2 extends Yasi {

	@Override
	protected BaseState.DataType[] getNewStateTypes() {
		BaseState.DataType[] new_types = {DataType.FLOAT, DataType.FLOAT};
		return new_types;
	}
	
	public Yasi2() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public Yasi2(String name) {
		super(name);
		// TODO �Զ����ɵĹ��캯�����
	}

	public Yasi2(Layer layer) {
		super(layer);
		// TODO �Զ����ɵĹ��캯�����
	}

	public Yasi2(String name, Layer layer) {
		super(name, layer);
		// TODO �Զ����ɵĹ��캯�����
	}

}

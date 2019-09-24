package com.billstark001.riseui.base.state;

import java.util.ArrayList;

import com.billstark001.riseui.math.Matrix;
import com.billstark001.riseui.math.ShapeMismatchException;

public class ComplexState extends State4 {

	private static final Matrix CALC_BASE = Matrix.I4;
	private ArrayList<SimpleState> state_list;
	
	public ComplexState(SimpleState...states) {
		state_list = new ArrayList<SimpleState>();
		for (SimpleState s: states)
			state_list.add(s);
	}
	
	public ComplexState(ComplexState state) {
		for (SimpleState s: state.state_list)
			state_list.add(s);
	}
	
	public ComplexState() {state_list = new ArrayList<SimpleState>();}
	
	public void pushState(SimpleState state) {
		state_list.add(state);
	}
	
	public SimpleState popState() {
		if (state_list.size() == 0) return new SimpleState();
		return state_list.remove(state_list.size() - 1);
	}
	
	public int getStateCount() {return state_list.size();}
	
	public Matrix getState() {
		Matrix ans = CALC_BASE;
		for (SimpleState s: state_list) {
			if (s == null) s = SimpleState.STATE_STANDARD;
			ans = State4.stateCompose(ans, s.getState());
		}
		return ans;
	}
	
	public SimpleState toSimpleState() {
		return new SimpleState(getState());
	}

}

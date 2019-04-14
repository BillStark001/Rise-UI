package com.dddviewr.collada.content.visualscene;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class InstanceMaterial extends Base {
	protected String symbol;

	public InstanceMaterial(String symbol, String target) {
		super(target);
		this.symbol = symbol;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getTarget() {
		return this.getId();
	}

	public String toString() {
		return "InstanceMaterial (symbol: " + this.symbol
				+ ", target: " + this.getId() + ")";
	}
}
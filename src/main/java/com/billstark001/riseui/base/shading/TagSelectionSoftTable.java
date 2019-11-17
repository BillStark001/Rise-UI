package com.billstark001.riseui.base.shading;

import java.util.Arrays;

public class TagSelectionSoftTable extends TagSelectionBase {

	private double[] table = {};
	public void setTable(double[] table) {this.table = Arrays.copyOf(table, table.length);}
	public void clearTable() {this.table = new double[0];}
	
	public TagSelectionSoftTable(int hierarchy, boolean activated, Type type, double[] table) {
		super(hierarchy, activated, type);
		this.setTable(table);
	}

	public TagSelectionSoftTable(int hierarchy, boolean activated) {super(hierarchy, activated);}
	public TagSelectionSoftTable(boolean activated) {super(activated);}
	public TagSelectionSoftTable(int hierarchy) {super(hierarchy);}
	public TagSelectionSoftTable(int hierarchy, Type type) {super(hierarchy, type);}
	public TagSelectionSoftTable(boolean activated, Type type) {super(activated, type);}
	public TagSelectionSoftTable(Type type) {super(type);}
	public TagSelectionSoftTable() {}
	
	public TagSelectionSoftTable(Type type, double[] table) {this(0, true, type, table);}
	public TagSelectionSoftTable(double[] table) {this(0, true, Type.FACE, table);}

	public TagSelectionSoftTable(TagSelectionBase t) {
		super(t);
		this.setTable(table);
	}

	@Override
	public double getContainRate(int index) {
		if (index > 0 && index < table.length) return table[index];
		else return 0;
	}

}

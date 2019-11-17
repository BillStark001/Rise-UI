package com.billstark001.riseui.base.shading;

import java.util.Arrays;

public class TagSelectionHardTable extends TagSelectionBase {

	private boolean[] table = {};
	public void setTable(boolean[] table) {this.table = Arrays.copyOf(table, table.length);}
	public void clearTable() {this.table = new boolean[0];}
	
	public TagSelectionHardTable(int hierarchy, boolean activated, Type type, boolean[] table) {
		super(hierarchy, activated, type);
		this.setTable(table);
	}

	public TagSelectionHardTable(int hierarchy, boolean activated) {super(hierarchy, activated);}
	public TagSelectionHardTable(boolean activated) {super(activated);}
	public TagSelectionHardTable(int hierarchy) {super(hierarchy);}
	public TagSelectionHardTable(int hierarchy, Type type) {super(hierarchy, type);}
	public TagSelectionHardTable(boolean activated, Type type) {super(activated, type);}
	public TagSelectionHardTable(Type type) {super(type);}
	public TagSelectionHardTable() {}
	
	public TagSelectionHardTable(Type type, boolean[] table) {this(0, true, type, table);}
	public TagSelectionHardTable(boolean[] table) {this(0, true, Type.FACE, table);}

	public TagSelectionHardTable(TagSelectionBase t) {
		super(t);
		this.setTable(table);
	}

	@Override
	public double getContainRate(int index) {
		if (index >= 0 && index < table.length) return table[index] ? 1 : 0;
		else return 0;
	}
	
	@Override
	public boolean contains(int index) {
		if (index >= 0 && index < table.length) return table[index];
		else return false;
	}

}

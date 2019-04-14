package com.billstark001.riseui.io;

import com.dddviewr.collada.Collada;

public class ColladaFile {
	
	private final Collada file;
	
	public ColladaFile(Collada file) {
		this.file = file;
	}
	
	public int getSceneIndex() {
		return file.getScene().getInstanceVisualScene().getUrl();
	}

}

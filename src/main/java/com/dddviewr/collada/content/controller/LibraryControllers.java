package com.dddviewr.collada.content.controller;

import java.util.ArrayList;
import java.util.List;

import com.dddviewr.collada.format.LibraryBase;

public class LibraryControllers extends LibraryBase<Controller> {

	public List<Skin> findSkins(int source) {
		List<Skin> result = new ArrayList<Skin>();
		for (Controller c : this.getElements()) {
			if (c.getSkin() == null)
				continue;
			int skinSrc = c.getSkin().getSource();
			if (skinSrc == 0)
				continue;
			if (!(skinSrc == source))
				continue;
			result.add(c.getSkin());
		}
		return result;
	}
}
package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.lights.LibraryLights;

public class library_lights extends State {
	protected LibraryLights library = new LibraryLights();

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		Collada collada = ((COLLADA) getParent()).getCollada();
		collada.setLibraryLights(this.library);
	}

	public LibraryLights getLibrary() {
		return this.library;
	}
}
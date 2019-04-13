package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.images.Image;
import com.dddviewr.collada.content.images.LibraryImages;

public class image extends State {
	protected Image theImage;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);

		this.theImage = new Image(attrs.getValue("id"), attrs.getValue("name"));
		LibraryImages lib = ((library_images) getParent()).getLibrary();
		lib.addElement(this.theImage);
	}

	public Image getImage() {
		return this.theImage;
	}

	public void setInitFrom(String initFrom) {
		this.theImage.setInitFrom(initFrom);
	}
}
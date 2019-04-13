package com.dddviewr.collada.states;

import org.xml.sax.Attributes;

import com.dddviewr.collada.State;
import com.dddviewr.collada.StateManager;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.visualscene.LibraryVisualScenes;
import com.dddviewr.collada.content.visualscene.VisualScene;

public class visual_scene extends State {
	protected VisualScene visualScene;

	public void init(String name, Attributes attrs, StateManager mngr) {
		super.init(name, attrs, mngr);
		this.visualScene = new VisualScene(attrs.getValue("id"), attrs
				.getValue("name"));
		LibraryVisualScenes library = ((library_visual_scenes) getParent())
				.getLibrary();
		library.addElement(this.visualScene);
	}

	public VisualScene getVisualScene() {
		return this.visualScene;
	}

	public void addNode(Node n) {
		this.visualScene.addNode(n);
	}
}
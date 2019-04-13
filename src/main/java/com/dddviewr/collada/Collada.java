package com.dddviewr.collada;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.dddviewr.collada.content.animation.LibraryAnimations;
import com.dddviewr.collada.content.controller.Controller;
import com.dddviewr.collada.content.controller.LibraryControllers;
import com.dddviewr.collada.content.controller.Skin;
import com.dddviewr.collada.content.effects.Effect;
import com.dddviewr.collada.content.effects.LibraryEffects;
import com.dddviewr.collada.content.geometry.Geometry;
import com.dddviewr.collada.content.geometry.LibraryGeometries;
import com.dddviewr.collada.content.geometry.Mesh;
import com.dddviewr.collada.content.images.Image;
import com.dddviewr.collada.content.images.LibraryImages;
import com.dddviewr.collada.content.materials.LibraryMaterials;
import com.dddviewr.collada.content.materials.Material;
import com.dddviewr.collada.content.nodes.LibraryNodes;
import com.dddviewr.collada.content.nodes.Node;
import com.dddviewr.collada.content.scene.Scene;
import com.dddviewr.collada.content.visualscene.LibraryVisualScenes;
import com.dddviewr.collada.format.Base;
import com.dddviewr.log.Log;


public class Collada extends Base {
	protected LibraryGeometries libraryGeometries;
	protected LibraryVisualScenes libraryVisualScenes;
	protected LibraryControllers libraryControllers;
	protected LibraryAnimations libraryAnimations;
	protected LibraryImages libraryImages;
	protected LibraryMaterials libraryMaterials;
	protected LibraryEffects libraryEffects;
	protected LibraryNodes libraryNodes;
	protected Scene scene;
	
	protected String authoringTool = "";
	protected String upAxis = "Z_UP";
	protected Unit unit;
	
	public static XMLReader reader;

	public LibraryGeometries getLibraryGeometries() {
		return libraryGeometries;
	}

	public void setLibraryGeometries(LibraryGeometries libraryGeometries) {
		this.libraryGeometries = libraryGeometries;
	}

	public LibraryVisualScenes getLibraryVisualScenes() {
		return libraryVisualScenes;
	}

	public void setLibraryVisualScenes(LibraryVisualScenes libraryVisualScenes) {
		this.libraryVisualScenes = libraryVisualScenes;
	}

	public LibraryControllers getLibraryControllers() {
		return libraryControllers;
	}

	public void setLibraryControllers(LibraryControllers libraryControllers) {
		this.libraryControllers = libraryControllers;
	}

	public LibraryAnimations getLibraryAnimations() {
		return libraryAnimations;
	}

	public void setLibraryAnimations(LibraryAnimations libraryAnimations) {
		this.libraryAnimations = libraryAnimations;
	}

	public LibraryImages getLibraryImages() {
		return libraryImages;
	}

	public void setLibraryImages(LibraryImages library) {
		this.libraryImages = library;
	}

	public LibraryMaterials getLibraryMaterials() {
		return libraryMaterials;
	}

	public void setLibraryMaterials(LibraryMaterials libraryMaterials) {
		this.libraryMaterials = libraryMaterials;
	}

	public LibraryEffects getLibraryEffects() {
		return libraryEffects;
	}

	public void setLibraryEffects(LibraryEffects libraryEffects) {
		this.libraryEffects = libraryEffects;
	}

	public LibraryNodes getLibraryNodes() {
		return libraryNodes;
	}

	public void setLibraryNodes(LibraryNodes libraryNodes) {
		this.libraryNodes = libraryNodes;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public String getUpAxis() {
		return upAxis;
	}

	public void setUpAxis(String upAxis) {
		this.upAxis = upAxis;
	}
	
	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getAuthoringTool() {
		return authoringTool;
	}

	public void setAuthoringTool(String authoringTool) {
		this.authoringTool = authoringTool;
	}

	public void dump(PrintStream out, int indent) {
		String prefix = createIndent(indent);
		out.println(prefix + "COLLADA");
		out.println(prefix + " (" + upAxis + ")");
		if (this.unit != null)
			this.unit.dump(out, indent + 1);
		if (this.libraryImages != null)
			this.libraryImages.dump(out, indent + 1);
		if (this.libraryMaterials != null)
			this.libraryMaterials.dump(out, indent + 1);
		if (this.libraryEffects != null)
			this.libraryEffects.dump(out, indent + 1);
		if (this.libraryGeometries != null)
			this.libraryGeometries.dump(out, indent + 1);
		if (this.libraryNodes != null)
			this.libraryNodes.dump(out, indent + 1);
		if (this.libraryControllers != null)
			this.libraryControllers.dump(out, indent + 1);
		if (this.libraryAnimations != null)
			this.libraryAnimations.dump(out, indent + 1);
		if (this.libraryVisualScenes != null)
			this.libraryVisualScenes.dump(out, indent + 1);
		if (this.scene != null)
			this.scene.dump(out, indent + 1);
	}

	public void deindexMeshes() {
		if(libraryGeometries == null) {
			return;
		}
		List<Geometry> geos = libraryGeometries.getElements();
		for (Geometry geo : geos) {
			Mesh mesh = geo.getMesh();
			if (mesh != null)
				mesh.deindex(this);
		}
	}
	
	public static Collada readFile(InputStream input) throws SAXException,
			FileNotFoundException, IOException {
		StateManager stateManager = new StateManager();
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(stateManager);
		reader.parse(new InputSource(input));
		Collada collada = stateManager.getCollada();
		return collada;
	}

	public static Collada readFile(String fname) throws SAXException,
			FileNotFoundException, IOException {
		return readFile(new FileInputStream(fname));
	}

	public static Collada readFile(URL file) throws SAXException,
			FileNotFoundException, IOException {
		StateManager stateManager = new StateManager();
		if(reader == null) {
			try {
				SAXParserFactory factory;
				factory = (SAXParserFactory) Class.forName(
						"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl").newInstance();
				//factory = SAXParserFactory.newInstance();
				Log.log("SAXParserFactory: " + factory);
				//factory.setNamespaceAware(true);
				//factory.setValidating(false);
				//factory.setXIncludeAware(false);
				reader = factory.newSAXParser().getXMLReader();
				
			} catch (Throwable t) {
				Log.exception(t);
			}
		}			
		Log.log("XMLReader: " + reader);
		reader.setContentHandler(stateManager);
		try {
			Log.log("Start parsing");
			reader.parse(new InputSource(file.openStream()));
			Log.log("Parsing done");
		} catch ( Throwable t) {
			Log.exception(t);
		}
		Collada collada = stateManager.getCollada();
		return collada;
	}
	
	public List<Skin> findSkins(int source) {
		if (this.libraryControllers == null)
			return new ArrayList<Skin>();
		return this.libraryControllers.findSkins(source);
	}

	public Controller findController(int id) {
		return this.libraryControllers.getElement(id);
	}

	public Effect findEffect(int id) {
		return this.libraryEffects.getElement(id);
	}

	public Image findImage(int id) {
		return this.libraryImages.getElement(id);
	}

	public Material findMaterial(int id) {
		return this.libraryMaterials.getElement(id);
	}

	public Geometry findGeometry(int id) {
		return this.libraryGeometries.getElement(id);
	}

	public Node findNode(int id) {
		return this.libraryNodes.getElement(id);
	}
}
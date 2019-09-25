package com.dddviewr.collada.content.effects;

import java.io.PrintStream;

import com.dddviewr.collada.format.Base;

public class Texture extends Base {
	protected int texture;
	protected int texcoord;

	public Texture(String texture, String texcoord) {
		this.setTexture(texture);
		this.setTexcoord(texcoord);
	}

	public int getTexcoord() {
		return this.texcoord;
	}

	public void setTexcoord(int texcoord) {
		this.texcoord = texcoord;
	}
	
	public void setTexcoord(String coord) {
		this.texcoord = Integer.valueOf(coord.substring(5));
	}

	public int getTexture() {
		return this.texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public void setTexture(String texture) {
		this.texture = Integer.valueOf(texture.substring(2));
	}
	
	public String toString() {
		return "Texture (texture: " + this.texture
				+ ", texcoord: " + this.texcoord + ")";
	}
}
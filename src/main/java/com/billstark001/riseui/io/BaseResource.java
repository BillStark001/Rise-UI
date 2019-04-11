package com.billstark001.riseui.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BaseResource {
	
	private InputStream stream;
	
	public enum ResourceType {
		NULL,
		IMAGE,
		OBJ,
		MTL;
	}
	private ResourceType type;
	private boolean read;
	private Object resource;
	public void setType(ResourceType type) {this.type = type;}
	public ResourceType getType() {return this.type;}
	
	public BaseResource(InputStream stream, ResourceType type) {
		this.read = false;
		this.stream = stream;
		this.type = type;
	}
	public BaseResource(InputStream stream) {this(stream, ResourceType.NULL);}
	private BaseResource() {
		this.read = false;
		byte[] b = new byte[16];
		this.stream = new ByteArrayInputStream(b);
		this.type = ResourceType.NULL;
	}
	public static BaseResource MISSING_RES = new BaseResource();
	
	public boolean isRead() {return read;}
	
}

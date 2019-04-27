package com.billstark001.riseui.io;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public final class IOUtils {

	public static InputStream getInputStream(ResourceLocation loc) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
	}
	
}

package com.billstark001.riseui.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//Quotation: net.minecraft.client.renderer.texture.TextureManager
@SideOnly(Side.CLIENT)
public class ByteResourceLoader implements IResourceManagerReloadListener
{
	private static final int BYTE_BUFFER = 4096;
    private static final Logger LOGGER = LogManager.getLogger();
    public static final byte[] MISSING_RES = new byte[0];
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    
    private final Map<ResourceLocation, byte[]> mapRes = Maps.<ResourceLocation, byte[]>newHashMap();
    private final IResourceManager res;

    private ByteResourceLoader(IResourceManager resMan) {this.res = resMan;}
    public static final ByteResourceLoader INSTANCE = new ByteResourceLoader(Minecraft.getMinecraft().getResourceManager());
    public static ByteResourceLoader getInstance() {return INSTANCE;}
    
    public boolean loadRes(ResourceLocation location) {return loadRes(location, MISSING_RES);}
    public boolean loadRes(ResourceLocation location, byte[] res)
    {
        boolean flag = true;
        if (mapRes.containsKey(location)) return flag;
        try
        {
        	InputStream input = this.res.getResource(location).getInputStream();
    		byte[] buffer = new byte[BYTE_BUFFER];
    		ArrayList<byte[]> cache = new ArrayList<byte[]>();
    		int b = 0;
    		int acc = 0;
    		while (b > -1) {
    			b = input.read(buffer);
    			if (b == -1) break;
    			if (b != BYTE_BUFFER) buffer = Arrays.copyOf(buffer, b);
    			acc += b;
    			cache.add(buffer.clone());
    		}
    		input.close();
    		byte[] bfinal = new byte[acc];
    		for (int i = 0; i < cache.size(); ++i) {
    			System.arraycopy(cache.get(i), 0, bfinal, i * BYTE_BUFFER, cache.get(i).length);
    		}
    		res = bfinal;
        }
        catch (IOException ioexception)
        {
            if (location != RESOURCE_LOCATION_EMPTY)
            {
                LOGGER.warn("Failed to load resource: {}", location, ioexception);
            }

            res = ByteResourceLoader.MISSING_RES;
            this.mapRes.put(location, res);
            flag = false;
        }
        this.mapRes.put(location, res);
        return flag;
    }
    
    public byte[] getRes(ResourceLocation location)
    {
        return this.mapRes.get(location);
    }
    
    public void delRes(ResourceLocation location)
    {
        byte[] res = this.getRes(location);

        if (res != null)
        {
            this.mapRes.remove(location);
        }
    }

    public void onResourceManagerReload(IResourceManager res)
    {
        net.minecraftforge.fml.common.ProgressManager.ProgressBar bar = net.minecraftforge.fml.common.ProgressManager.push("Reloading Rise UI Byte Resource Manager", this.mapRes.keySet().size(), true);
        Iterator<Entry<ResourceLocation, byte[]>> iterator = this.mapRes.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry<ResourceLocation, byte[]> entry = iterator.next();
            bar.step(entry.getKey().toString());
            byte[] s = entry.getValue();

            if (s.equals(ByteResourceLoader.MISSING_RES))
            {
                iterator.remove();
            }
            else
            {
                this.loadRes(entry.getKey(), s);
            }
        }
        net.minecraftforge.fml.common.ProgressManager.pop(bar);
    }
}
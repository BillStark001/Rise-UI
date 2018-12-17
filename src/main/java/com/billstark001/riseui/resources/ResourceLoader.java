package com.billstark001.riseui.resources;

import java.io.IOException;
import java.io.InputStream;
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
public class ResourceLoader implements IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MISSING_RES = "";
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    
    private final Map<ResourceLocation, String> mapRes = Maps.<ResourceLocation, String>newHashMap();
    private final IResourceManager res;

    private ResourceLoader(IResourceManager resMan)
    {
        this.res = resMan;
    }
    
    public static final ResourceLoader INSTANCE = new ResourceLoader(Minecraft.getMinecraft().getResourceManager());
    public static ResourceLoader getInstance() {return INSTANCE;}
    
    public boolean loadRes(ResourceLocation location) {return loadRes(location, MISSING_RES);}

    public boolean loadRes(ResourceLocation location, String res)
    {
        boolean flag = true;
        if (mapRes.containsKey(location)) return flag;
        try
        {
        	InputStream input = this.res.getResource(location).getInputStream();
    		StringBuffer s = new StringBuffer();
    		int b = 0;
    		while (b != -1) {
    			b = input.read();
    			s = s.append((char)b);
    		}
    		input.close();
    		res = s.toString();
        }
        catch (IOException ioexception)
        {
            if (location != RESOURCE_LOCATION_EMPTY)
            {
                LOGGER.warn("Failed to load resource: {}", location, ioexception);
            }

            res = this.MISSING_RES;
            this.mapRes.put(location, res);
            flag = false;
        }
        this.mapRes.put(location, res);
        return flag;
    }
    
    public String getRes(ResourceLocation location)
    {
        return this.mapRes.get(location);
    }
    
    public void delRes(ResourceLocation location)
    {
        String res = this.getRes(location);

        if (res != null)
        {
            this.mapRes.remove(location);
        }
    }

    public void onResourceManagerReload(IResourceManager res)
    {
        net.minecraftforge.fml.common.ProgressManager.ProgressBar bar = net.minecraftforge.fml.common.ProgressManager.push("Reloading Rise UI 3D Resource Manager", this.mapRes.keySet().size(), true);
        Iterator<Entry<ResourceLocation, String>> iterator = this.mapRes.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry<ResourceLocation, String> entry = (Entry)iterator.next();
            bar.step(entry.getKey().toString());
            String s = entry.getValue();

            if (s.equals(this.MISSING_RES))
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
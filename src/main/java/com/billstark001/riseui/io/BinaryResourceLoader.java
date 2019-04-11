package com.billstark001.riseui.io;

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
public class BinaryResourceLoader implements IResourceManagerReloadListener
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final BaseResource MISSING_RES = BaseResource.MISSING_RES;
    public static final ResourceLocation RESOURCE_LOCATION_EMPTY = new ResourceLocation("");
    
    private final Map<ResourceLocation, BaseResource> mapRes = Maps.<ResourceLocation, BaseResource>newHashMap();
    private final IResourceManager res;

    private BinaryResourceLoader(IResourceManager resMan){this.res = resMan;}
    public static final BinaryResourceLoader INSTANCE = new BinaryResourceLoader(Minecraft.getMinecraft().getResourceManager());
    public static BinaryResourceLoader getInstance() {return INSTANCE;}
    
    public boolean loadRes(ResourceLocation location, BaseResource.ResourceType type, BaseResource res)
    {
        boolean flag = true;
        if (mapRes.containsKey(location)) return flag;
        try
        {
        	InputStream input = this.res.getResource(location).getInputStream();
    		res = new BaseResource(input, type);
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
    public boolean loadRes(ResourceLocation location, BaseResource res) {return loadRes(location, res.getType(), res);}
    public boolean loadRes(ResourceLocation location, BaseResource.ResourceType type) {return loadRes(location, type, this.MISSING_RES);}
    
    public BaseResource getRes(ResourceLocation location)
    {
        return this.mapRes.get(location);
    }
    
    public void delRes(ResourceLocation location)
    {
        BaseResource res = this.getRes(location);
        if (res != null) {this.mapRes.remove(location);}
    }

    public void onResourceManagerReload(IResourceManager res)
    {
        net.minecraftforge.fml.common.ProgressManager.ProgressBar bar = net.minecraftforge.fml.common.ProgressManager.push("Reloading Rise UI 3D Resource Manager", this.mapRes.keySet().size(), true);
        Iterator<Entry<ResourceLocation, BaseResource>> iterator = this.mapRes.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry<ResourceLocation, BaseResource> entry = iterator.next();
            bar.step(entry.getKey().toString());
            BaseResource s = entry.getValue();

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
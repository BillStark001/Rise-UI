package com.billstark001.wdcs.common;

import com.billstark001.wdcs.WorldConsciousness;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.command.CommandBase;
import net.minecraft.util.ResourceLocation;

public class CommonProxy {

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
    {
		
    }

	@EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }

	@EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }
    
    @EventHandler
    public void registerCommands(FMLServerStartingEvent e){
    	e.registerServerCommand(new CommandWdcsCaller());
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(WorldConsciousness.instance);
        System.out.println("Server Starting!");
    }
    
}

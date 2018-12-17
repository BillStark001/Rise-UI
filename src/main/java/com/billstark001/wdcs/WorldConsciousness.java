package com.billstark001.wdcs;

import com.billstark001.wdcs.common.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = WorldConsciousness.MODID,name=WorldConsciousness.NAME,version = WorldConsciousness.VERSION)

public class WorldConsciousness {
	
	public static final String MODID="wdcs";
	public static final String VERSION="0.1";
	public static final String NAME="World Consciousness";

	@Instance(WorldConsciousness.MODID)
    public static WorldConsciousness instance;

    @SidedProxy(clientSide = "com.billstark001.wdcs.common.ClientProxy", serverSide = "com.billstark001.wdcs.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
    	proxy.preInit(event);
    }
    @EventHandler
    public void init(FMLInitializationEvent event){
    	proxy.init(event);
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	proxy.postInit(event);
    }
    @EventHandler
    public void registerCommands(FMLServerStartingEvent e){
    	proxy.registerCommands(e);
    }
}

package com.billstark001.riseui;

import com.billstark001.riseui.common.CommonProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = RiseUI.MODID, name = RiseUI.NAME, version = RiseUI.VERSION)

public class RiseUI {

	public static final String MODID = "riseui";
	public static final String VERSION = "0.3";
	public static final String NAME = "Rise UI";

	@Instance(RiseUI.MODID)
	private static RiseUI INSTANCE = new RiseUI();

	public static RiseUI getInstance() {
		return INSTANCE;
	}

	@SidedProxy(clientSide = "com.billstark001.riseui.client.ClientProxy", serverSide = "com.billstark001.riseui.common.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}

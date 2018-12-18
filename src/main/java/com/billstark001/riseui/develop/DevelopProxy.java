package com.billstark001.riseui.develop;

import org.lwjgl.input.Keyboard;

import com.billstark001.riseui.events.RenderTileOverlayEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class DevelopProxy {

	public static final KeyBinding TOGGLE_RENDER = new KeyBinding("Start/Stop Rise UI Renders", Keyboard.KEY_H, "Rise UI");
	public static final KeyBinding TOGGLE_GRID = new KeyBinding("Start/Stop Rendering Tile Grids", Keyboard.KEY_Z, "Rise UI");
	public static final KeyBinding TOGGLE_MOB = new KeyBinding("Start/Stop Rendering Mobs", Keyboard.KEY_X, "Rise UI");
	public static final KeyBinding TOGGLE_ORE = new KeyBinding("Start/Stop Rendering Ores", Keyboard.KEY_C, "Rise UI");
	public static final KeyBinding TOGGLE_MAP = new KeyBinding("Start/Stop Rendering MiniMap", Keyboard.KEY_M, "Rise UI");
	public static final KeyBinding TEST = new KeyBinding("Test", Keyboard.KEY_J, "Rise UI");
	
	public static boolean isStarted=false;
	public static int mark1 = 0;
	public static boolean renderGrid=true;
	public static boolean renderMob=true;
	public static boolean renderOre=true;
	public static boolean renderMap=true;
	
	private DevelopProxy() {}
	private static final DevelopProxy INSTANCE = new DevelopProxy();
	public static DevelopProxy getInstance() {return INSTANCE;}
	
	public void CallInit(FMLPostInitializationEvent event) {
		ClientRegistry.registerKeyBinding(TOGGLE_RENDER);
    	ClientRegistry.registerKeyBinding(TOGGLE_GRID);
    	ClientRegistry.registerKeyBinding(TOGGLE_MOB);
    	ClientRegistry.registerKeyBinding(TOGGLE_ORE);
    	ClientRegistry.registerKeyBinding(TOGGLE_MAP);
    	RenderTestObject.prepareRender();
	}
	
	public static void speakToPlayer(EntityPlayer player, String level, String words) {
    	player.sendMessage(new TextComponentString("[RiseUI | " + level + "] " + words));
    }
    public static void speakToPlayer(EntityPlayer player, String words) {
    	speakToPlayer(player, "DEV", words);
    }
    
    @SubscribeEvent
   	public void toggleRender(PlayerTickEvent e) {
   		EntityPlayer player = Minecraft.getMinecraft().player;
   		World world=Minecraft.getMinecraft().world;

   		if(TOGGLE_RENDER.isPressed()){
   			speakToPlayer(player, "The Deep°·Dark°‚Fantasy.");
   			isStarted=!isStarted;
   		}
   		if(TOGGLE_GRID.isPressed()){
   			speakToPlayer(player, "Grid's Deep°·Dark°‚Fantasy.");
   			renderGrid=!renderGrid;
   		}
   		if(TOGGLE_MOB.isPressed()){
   			speakToPlayer(player, "Mob's Deep°·Dark°‚Fantasy.");
   			renderMob=!renderMob;
   		}
   		if(TOGGLE_ORE.isPressed()){
   			speakToPlayer(player, "Ore's Deep°·Dark°‚Fantasy.");
   			renderOre=!renderOre;
   		}
   		if(TOGGLE_MAP.isPressed()){
   			speakToPlayer(player, "MiniMap's Deep°·Dark°‚Fantasy.");
   			renderMap=!renderMap;
   		}
   	}
	
	@SubscribeEvent
   	public void devTest(PlayerTickEvent e) {
   		EntityPlayer player = Minecraft.getMinecraft().player;
   		World world=Minecraft.getMinecraft().world;
   		if(TEST.isPressed()){
   			mark1 += 1;
   			mark1 %= 3;
   			speakToPlayer(player, Integer.toString(player.getHeldItem(EnumHand.MAIN_HAND).getRepairCost()));
   		}
   	}
	
	@SubscribeEvent
	public void doRender(RenderTileOverlayEvent e) {
		if (isStarted) {
			RenderTestObject.doRender(e.getPartialTicks());
			RenderHUD.doRender(e.getPartialTicks());
		}
	}

}

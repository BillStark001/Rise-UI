package com.billstark001.riseui.develop;

import org.lwjgl.input.Keyboard;

import com.billstark001.riseui.client.events.RenderAdvancedEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.ListenerList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class DevelopProxy {

	public static final KeyBinding TOGGLE_RENDER = new KeyBinding("Start/Stop Rise UI Renders", Keyboard.KEY_H, "Rise UI");
	public static final KeyBinding TOGGLE_GRID = new KeyBinding("Start/Stop Rendering Tile Grids", Keyboard.KEY_Z, "Rise UI");
	public static final KeyBinding TOGGLE_MOB = new KeyBinding("Start/Stop Rendering Mobs", Keyboard.KEY_X, "Rise UI");
	public static final KeyBinding TOGGLE_ORE = new KeyBinding("Start/Stop Rendering Ores", Keyboard.KEY_C, "Rise UI");
	public static final KeyBinding TEST = new KeyBinding("Test", Keyboard.KEY_J, "Rise UI");
	public static final KeyBinding RECOMPILE = new KeyBinding("Recompile List", Keyboard.KEY_K, "Rise UI");
	public static final KeyBinding REASSIGN = new KeyBinding("Reassign List Number", Keyboard.KEY_L, "Rise UI");
	
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
    	ClientRegistry.registerKeyBinding(REASSIGN);
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
   			speakToPlayer(player, "The Deep��Dark��Fantasy.");
   			isStarted=!isStarted;
   		}
   		if(TOGGLE_GRID.isPressed()){
   			speakToPlayer(player, "Grid's Deep��Dark��Fantasy.");
   			renderGrid=!renderGrid;
   		}
   		if(TOGGLE_MOB.isPressed()){
   			speakToPlayer(player, "Mob's Deep��Dark��Fantasy.");
   			renderMob=!renderMob;
   		}
   		if(TOGGLE_ORE.isPressed()){
   			speakToPlayer(player, "Ore's Deep��Dark��Fantasy.");
   			renderOre=!renderOre;
   		}
   	}
	
	@SubscribeEvent
   	public void devTest(PlayerTickEvent e) {
   		EntityPlayer player = Minecraft.getMinecraft().player;
   		World world=Minecraft.getMinecraft().world;
   		if(TEST.isPressed()){
   			mark1 += 1;
   			mark1 %= 2;
   			speakToPlayer(player, Integer.toString(this.mark1));
   			
   			RenderTestObject.horse.markRecompile();
   			RenderTestObject.table.markRecompile();
   		}
   	}
	
	/*
	@SubscribeEvent
   	public void recompile(PlayerTickEvent e) {
   		EntityPlayer player = Minecraft.getMinecraft().player;
   		World world=Minecraft.getMinecraft().world;
   		if(RECOMPILE.isPressed()){
   			RenderTestObject.horse.compileList();
   			RenderTestObject.sphere.compileList();
   			RenderTestObject.table.compileList();
   			speakToPlayer(player, String.format("Recompiled: %d, %d, %d", RenderTestObject.horse.getDisplayList(), RenderTestObject.table.getDisplayList(), RenderTestObject.sphere.getDisplayList()));
   		}
   		if(REASSIGN.isPressed()){
   			RenderTestObject.horse.clearDisplayList();
   			RenderTestObject.sphere.clearDisplayList();
   			RenderTestObject.table.clearDisplayList();
   			speakToPlayer(player, "Cleared");
   		}
   	}
	*/
	
	@SubscribeEvent
	public void doRender(RenderAdvancedEvent e) {
		if (isStarted) {
			RenderTestObject.doRender(e.getPartialTicks());
			RenderHUD.doRender(e.getPartialTicks());
		}
	}
	/*
	@SubscribeEvent
	public void detectRenderEvent(Event e) {
		if (e.getClass().getSimpleName().contains("Render")) {
			System.out.println(e);
			if (e.getClass().getSimpleName().contains("RenderTick")) {
				ListenerList el = e.getListenerList();
				el = e.getListenerList();
			}
			if (e.getClass().getSimpleName().contains("RenderFog")) {
				ListenerList elf = e.getListenerList();
				elf = e.getListenerList();
			}
		}
	}
*/
}

package com.billstark001.wdcs.common;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;


public class ClientProxy extends CommonProxy
{
	
	public static final KeyBinding WDCS_INFO = new KeyBinding("Broadcast Basic Information", Keyboard.KEY_G, "World Consciousness");
	
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    	ClientRegistry.registerKeyBinding(WDCS_INFO);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public static void speakToPlayer(EntityPlayer player, String level, String words) {
    	player.sendMessage(new TextComponentString("[WDCS | " + level + "] " + words));
    }
    public static void speakToPlayer(EntityPlayer player, String words) {
    	speakToPlayer(player, "DEV", words);
    }
    
    @SubscribeEvent
   	public void displayInfo(PlayerTickEvent e) {
   		EntityPlayer player = Minecraft.getMinecraft().player;
   		World world=Minecraft.getMinecraft().world;

   		if(WDCS_INFO.isPressed()){
   			speakToPlayer(player, "The Deep¡á Dark¡â Fantasy.");
   		}
   	}
}
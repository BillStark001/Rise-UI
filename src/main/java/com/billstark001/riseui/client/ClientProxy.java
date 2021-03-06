package com.billstark001.riseui.client;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.base.shading.shader.Shader;
import com.billstark001.riseui.base.shading.shader.States;
import com.billstark001.riseui.client.events.RenderAdvancedEvent;
import com.billstark001.riseui.common.CommonProxy;
import com.billstark001.riseui.computation.UtilsInteract;
import com.billstark001.riseui.core.polygon.Presets;
import com.billstark001.riseui.develop.DevelopProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(DevelopProxy.getInstance());
	}	

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		Presets.loadPresets();
		DevelopProxy.getInstance().CallInit(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void postRenderEvent(RenderWorldLastEvent e) {
		GlStateManager.pushMatrix();
		//Shader.SHADER_DIFFUSE.applyState();
		EntityPlayer player = Minecraft.getMinecraft().player;

		Vec3d CurrentPos = player.getPositionVector();
		Vec3d LastTickPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
		Vec3d Bias = UtilsInteract.numMult(CurrentPos.subtract(LastTickPos), e.getPartialTicks());
		Vec3d RenderPos = LastTickPos.add(Bias);
		GlStateManager.translate(-RenderPos.x + 0.5, -RenderPos.y + 0.5, -RenderPos.z + 0.5);

		MinecraftForge.EVENT_BUS.post(new RenderAdvancedEvent(e.getContext(), e.getPartialTicks()));
		//Shader.SHADER_DIFFUSE.applyState();
		GlStateManager.popMatrix();
		
	}
	
	@SubscribeEvent
	public void handleTickableTags(TickEvent e) {
		
	}

}
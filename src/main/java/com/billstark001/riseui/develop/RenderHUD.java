package com.billstark001.riseui.develop;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.client.ClientProxy;
import com.billstark001.riseui.computation.UtilsInteract;
import com.billstark001.riseui.render.GlHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class RenderHUD {
	
	private static GlHelper me = GlHelper.getInstance();
	private static EntityPlayer player = Minecraft.getMinecraft().player;
	private static World world = Minecraft.getMinecraft().world;
	
	private final static Vec3d scale = new Vec3d(1.28, 0.05, 0.72);
	
	public static void doRender(double delta){
		player = Minecraft.getMinecraft().player;
		world = Minecraft.getMinecraft().world;
		
		Vec3d CurrentPos = player.getPositionVector();
		Vec3d LastTickPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
		Vec3d LookPos = player.getLook((float) delta);
		double Yaw = -player.rotationYaw;
		double Pitch = player.rotationPitch;
		Vec3d MotionPos = new Vec3d(player.motionX, player.motionY, player.motionZ);
		Vec3d CurrentBlockPos = new Vec3d(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
		Vec3d Bias = UtilsInteract.numMult(CurrentPos.subtract(LastTickPos), delta);
		Vec3d RenderPos = LastTickPos.add(Bias);

		Chunk CurrentChunk = world.getChunkFromBlockCoords(player.getPosition());
		int ChunkX = CurrentChunk.x, ChunkZ = CurrentChunk.z;
		
		//Something done first
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glTranslated(0, player.getEyeHeight(), 0);
        
        //System.out.println(player.rotationPitch);
        //System.out.println(player.rotationYaw);
        //GL11.glTranslated(1, 1, 1);
        GL11.glRotated(Yaw, 0, 1, 0);
        GL11.glRotated(Pitch, 1, 0, 0);
        GL11.glRotated(90, 1, 0, 0);
        GL11.glRotated(180, 0, 1, 0);
        //Draw Opaque Elements
        
        
        GL11.glClear(GL11.GL_ACCUM);
        
        //Draw X-Ray Elements
        //drawConstraintGraph(0x00FFFF, 0xCC);
        //if(ClientProxy.renderMap) drawMiniMap(CurrentPos, ChunkX, ChunkZ, 0.01, 10);
        /*
        GL11.glClear(GL11.GL_ACCUM);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        engine.bindTexture(ClientProxy.TEX_MAIN_LAYER);
        GL11.glRotated(player.rotationYaw,0,-1,0);
        GL11.glRotated(player.rotationPitch,1,0,0);
        r.startDrawing(GL11.GL_QUADS);
        //prepareColor(1,t);
        r.setNormal(0,0,1);
        r.addVertexWithUV(-WIDTH, -LENGTH, SECOND, 0.0, 0.0); //����
        r.addVertexWithUV(WIDTH, -LENGTH, SECOND, 1, 0); //����
        r.addVertexWithUV(WIDTH, LENGTH, SECOND, 1.0, 1.0); //����
        r.addVertexWithUV(-WIDTH, LENGTH, SECOND, 0, 1); //����
        t.draw(); 
        GL11.glPopMatrix();
        */
        //Something done at last
        //DoubleBuffer d;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}
	

}

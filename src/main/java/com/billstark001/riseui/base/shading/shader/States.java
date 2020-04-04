package com.billstark001.riseui.base.shading.shader;

import org.lwjgl.opengl.GL11;

import com.billstark001.riseui.render.GlHelper;

import net.minecraft.client.renderer.GlStateManager;

public final class States {

	private States() {}
	
	private static GlHelper glh = GlHelper.getInstance();
	
	public static void debug() {
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		//System.out.println(1);
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
	}
	
	public static void standard_vert() {
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ZERO);
		glh.disableBlend();
	}

	public static void standard_edge() {
		GlStateManager.enableDepth();
		GlStateManager.disableLighting();
		GlStateManager.disableTexture2D();
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ZERO);
		glh.disableBlend();
	}

	public static void face_diffuse() {
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.enableAlpha();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		//System.out.println(0);
		//System.out.println(GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF));
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ZERO);
		glh.disableBlend();
	}
	
	public static void face_diffuse_soft_alpha() {
		GlStateManager.enableDepth();
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		//System.out.println(0);
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ZERO);
		glh.disableBlend();
		//GlStateManager.enableAlpha();
		//GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0.0F);
	}
	
	public static void face_light() {
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();
		GlStateManager.disableLighting();
		//System.out.println(1);
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
		//GlStateManager.enableAlpha();
		//GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0.0F);
	}
	
	public static void face_light_without_cull() {
		GlStateManager.enableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		//System.out.println(1);
		glh.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
		//GlStateManager.enableAlpha();
		//GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0.0F);
	}

}

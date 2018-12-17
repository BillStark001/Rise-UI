package com.billstark001.riseui.events;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderTileOverlayEvent extends Event {
	private final RenderGlobal context;
	private final float partialTicks;

	public RenderTileOverlayEvent(RenderGlobal context, float partialTicks) {
		this.context = context;
		this.partialTicks = partialTicks;
	}

	public RenderGlobal getContext() {
		return context;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
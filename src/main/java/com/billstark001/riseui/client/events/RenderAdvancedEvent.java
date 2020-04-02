package com.billstark001.riseui.client.events;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderAdvancedEvent extends Event {
	private final RenderGlobal context;
	private final float partialTicks;

	public RenderAdvancedEvent(RenderGlobal context, float partialTicks) {
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
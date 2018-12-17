package com.billstark001.wdcs.operations;

import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;

public class OperationRegenChunk implements IOperation{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "regenchunk";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "operations.regenchunk.desc";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "operations.regenchunk.usage";
	}

	@Override
	public String checkArgsValidity(String[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean execute(MinecraftServer server, EntityPlayer player, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		World world = server.getWorld(player.getSpawnDimension());
		WorldGenerator g = null;
		OreGenEvent e = null;
		NBTTagCompound t = null;
		return false;
	}

}

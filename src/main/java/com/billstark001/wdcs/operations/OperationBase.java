package com.billstark001.wdcs.operations;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class OperationBase implements IOperation{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "base";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "operations.base.desc";
	}

	@Override
	public String getUsage() {
		// TODO Auto-generated method stub
		return "operations.base.usage";
	}
	
	public void speakToPlayer(EntityPlayer player, String words) {
    	player.sendMessage(new TextComponentString("[WDCS | OP | " + getName().toUpperCase() + "] " + words));
    }

	@Override
	public boolean execute(MinecraftServer server, EntityPlayer player, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String checkArgsValidity(String[] args) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

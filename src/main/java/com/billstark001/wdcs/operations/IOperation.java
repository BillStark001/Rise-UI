package com.billstark001.wdcs.operations;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public interface IOperation {

	/**
     * Gets the name of the operation.
     */
	public String getName();
	
	/**
     * Gets the description of the operation.
     */
	public String getDescription();
	
	/**
     * Gets the usage of the operation.
     */
	public String getUsage();
	
	/**
     * Check the arguments' validity.
     * Returns null if valid, otherwise problem information.
     */
	public String checkArgsValidity(String[] args);
	
	/**
     * Called when execute.
     */
	public boolean execute(MinecraftServer server, EntityPlayer sender, String[] args) throws CommandException;
	
}

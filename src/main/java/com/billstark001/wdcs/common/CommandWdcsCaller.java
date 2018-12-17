package com.billstark001.wdcs.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.billstark001.wdcs.operations.IOperation;
import com.billstark001.wdcs.operations.OperationBase;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class CommandWdcsCaller extends CommandBase{
	
	private Map<String, IOperation> ops = new HashMap<String, IOperation>();

	public static final CommandWdcsCaller INSTANCE = new CommandWdcsCaller();
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "wdcs";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "commands.wdcs.caller.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		// TODO Auto-generated method stub
		if (args.length == 0 || (args[0] == "?" || args[0] == "help")) {
			ClientProxy.speakToPlayer((EntityPlayer) sender, "HELP", "commands.wdcs.caller.help");
			return;
		}
		String s = args[0];
		args = Arrays.copyOfRange(args, 1, args.length);
		if (!ops.containsKey(s)) throw new CommandException("No Opeartion called \"%s\"!".format(s));
		IOperation op = ops.get(s);
		if (!(op.checkArgsValidity(args) == null)) throw new CommandException("Wrong arguments for operation \"%s!\" (%s)".format(s, op.checkArgsValidity(args)));
		
	}

}

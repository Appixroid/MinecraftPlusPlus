package net.minecraft.command;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandServerKick extends CommandBase
{
	/**
	 * Gets the name of the command
	 */
	@Override
	public String getCommandName()
	{
		return "kick";
	}

	/**
	 * Return the required permission level for this command.
	 */
	@Override
	public int getRequiredPermissionLevel()
	{
		return 3;
	}

	/**
	 * Gets the usage string for the command.
	 */
	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "commands.kick.usage";
	}

	/**
	 * Callback for when the command is executed
	 */
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length > 0 && args[0].length() > 1)
		{
			EntityPlayerMP entityplayermp = server.getPlayerList().getPlayerByUsername(args[0]);

			if (entityplayermp == null)
			{
				throw new PlayerNotFoundException("commands.generic.player.notFound", new Object[] { args[0] });
			}
			else
			{
				if (args.length >= 2)
				{
					ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 1);
					entityplayermp.connection.func_194028_b(itextcomponent);
					notifyCommandListener(sender, this, "commands.kick.success.reason", new Object[] { entityplayermp.getName(), itextcomponent.getUnformattedText() });
				}
				else
				{
					entityplayermp.connection.func_194028_b(new TextComponentTranslation("multiplayer.disconnect.kicked", new Object[0]));
					notifyCommandListener(sender, this, "commands.kick.success", new Object[] { entityplayermp.getName() });
				}
			}
		}
		else
		{
			throw new WrongUsageException("commands.kick.usage", new Object[0]);
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		return args.length >= 1 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : Collections.emptyList();
	}
}

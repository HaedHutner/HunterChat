package com.atherys.chat.command;

import com.atherys.chat.AtherysChat;
import com.atherys.core.command.annotation.Aliases;
import com.atherys.core.command.annotation.Description;
import com.atherys.core.command.annotation.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import javax.annotation.Nonnull;

@Aliases("reload")
@Description("Reloads Chat Configuration")
@Permission("atheryschat.commands.reload")
public class ReloadCommand implements CommandExecutor {

    @Nonnull
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        AtherysChat.getInstance().getChannelFacade().reload(src);
        return CommandResult.success();
    }
}

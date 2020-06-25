package com.atherys.chat.command;

import com.atherys.chat.AtherysChat;
import com.atherys.core.command.PlayerCommand;
import com.atherys.core.command.annotation.Aliases;
import com.atherys.core.command.annotation.Children;
import com.atherys.core.command.annotation.Description;
import com.atherys.core.command.annotation.Permission;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

@Aliases("chat")
@Description("Base chat command.")
@Permission("atheryschat.commands")
@Children({
        JoinChannelCommand.class,
        LeaveChannelCommand.class,
        SpeakChannelCommand.class,
        ReloadCommand.class
})
public class ChatCommand implements PlayerCommand, CommandExecutor {
    @Override
    public CommandResult execute(Player src, CommandContext args) {
        AtherysChat.getInstance().getChannelFacade().displayPlayerChannels(src);
        return CommandResult.success();
    }
}

package com.atherys.chat.command;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.core.command.PlayerCommand;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class ChannelAliasCommand implements PlayerCommand, CommandExecutor {

    private AtherysChannel channel;

    public ChannelAliasCommand(AtherysChannel channel) {
        this.channel = channel;
    }

    public CommandSpec getSpec() {
        return CommandSpec.builder()
                .description(Text.of("Switch to/send a message to the",  this.channel.getTextName(), " channel."))
                .executor(this)
                .arguments(this.getArguments())
                .build();
    }

    public CommandResult execute(Player player, CommandContext args) throws CommandException {
        if (args.getOne("message").isPresent()) {
            AtherysChat.getInstance().getChannelFacade().speakToChannel(player, channel, args.<String>getOne("message").get());
        } else {
            AtherysChat.getInstance().getChannelFacade().joinChannel(player, channel);
        }
        return CommandResult.success();
    }

    public CommandElement[] getArguments() {
        return new CommandElement[]{
                GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message")))
        };
    }
}

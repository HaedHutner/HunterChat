package com.atherys.chat.command;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.core.command.ParameterizedCommand;
import com.atherys.core.command.PlayerCommand;
import com.atherys.core.command.annotation.Aliases;
import com.atherys.core.command.annotation.Description;
import com.atherys.core.command.annotation.Permission;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;

@Aliases({"speak", "say", "s"})
@Description("Speak to a channel channel")
@Permission("atheryschat.commands.speak")
public class SpeakChannelCommand implements PlayerCommand, ParameterizedCommand {
    @Nonnull
    @Override
    public CommandResult execute(@Nonnull Player source, @Nonnull CommandContext args) throws CommandException {
        AtherysChat.getInstance().getChannelFacade().speakToChannel(source, args.<AtherysChannel>getOne("channel").get(),
                args.<String>getOne("message").get());
        return CommandResult.success();
    }

    @Override
    public CommandElement[] getArguments() {
        return new CommandElement[]{
                new ChannelCommandElement(Text.of("channel")),
                GenericArguments.remainingJoinedStrings(Text.of("message"))
        };
    }
}

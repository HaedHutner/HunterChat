package com.atherys.chat.command;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.model.AtherysChannel;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChannelCommandElement extends CommandElement {
    private static TextTemplate exception = TextTemplate.of("No Channel called ", TextTemplate.arg("channel"), " found.");

    private boolean returnMemberChannels = true;
    private boolean returnNonMemberChannels = true;

    public ChannelCommandElement(@Nullable Text key) {
        super(key);
    }

    public ChannelCommandElement(@Nullable Text key, boolean returnMemberChannels, boolean returnNonMemberChannels) {
        super(key);
        this.returnMemberChannels = returnMemberChannels;
        this.returnNonMemberChannels = returnNonMemberChannels;
    }

    @Nullable
    @Override
    protected AtherysChannel parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String channel = args.next();
        if (channel.isEmpty()) {
            throw exception(channel, args);
        }
        return AtherysChat.getInstance().getChatService().getChannelById(channel.toLowerCase())
                .orElseThrow(() -> exception(channel, args));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if (!(src instanceof Player)) {
            return Collections.emptyList();
        }

        // Lists all channels that a player
        if (this.returnMemberChannels && this.returnNonMemberChannels) {
            return AtherysChat.getInstance().getChannelFacade().getPlayerVisibleChannels((Player) src).stream()
                    .map(AtherysChannel::getId)
                    .collect(Collectors.toList());
        // List all channels that the player
        } else if (this.returnNonMemberChannels) {
            return AtherysChat.getInstance().getChannelFacade().getPlayerNonMemberChannels((Player) src).stream()
                    .map(AtherysChannel::getId)
                    .collect(Collectors.toList());
        // List only joined channels
        } else if (this.returnMemberChannels) {
            return AtherysChat.getInstance().getChannelFacade().getPlayerMemberChannels((Player) src).stream()
                    .map(AtherysChannel::getId)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private static ArgumentParseException exception(String channel, CommandArgs args) {
        return args.createError(exception.apply(ImmutableMap.of("channel", channel)).build());
    }
}

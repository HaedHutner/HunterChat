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
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChannelCommandElement extends CommandElement {
    private static TextTemplate exception = TextTemplate.of("No Channel called ", TextTemplate.arg("channel"), " found.");

    public ChannelCommandElement(@Nullable Text key) {
        super(key);
    }

    @Nullable
    @Override
    protected AtherysChannel parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String channel = args.next();

        if (channel.isEmpty()) {
            throw exception(channel, args);
        }

        return AtherysChat.getInstance().getChatService().getChannelById(channel.toLowerCase()).orElseThrow(() -> exception(channel, args));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        if (!(src instanceof Player)) {
            return Collections.emptyList();
        }
        return AtherysChat.getInstance().getChannelFacade().getPlayerVisibleChannels((Player) src).stream()
                .map(channel -> channel.getId())
                .collect(Collectors.toList());
    }

    private static ArgumentParseException exception(String channel, CommandArgs args) {
        return args.createError(exception.apply(ImmutableMap.of("channel", channel)).build());
    }
}

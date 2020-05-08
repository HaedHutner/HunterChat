package com.atherys.chat.command;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.model.AtherysChannel;
import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChannelCommandElement extends CommandElement {
    private static TextTemplate exception = TextTemplate.of("No skill called ", TextTemplate.arg("skill"), " found.");

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

        return AtherysChat.getInstance().getChannelService().getChannelById(channel.toLowerCase()).orElseThrow(() -> exception(channel, args));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return AtherysChat.getInstance().getChannelService().getChannels().entrySet().stream()
                .filter(entry -> entry.getValue().getPermission() == null || src.hasPermission(entry.getValue().getPermission()))
                .filter(entry -> args.nextIfPresent().map(arg -> entry.getKey().startsWith(arg.toLowerCase())).orElse(true))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static ArgumentParseException exception(String skill, CommandArgs args) {
        return args.createError(exception.apply(ImmutableMap.of("skill", skill)).build());
    }
}

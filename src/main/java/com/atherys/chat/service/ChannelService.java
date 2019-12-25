package com.atherys.chat.service;

import com.atherys.chat.model.AtherysMessageChannel;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class ChannelService {

    private static final String READ_POSTFIX = ".read";

    private static final String WRITE_POSTFIX = "write";

    private static final String TOGGLE_POSTFIX = ".toggle";

    private static final String FORMAT_POSTFIX = ".format";

    public Collection<MessageReceiver> getChannelMembers(AtherysMessageChannel channel) {
        // If the channel is broadcast, return all players
        if (channel.isBroadcast()) {
            return Sponge.getServer().getOnlinePlayers().stream().map(p -> p.getCommandSource().orElse(null)).collect(Collectors.toSet());
        }

        // If the channel is not broadcast, return only players who have permissions to read
        PermissionService service = Sponge.getGame().getServiceManager().provideUnchecked(PermissionService.class);

        return service.getLoadedCollections().values().stream()
                .flatMap(input -> input.getLoadedWithPermission(getReadPermission(channel)).entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .map(entry -> entry.getKey().getCommandSource().orElse(null))
                        .filter(Objects::nonNull))
                .collect(Collectors.toSet());
    }

    public String getReadPermission(AtherysMessageChannel channel) {
        return channel.getPermission() + READ_POSTFIX;
    }

    public String getWritePermission(AtherysMessageChannel channel) {
        return channel.getPermission() + WRITE_POSTFIX;
    }

    public String getTogglePermission(AtherysMessageChannel channel) {
        return channel.getPermission() + TOGGLE_POSTFIX;
    }

    public String getFormatPostfix(AtherysMessageChannel channel) {
        return channel.getPermission() + FORMAT_POSTFIX;
    }
}

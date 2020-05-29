package com.atherys.chat.model;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.stream.Collectors;

public class GlobalChannel extends AtherysChannel {

    private final ChannelType type = ChannelType.GLOBAL;

    public GlobalChannel(String id) {
        super(id);
    }

    public Collection<MessageReceiver> getMembers(Object sender) {
        return Sponge.getServer().getOnlinePlayers().stream()
                .filter(player -> this.getPlayers().contains(player.getUniqueId()))
                .collect(Collectors.toSet());
    }
}

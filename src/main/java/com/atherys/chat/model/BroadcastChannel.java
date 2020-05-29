package com.atherys.chat.model;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.HashSet;

public class BroadcastChannel extends AtherysChannel {

    private final ChannelType type = ChannelType.BROADCAST;

    public BroadcastChannel(String id) {
        super(id);
    }

    public Collection<MessageReceiver> getMembers(Object sender) {
        return new HashSet<>(Sponge.getServer().getOnlinePlayers());
    }
}

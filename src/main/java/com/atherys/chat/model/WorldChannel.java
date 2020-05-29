package com.atherys.chat.model;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.stream.Collectors;

public class WorldChannel extends AtherysChannel {

    private final ChannelType type = ChannelType.WORLD;

    public WorldChannel(String id) {
        super(id);
    }

    public Collection<MessageReceiver> getMembers(Object sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            return player.getWorld().getPlayers().stream()
                    .filter(onlinePlayer -> this.getPlayers().contains(onlinePlayer.getUniqueId()))
                    .collect(Collectors.toSet());
        }
        return getMembers();
    }
}

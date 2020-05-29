package com.atherys.chat.model;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Collection;
import java.util.stream.Collectors;

public class RangeChannel extends AtherysChannel {

    private final ChannelType type = ChannelType.RANGE;

    private int range;

    public RangeChannel(String id, int range) {
        super(id);
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Collection<MessageReceiver> getMembers(Object sender) {
        if (sender instanceof Player && this.range > 0) {
            Player player = (Player) sender;

            return player.getNearbyEntities(range).stream()
                    .filter(entity -> entity instanceof Player && this.players.contains(entity.getUniqueId()))
                    .map(entity -> (Player) entity)
                    .collect(Collectors.toSet());
        }

        return getMembers();
    }
}

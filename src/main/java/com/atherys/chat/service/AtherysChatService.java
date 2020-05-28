package com.atherys.chat.service;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.config.AtherysChatConfig;
import com.atherys.chat.config.ChannelConfig;
import com.atherys.chat.model.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;

@Singleton
public class AtherysChatService implements ChatService {

    @Inject
    private AtherysChatConfig config;

    private Map<String, AtherysChannel> channels = new HashMap<>();

    private Map<UUID, AtherysChannel> playerSpeakingMap = new HashMap<>();

    private Set<AtherysChannel> autoJoinChannels = new HashSet<>();

    private AtherysChannel defaultChannel;

    public void init() {
        for (ChannelConfig channelConfig : config.CHANNELS) {
            AtherysChannel channel;

            switch (channelConfig.getType()) {
                case BROADCAST:
                    channel = new BroadcastChannel(channelConfig.getId());
                    break;
                case GLOBAL:
                    channel = new GlobalChannel(channelConfig.getId());
                    break;
                case WORLD:
                    channel = new WorldChannel(channelConfig.getId());
                    break;
                case RANGE:
                    channel = new RangeChannel(channelConfig.getId(), channelConfig.getRange());
                    break;
                default:
                    AtherysChat.getInstance().getLogger().error("Unknown Channel type: " + channelConfig.getType() +
                                                                " for channel" + channelConfig.getId());
                    channel = new GlobalChannel(channelConfig.getId());
            }

            channel.setPermission(channelConfig.getPermission());
            channel.setFormat(channelConfig.getFormat());
            channel.setName(channelConfig.getName());
            channel.setPrefix(channelConfig.getPrefix());
            channel.setSuffix(channelConfig.getSuffix());
            channel.setAliases(channelConfig.getAliases());

            registerChannel(channel);
        }

        this.defaultChannel = channels.get(config.DEFAULT_CHANNEL);
    }

    public void registerChannel(AtherysChannel channel) {
        this.channels.put(channel.getId(), channel);

        // TODO: Deal with channel commands

        if (config.AUTO_JOIN_CHANNELS.contains(channel.getId())) {
            this.autoJoinChannels.add(channel);
        }
    }

    public void reload() {
        // TODO: Support reloading configuration
    }

    public Map<String, AtherysChannel> getChannels() {
        return channels;
    }

    public void setDefaultChannels(Player player) {
        for (AtherysChannel channel : autoJoinChannels) {
            addPlayerToChannel(player, channel);
        }
        addPlayerToChannel(player, defaultChannel);
        setPlayerSpeakingChannel(player, defaultChannel);
    }

    public AtherysChannel getPlayerSpeakingChannel(Player player) {
        return playerSpeakingMap.getOrDefault(player.getUniqueId(), defaultChannel);
    }

    public void setPlayerSpeakingChannel(Player player, AtherysChannel channel) {
        playerSpeakingMap.put(player.getUniqueId(), channel);
    }

    public AtherysChannel getPlayerChannel(Player player) {
        for (AtherysChannel channel : channels.values()) {
            if (channel.getPlayers().contains(player.getUniqueId())) {
                return channel;
            }
        }
        return defaultChannel;
    }

    public Optional<AtherysChannel> getChannelById(String id) {
        return Optional.ofNullable(channels.get(id));
    }

    public void addPlayerToChannel(Player player, AtherysChannel channel) {
        channel.getPlayers().add(player.getUniqueId());
    }

    public void removePlayerFromChannel(Player player, AtherysChannel channel) {
        channel.getPlayers().remove(player.getUniqueId());
    }
}

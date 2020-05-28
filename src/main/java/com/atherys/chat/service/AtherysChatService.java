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

import java.nio.channels.Channel;
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

        for (Map.Entry<String, ChannelConfig> entry : config.CHANNELS.entrySet()) {
            String id = entry.getKey();
            ChannelConfig channelConfig = entry.getValue();
            AtherysChannel channel;

            switch (channelConfig.type) {
                case BROADCAST:
                    channel = new BroadcastChannel(id);
                    break;
                case GLOBAL:
                    channel = new GlobalChannel(id);
                    break;
                case WORLD:
                    channel = new WorldChannel(id);
                    break;
                case RANGE:
                    channel = new RangeChannel(id, channelConfig.range);
                    break;
                default:
                    AtherysChat.getInstance().getLogger().error("Unknown Channel type: " + channelConfig.type +
                                                                " for channel" + id);
                    channel = new GlobalChannel(id);
            }

            channel.setPermission(channelConfig.permission);
            channel.setFormat(channelConfig.format);
            channel.setName(channelConfig.name);
            channel.setPrefix(channelConfig.prefix);
            channel.setSuffix(channelConfig.suffix);
            channel.setAliases(channelConfig.aliases);

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

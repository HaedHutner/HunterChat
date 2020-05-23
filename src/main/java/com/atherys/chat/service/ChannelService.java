package com.atherys.chat.service;

import com.atherys.chat.config.AtherysChatConfig;
import com.atherys.chat.config.ChannelConfig;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.core.AtherysCore;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.ChatTypeMessageReceiver;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class ChannelService {

    private static final String READ_POSTFIX = ".read";

    private static final String WRITE_POSTFIX = ".write";

    private static final String TOGGLE_POSTFIX = ".toggle";

    private static final String FORMAT_POSTFIX = ".format";

    @Inject
    private AtherysChatConfig config;

    private Map<String, AtherysChannel> channels = new HashMap<>();

    private Map<UUID, AtherysChannel> playerSpeakingMap = new HashMap<>();

    private Set<AtherysChannel> autoJoinChannels = new HashSet<>();

    private AtherysChannel defaultChannel;

    public void init() {
        for (ChannelConfig channelConfig : config.CHANNELS) {
            AtherysChannel channel = new AtherysChannel(channelConfig.getId());

            channel.setBroadcast(channelConfig.isBroadcastChannel());
            channel.setPrefix(TextSerializers.FORMATTING_CODE.deserialize(channelConfig.getPrefix()));
            channel.setColor(channelConfig.getColor());
            channel.setPermission(channelConfig.getPermission());
            channel.setRange(channelConfig.getRange());

            registerChannel(channel);

            if (config.AUTO_JOIN_CHANNELS.contains(channel.getId())) {
                this.autoJoinChannels.add(channel);
            }
        }

        this.defaultChannel = channels.get(config.DEFAULT_CHANNEL);
    }

    public void registerChannel(AtherysChannel channel) {
        this.channels.put(channel.getId(), channel);
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

    public Text getNameWithPrefix(CommandSource commandSource) {
        String name = commandSource.getName();
        return Text.of(TextSerializers.FORMATTING_CODE.deserialize(commandSource.getOption("prefix").orElse("")), " ", name, ": ");
    }

    public Optional<Text> transformMessage(AtherysChannel channel, Object sender, MessageReceiver receiver, Text original) {
        AtherysCore.getInstance().getLogger().info("Messaged");
        Text.Builder builder = Text.builder();
        builder.append(channel.getPrefix());

        Text body = original;
        if (sender instanceof CommandSource) {
            builder.append(getNameWithPrefix((CommandSource) sender));
            String raw = original.toPlain();
            raw = raw.replaceFirst("<" + ((CommandSource) sender).getName() + ">", "").trim();
            body = TextSerializers.FORMATTING_CODE.deserialize(raw);
        }
        builder.append(Text.of(channel.getColor(), body));

        return Optional.of(builder.build());
    }

    public Collection<MessageReceiver> getChannelMembers(AtherysChannel channel) {
        // If the channel is broadcast, return all players
        if (channel.isBroadcast()) {
            return new ArrayList<>(Sponge.getServer().getOnlinePlayers());
        }

        return Sponge.getServer().getOnlinePlayers().stream()
                .filter(player -> channel.getPlayers().contains(player.getUniqueId()))
                .collect(Collectors.toSet());
    }

    public void setEventChannel(MessageChannelEvent.Chat event, Player player) {
        AtherysChannel channel = getPlayerSpeakingChannel(player);
        event.setChannel(channel);
    }

    public void sendToChannel(AtherysChannel channel, @Nullable Object sender, Text original, ChatType type) {
        checkNotNull(original, "original text");
        checkNotNull(type, "type");
        for (MessageReceiver member : channel.getMembers(sender)) {
            if (member instanceof ChatTypeMessageReceiver) {
                channel.transformMessage(sender, member, original, type).ifPresent(text -> ((ChatTypeMessageReceiver) member).sendMessage(type, text));
            } else {
                channel.transformMessage(sender, member, original, type).ifPresent(member::sendMessage);
            }
        }
    }

    public String getReadPermission(AtherysChannel channel) {
        return channel.getPermission() + READ_POSTFIX;
    }

    public String getWritePermission(AtherysChannel channel) {
        return channel.getPermission() + WRITE_POSTFIX;
    }

    public String getTogglePermission(AtherysChannel channel) {
        return channel.getPermission() + TOGGLE_POSTFIX;
    }

    public String getFormatPostfix(AtherysChannel channel) {
        return channel.getPermission() + FORMAT_POSTFIX;
    }
}

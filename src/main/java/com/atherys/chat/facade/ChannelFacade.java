package com.atherys.chat.facade;

import com.atherys.chat.config.AtherysChatConfig;
import com.atherys.chat.exception.AtherysChatException;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.chat.service.ChatService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ChannelFacade {

    @Inject
    private AtherysChatConfig chatConfig;

    @Inject
    private ChatService chatService;

    @Inject
    private ChatMessagingFacade cmf;

    public ChannelFacade() {
    }

    public void onPlayerJoin(Player player) {
        chatService.setDefaultChannels(player);
    }

    public void onPlayerChat(MessageChannelEvent.Chat event, Player player) {
        // TODO: Deal with Permissions
        AtherysChannel channel = chatService.getPlayerSpeakingChannel(player);
        event.setChannel(channel);
    }

    public Set<AtherysChannel> getPlayerChannels(Player player) {
        return chatService.getChannels().values().stream()
                .filter(channel -> channel.getPlayers().contains(player.getUniqueId()))
                .collect(Collectors.toSet());
    }

    public Set<AtherysChannel> getPlayerVisibleChannels(Player player) {
        return chatService.getChannels().values().stream()
                .filter(channel -> channel.getPermission() == null || player.hasPermission(channel.getPermission()))
                .collect(Collectors.toSet());
    }

    public void joinChannel(Player source, AtherysChannel channel) throws CommandException {
        if (channel.getPermission() != null && !source.hasPermission(channel.getPermission())) {
            throw new AtherysChatException("You do not have permission to join the ", channel.getTextName(), " channel.");
        }

        addPlayerToChannel(source, channel);
    }

    public void leaveChannel(Player source, AtherysChannel channel) throws CommandException {
        if (channel.getPlayers().contains(source.getUniqueId())) {
            removePlayerFromChannel(source, channel);
        } else {
            throw new AtherysChatException("You are not in that channel.");
        }
    }

    public void removePlayerFromChannel(Player player, AtherysChannel channel) {
        chatService.removePlayerFromChannel(player, channel);

        // If this is the players speaking channel, set it to another channel they are in
        if (channel == chatService.getPlayerSpeakingChannel(player)) {
            chatService.setPlayerSpeakingChannel(player, chatService.getPlayerChannel(player));
        }
    }

    public void addPlayerToChannel(Player player, AtherysChannel channel) {
        chatService.addPlayerToChannel(player, channel);
        chatService.setPlayerSpeakingChannel(player, channel);
        cmf.info(player, "You are now chatting in ", channel.getTextName(), ".");
    }

    public void speakToChannel(Player player, AtherysChannel channel, String message) {
        channel.send(player, Text.of(message));
    }
}


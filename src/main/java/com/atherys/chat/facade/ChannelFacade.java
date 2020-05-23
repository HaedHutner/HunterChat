package com.atherys.chat.facade;

import com.atherys.chat.config.AtherysChatConfig;
import com.atherys.chat.exception.AtherysChatException;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.chat.service.ChannelService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

@Singleton
public class ChannelFacade {

    public static final String TEMPLATE_DELIMETER = "%";

    @Inject
    private AtherysChatConfig chatConfig;

    @Inject
    private ChannelService channelService;

    @Inject
    private ChatMessagingFacade cmf;

    public ChannelFacade() {
    }

    public void onPlayerJoin(Player player) {
        channelService.setDefaultChannels(player);
    }

    public void joinChannel(Player source, AtherysChannel channel) throws CommandException {
        if (channel.getPermission() != null && !source.hasPermission(channel.getPermission())) {
            throw new AtherysChatException("You do not have permission to join the ", channel.getId(), " channel.");
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
        channelService.removePlayerFromChannel(player, channel);

        // If this is the players speaking channel, set it to another channel they are in
        if (channel == channelService.getPlayerSpeakingChannel(player)) {
            channelService.setPlayerSpeakingChannel(player, channelService.getPlayerChannel(player));
        }
    }

    public void addPlayerToChannel(Player player, AtherysChannel channel) {
        channelService.addPlayerToChannel(player, channel);
        channelService.setPlayerSpeakingChannel(player, channel);
        cmf.info(player, "You are now chatting in ", channel.getColor(), channel.getId(), ".");
    }

    public void speakToChannel(Player player, AtherysChannel channel, String message) {
        channel.send(player, Text.of(message));
    }
}


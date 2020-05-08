package com.atherys.chat.facade;

import com.atherys.chat.AtherysChatConfig;
import com.atherys.chat.exception.AtherysChatException;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.chat.service.ChannelService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.PermissionService;

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

    public void joinChannel(Player source, AtherysChannel channel) throws CommandException {
        if (channel.getPermission() != null && !source.hasPermission(channel.getPermission())) {
            throw new AtherysChatException("You do not have permission to join the ", channel.getId(), " channel.");
        }

        source.setMessageChannel(channel);
        channelService.addPlayerToChannel(source, channel);
        cmf.info(source, "You are now chatting in ", channel.getColor(), channel.getId(), ".");
    }

    public void leaveChannel(Player source, AtherysChannel channel) throws CommandException {
        if (channel.getPlayers().contains(source.getUniqueId())) {
            channelService.removePlayerFromChannel(source, channel);
            joinChannel(source, channelService.getPlayerChannel(source));
        } else {
            throw new AtherysChatException("You are not in that channel.");
        }
    }
}


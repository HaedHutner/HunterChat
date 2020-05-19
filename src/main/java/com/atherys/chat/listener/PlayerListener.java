package com.atherys.chat.listener;

import com.atherys.chat.facade.ChannelFacade;
import com.atherys.chat.service.ChannelService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;

@Singleton
public class PlayerListener {

    @Inject
    private ChannelFacade channelFacade;

    @Inject
    private ChannelService channelService;

    @Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        channelFacade.onPlayerJoin(event.getTargetEntity());
    }

    @Listener
    public void onChat(MessageChannelEvent.Chat event) {
        channelService.setEventChannel(event);
    }
}

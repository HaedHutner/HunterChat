package com.atherys.chat.event;

import com.atherys.chat.model.AtherysChannel;
import com.atherys.chat.service.ChatService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

public class ChatRegistrationEvent implements Event {

    private Cause cause;

    private ChatService chatService;

    public ChatRegistrationEvent(ChatService chatService) {
        cause = Cause.of(Sponge.getCauseStackManager().getCurrentContext(), chatService);
        this.chatService = chatService;
    }

    public void registerChannels(AtherysChannel... channels) {
        for (AtherysChannel channel : channels) {
            chatService.registerChannel(channel);
        }
    }

    @Override
    public Cause getCause() {
        return cause;
    }
}

package com.atherys.chat;

import com.atherys.chat.service.AtherysChatService;
import com.atherys.chat.service.ChatService;
import com.google.inject.AbstractModule;

public class AtherysChatModule extends AbstractModule {
    @Override
    public void configure() {
        bind(ChatService.class).to(AtherysChatService.class);
    }
}

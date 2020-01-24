package com.atherys.chat.facade;

import com.atherys.core.facade.MessagingFacade;
import com.google.inject.Singleton;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

@Singleton
public class ChatMessagingFacade implements MessagingFacade {
    @Override
    public Text getPrefix() {
        return Text.of(TextColors.DARK_GREEN, "[", TextColors.DARK_GRAY, "Chat", TextColors.DARK_GREEN, "] ", TextColors.RESET);
    }
}

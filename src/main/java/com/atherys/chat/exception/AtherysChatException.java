package com.atherys.chat.exception;

import com.atherys.chat.AtherysChat;
import org.spongepowered.api.command.CommandException;

public class AtherysChatException extends CommandException {
    public AtherysChatException(Object... message) {
        super(AtherysChat.getInstance().getChatMessagingFacade().formatError(message));
    }
}

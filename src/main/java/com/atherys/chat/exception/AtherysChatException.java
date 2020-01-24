package com.atherys.chat.exception;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.text.Text;

public class AtherysChatException extends CommandException {
    public AtherysChatException(Text message) {
        super(message);
    }
}

package com.atherys.chat.facade;

import com.atherys.chat.model.AtherysChannel;
import com.atherys.chat.service.ChatService;
import com.atherys.core.utils.AbstractMessagingFacade;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;

@Singleton
public class ChatMessagingFacade extends AbstractMessagingFacade {

    @Inject
    private ChatService chatService;

    public ChatMessagingFacade() {
        super("Chat");
    }

    public Optional<Text> formatMessage(AtherysChannel channel, Object sender, MessageReceiver receiver, Text original) {
        String playerName = "";
        String world = "";
        String prefix = "";
        String suffix = "";
        String message = original.toPlain();

        // Remove the player information from the message
        if (sender instanceof CommandSource) {
            CommandSource commandSource = (CommandSource) sender;
            playerName = commandSource.getName();
            prefix = commandSource.getOption("prefix").orElse("");
            suffix = commandSource.getOption("suffix").orElse("");
            message = message.replaceFirst("<" + playerName + ">", "").trim();

            if (!chatService.hasFormatPermission(commandSource, channel)) {
                message = TextSerializers.FORMATTING_CODE.stripCodes(message);
            }
        }

        if (sender instanceof Player) {
            world = ((Player) sender).getWorld().getName();
        }

        // Replace variables
        String format = channel.getFormat()
                .replace("%prefix", prefix)
                .replace("%suffix", suffix)
                .replace("%cprefix", channel.getPrefix())
                .replace("%csuffix", channel.getSuffix())
                .replace("%player", playerName)
                .replace("%message", message)
                .replace("%world", world);

        Text.Builder builder = Text.builder();
        builder.append(TextSerializers.FORMATTING_CODE.deserialize(format));
        return Optional.of(builder.build());
    }
}

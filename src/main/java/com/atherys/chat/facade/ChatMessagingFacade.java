package com.atherys.chat.facade;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.model.AtherysChannel;
import com.atherys.core.utils.AbstractMessagingFacade;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;
import java.util.regex.Pattern;

@Singleton
public class ChatMessagingFacade extends AbstractMessagingFacade {
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

            // Check for formatting permissions
            if (!channel.hasFormatPermission(commandSource)) {
                Pattern formatCodes = Pattern.compile("(?i)&([a-f0-9rl-ok])");
                message = message.replaceAll(formatCodes.pattern(), "");
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

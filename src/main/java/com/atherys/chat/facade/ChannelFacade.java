package com.atherys.chat.facade;

import com.atherys.chat.AtherysChatConfig;
import com.atherys.chat.model.AtherysMessageChannel;
import com.atherys.chat.service.ChannelService;
import com.atherys.core.template.TemplateEngine;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ChannelFacade {

    @Inject
    private AtherysChatConfig chatConfig;

    @Inject
    private ChannelService channelService;

    private Set<AtherysMessageChannel> channels;

    public ChannelFacade() {
    }

    public void init() {
        channels = chatConfig.CHANNELS.stream().map(config -> {
            AtherysMessageChannel messageChannel = new AtherysMessageChannel();
            messageChannel.setId(config.getId());
            messageChannel.setPermission(config.getPermission());
            messageChannel.setTemplate(createChannelTemplate(config.getTemplate()));
            messageChannel.setBroadcast(config.isBroadcastChannel());

            return messageChannel;
        }).collect(Collectors.toSet());
    }

    public void sendMessageTo(String id, CommandSource sender, Text original) throws CommandException {
        Optional<AtherysMessageChannel> messageChannel = channels.stream().filter(channel -> channel.getId().equals(id)).findAny();

        if (!messageChannel.isPresent()) {
            // TODO: Throw exception
            return;
        }

        sendMessageTo(messageChannel.get(), sender, original);
    }

    public void sendMessageTo(AtherysMessageChannel channel, Object sender, Text original) {

        // TODO: Check if players has send permission

        channel.send(sender, original);
    }

    private TextTemplate createChannelTemplate(String template) {

        // TODO: Create TextTemplate from the string-based template retrieved from the config

        return TextTemplate.of(TextTemplate.arg("PLAYER"), ": ", TextTemplate.arg("MESSAGE"));
    }

    public Optional<Text> transformMessage(AtherysMessageChannel channel, Object sender, MessageReceiver recipient, Text original, ChatType type) {
        // TODO: Construct system for text template arguments

        Map<String, Text> arguments = new HashMap<>();

        arguments.put("PLAYER", Text.of(sender));
        arguments.put("MESSAGE", original);

        return Optional.of(channel.getTemplate().apply(arguments).toText());
    }
}

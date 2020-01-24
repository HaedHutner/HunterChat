package com.atherys.chat.facade;

import com.atherys.chat.AtherysChatConfig;
import com.atherys.chat.api.ChatContext;
import com.atherys.chat.api.ChatPlaceholder;
import com.atherys.chat.exception.AtherysChatException;
import com.atherys.chat.model.AtherysMessageChannel;
import com.atherys.chat.service.ChannelService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ChannelFacade {

    public static final String TEMPLATE_DELIMETER = "%";

    @Inject
    private AtherysChatConfig chatConfig;

    @Inject
    private ChannelService channelService;

    @Inject
    private ChatMessagingFacade cmf;

    @Inject
    private PermissionService permissionService;

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

    // TODO: Create command for sending messages to specific channel
    public void sendMessageTo(String id, CommandSource sender, Text original) throws CommandException {
        Optional<AtherysMessageChannel> messageChannel = channels.stream().filter(channel -> channel.getId().equals(id)).findAny();

        if (!messageChannel.isPresent()) {
            throw new AtherysChatException(cmf.formatError("The requested channel does not exist."));
        }

        sendMessageTo(messageChannel.get(), sender, original);
    }

    public void sendMessageTo(AtherysMessageChannel channel, Object sender, Text original) throws CommandException {

        // If the sender is a subject, check permissions
        // Otherwise, send message anyway ( assumed that the sender is the console )
        if (sender instanceof Subject) {
            Subject senderSubject = (Subject) sender;

            if (!senderSubject.hasPermission(channelService.getWritePermission(channel))) {
                throw new AtherysChatException(cmf.formatError("Lacking required permissions to send a message to this channel."));
            }
        }

        channel.send(sender, original);
    }

    private TextTemplate createChannelTemplate(String template) {
        // tokenize the string
        StringTokenizer tokenizer = new StringTokenizer(template, TEMPLATE_DELIMETER);

        boolean skipPlaceholderCheck = true;

        List<Object> templateElements = new ArrayList<>();

        // collect all tokens into the list of template elements, adding a space after each
        // but not after recognized placeholder tokens
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            // Even tokens are potential placeholders, odd ones are not
            // "asdf %token% asdf %token%"
            //  1st   2nd    3rd   4th
            if (skipPlaceholderCheck) {
                templateElements.add(token);
            } else {
                Optional<ChatPlaceholder> placeholder = Sponge.getRegistry().getType(ChatPlaceholder.class, token.substring(1, token.length() - 1));

                if (placeholder.isPresent()) {
                    // add the token as a text template argument
                    templateElements.add(TextTemplate.arg(placeholder.get().getId()));
                } else {
                    // if a placeholder could not be found from the token, add it as it appears in the string template
                    templateElements.add("%" + token + "%");
                }
            }

            skipPlaceholderCheck = !skipPlaceholderCheck;
        }

        return TextTemplate.of(templateElements);
    }

    public Optional<Text> transformMessage(AtherysMessageChannel channel, Object sender, MessageReceiver recipient, Text original, ChatType type) {
        ChatContext chatContext = new ChatContext(channel, sender, recipient, original, type);
        return Optional.ofNullable(chatContext.renderMessage());
    }
}

package com.atherys.chat.api;

import com.atherys.chat.model.AtherysMessageChannel;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;

import java.util.*;

public class ChatContext {

    private AtherysMessageChannel channel;

    private Object sender;

    private MessageReceiver recipient;

    private Text original;

    private ChatType type;

    private Set<ChatPlaceholder> chatPlaceholders = new HashSet<>();

    public ChatContext(AtherysMessageChannel channel, Object sender, MessageReceiver recipient, Text original, ChatType type) {
        this.channel = channel;
        this.sender = sender;
        this.recipient = recipient;
        this.original = original;
        this.type = type;
    }

    public void setPlaceholders(Set<ChatPlaceholder> chatPlaceholders) {
        this.chatPlaceholders = chatPlaceholders;
    }

    public Set<ChatPlaceholder> getChatPlaceholders() {
        return chatPlaceholders;
    }

    public void addPlaceholders(ChatPlaceholder... placeholders) {
        chatPlaceholders.addAll(Arrays.asList(placeholders));
    }

    public void removePlaceholders(ChatPlaceholder... placeholders) {
        chatPlaceholders.removeAll(Arrays.asList(placeholders));
    }

    public AtherysMessageChannel getChannel() {
        return channel;
    }

    public Object getSender() {
        return sender;
    }

    public MessageReceiver getRecipient() {
        return recipient;
    }

    public Text getOriginal() {
        return original;
    }

    public ChatType getType() {
        return type;
    }

    public Text applyPlaceholders(TextTemplate template) {
        Map<String, Object> templateArguments = new HashMap<>();

        chatPlaceholders.forEach(placeholder -> {
            templateArguments.put(placeholder.getId().toUpperCase(), placeholder.getSupplier().apply(this));
        });

        return template.apply(templateArguments).toText();
    }

    public Text renderMessage() {
        return applyPlaceholders(channel.getTemplate());
    }

}

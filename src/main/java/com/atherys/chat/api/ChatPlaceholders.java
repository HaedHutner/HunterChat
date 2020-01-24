package com.atherys.chat.api;

public final class ChatPlaceholders {

    public static final ChatPlaceholder SENDER = ChatPlaceholder.of("sender", ChatContext::getSender);

    public static final ChatPlaceholder ORIGINAL_MESSAGE = ChatPlaceholder.of("original_message", ChatContext::getOriginal);

    public static final ChatPlaceholder CHANNEL_ID = ChatPlaceholder.of("channel_id", chatContext -> chatContext.getChannel().getId());

}

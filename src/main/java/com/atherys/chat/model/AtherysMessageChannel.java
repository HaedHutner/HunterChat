package com.atherys.chat.model;

import com.atherys.chat.AtherysChat;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Optional;

public class AtherysMessageChannel implements MessageChannel {

    private String id;

    private String permission;

    private TextTemplate template;

    private boolean broadcast;

    public AtherysMessageChannel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public TextTemplate getTemplate() {
        return template;
    }

    public void setTemplate(TextTemplate template) {
        this.template = template;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public void send(@Nullable Object sender, Text original) {
        AtherysChat.getInstance().getChannelFacade().sendMessageTo(this, sender, original);
    }

    @Override
    public Optional<Text> transformMessage(@Nullable Object sender, MessageReceiver recipient, Text original, ChatType type) {
        return AtherysChat.getInstance().getChannelFacade().transformMessage(this, sender, recipient, original, type);
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return AtherysChat.getInstance().getChannelService().getChannelMembers(this);
    }
}

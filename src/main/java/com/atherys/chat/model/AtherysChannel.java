package com.atherys.chat.model;

import com.atherys.chat.AtherysChat;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.ChatTypeMessageReceiver;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;


public abstract class AtherysChannel implements MessageChannel {

    private static final String READ_POSTFIX = ".read";

    private static final String WRITE_POSTFIX = ".write";

    private static final String LEAVE_POSTFIX = ".toggle";

    private static final String FORMAT_POSTFIX = ".format";

    private String id;

    private String name;

    private String permission;

    private String format;

    private String prefix;

    private String suffix;

    private Set<String> aliases;

    protected Set<UUID> players;

    public AtherysChannel(String id) {
        this.id = id;
        this.players = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Text getTextName() {
        return TextSerializers.FORMATTING_CODE.deserialize(name);
    }

    public void setName(String name) {
        this.name = Optional.ofNullable(name).orElse(id);
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = Optional.ofNullable(permission).orElse("atheryschat.channels." + id);;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = Optional.ofNullable(prefix).orElse("");
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = Optional.ofNullable(suffix).orElse("");
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UUID> players) {
        this.players = players;
    }

    public boolean hasReadPermission(CommandSource src) {
        return src.hasPermission(this.permission + READ_POSTFIX);
    }

    public boolean hasWritePermission(CommandSource src) {
        return src.hasPermission(this.permission + WRITE_POSTFIX);
    }

    public boolean hasLeavePermission(CommandSource src) {
        return src.hasPermission(this.permission + LEAVE_POSTFIX);
    }

    public boolean hasFormatPermission(CommandSource src) {
        return src.hasPermission(this.permission + FORMAT_POSTFIX);
    }

    @Override
    public Optional<Text> transformMessage(@Nullable Object sender, MessageReceiver recipient, Text original, ChatType type) {
        return AtherysChat.getInstance().getChatMessagingFacade().formatMessage(this, sender, recipient, original);
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return Sponge.getServer().getOnlinePlayers().stream()
                .filter(player -> getPlayers().contains(player.getUniqueId()))
                .collect(Collectors.toSet());
    }

    public abstract Collection<MessageReceiver> getMembers(Object sender);

    public void send(@Nullable Object sender, Text original, ChatType type) {
        checkNotNull(original, "original text");
        checkNotNull(type, "type");

        for (MessageReceiver member : this.getMembers(sender)) {
            if (member instanceof Player && !this.hasReadPermission((Player) member)) {
                // Allow a user to read their own messages
                if (sender == null || !sender.equals(member)) {
                    continue;
                }
            }
            if (member instanceof ChatTypeMessageReceiver) {
                this.transformMessage(sender, member, original, type).ifPresent(text -> ((ChatTypeMessageReceiver) member).sendMessage(type, text));
            } else {
                this.transformMessage(sender, member, original, type).ifPresent(member::sendMessage);
            }
        }
    }
}

package com.atherys.chat.model;

import com.atherys.chat.AtherysChat;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.format.TextColor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


public class AtherysChannel implements MessageChannel {

    private String id;

    private String permission;

    private boolean broadcast;

    private Text prefix;

    private TextColor color;

    private Set<UUID> players;

    private int range;

    public AtherysChannel(String id) {
        this.id = id;
        this.players = new HashSet<>();
    }

    public String getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public Text getPrefix() {
        return prefix;
    }

    public void setPrefix(Text prefix) {
        this.prefix = prefix;
    }

    public TextColor getColor() {
        return color;
    }

    public void setColor(TextColor color) {
        this.color = color;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UUID> players) {
        this.players = players;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public Optional<Text> transformMessage(@Nullable Object sender, MessageReceiver recipient, Text original, ChatType type) {
        return AtherysChat.getInstance().getChannelService().transformMessage(this, sender, recipient, original);
    }

    @Override
    public Collection<MessageReceiver> getMembers() {
        return AtherysChat.getInstance().getChannelService().getChannelMembers(this);
    }

    public Collection<MessageReceiver> getMembers(Object sender) {
        if (sender instanceof Player && this.range > 0) {
            Player player = (Player) sender;

            return player.getNearbyEntities(range).stream()
                    .filter(entity -> entity instanceof Player && this.players.contains(entity.getUniqueId()))
                    .map(entity -> (Player) entity)
                    .collect(Collectors.toSet());
        }

        return getMembers();
    }

    public void send(@Nullable Object sender, Text original, ChatType type) {
        AtherysChat.getInstance().getChannelService().sendToChannel(this, sender, original, type);
    }
}

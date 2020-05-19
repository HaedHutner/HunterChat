package com.atherys.chat.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashSet;
import java.util.Set;

@ConfigSerializable
public class ChannelConfig {

    @Setting("id")
    private String id;

    /**
     * If the configured permission is "example.plugin.someChat",
     * then the following permissions can be assigned:
     *
     * * "example.plugin.someChat.read" -- for read access
     * * "example.plugin.someChat.write" -- for write access
     * * "example.plugin.someChat.format" -- for sending formatted messages
     *
     * If this is null, then by default it is understood that all players have permissions to read, write, format
     */
    @Setting("permission")
    private String permission;

    @Setting("is-broadcast")
    private boolean broadcastChannel = true;

    @Setting("color")
    private TextColor color = TextColors.WHITE;

    @Setting("prefix")
    private String prefix = "[Default]";

    @Setting("range")
    private int range;

    public ChannelConfig() {
        this.id = "default";
    }

    public String getId() {
        return id;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isBroadcastChannel() {
        return broadcastChannel;
    }

    public TextColor getColor() {
        return color;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getRange() {
        return range;
    }

}

package com.atherys.chat.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChannelConfig {

    @Setting("id")
    private String id;

    @Setting("template")
    private String template; // Example: "&c[&rSomeChat&c]&r %PLAYER_NAME%: %MESSAGE%"

    /**
     * If the configured permission is "example.plugin.someChat",
     * then the following permissions can be assigned:
     *
     * * "example.plugin.someChat.read" -- for read access
     * * "example.plugin.someChat.write" -- for write access
     * * "example.plugin.someChat.format" -- for sending formatted messages
     * * "example.plugin.someChat.toggle" -- for toggling this chat on/off as their default destination for chat messages
     *
     * If this is null, then by default it is understood that all players have permissions to read, write, format and toggle.
     */
    @Setting("permission")
    private String permission;

    @Setting("is-broadcast")
    private boolean broadcastChannel;

    public ChannelConfig() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(boolean broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }
}

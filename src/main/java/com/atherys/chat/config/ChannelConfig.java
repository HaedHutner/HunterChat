package com.atherys.chat.config;

import com.atherys.chat.model.ChannelType;
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

    @Setting("name")
    private String name;

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

    @Setting("type")
    private ChannelType type = ChannelType.GLOBAL;

    @Setting("format")
    private String format = "%cprefix %player: %message %csuffix";

    @Setting("prefix")
    private String prefix = "[Default]";

    @Setting("suffix")
    private String suffix;

    @Setting("aliases")
    private Set<String> aliases;

    @Setting("range")
    private int range;

    public ChannelConfig() {
        this.id = "default";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ChannelType getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getPermission() {
        return permission;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public int getRange() {
        return range;
    }

}

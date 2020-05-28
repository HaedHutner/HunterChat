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

    @Setting("name")
    public String name = "&7Global";

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
    public String permission;

    @Setting("type")
    public ChannelType type = ChannelType.GLOBAL;

    @Setting("format")
    public String format = "%cprefix %player: %message %csuffix";

    @Setting("prefix")
    public String prefix = "[§2Global&r]";

    @Setting("suffix")
    public String suffix;

    @Setting("aliases")
    public Set<String> aliases;

    @Setting("range")
    public int range;

}

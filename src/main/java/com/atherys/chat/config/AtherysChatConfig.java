package com.atherys.chat.config;

import com.atherys.chat.AtherysChat;
import com.atherys.chat.config.ChannelConfig;
import com.atherys.chat.model.ChannelType;
import com.atherys.core.utils.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton
public class AtherysChatConfig extends PluginConfig {

    @Setting("channels")
    public Map<String, ChannelConfig> CHANNELS = new HashMap<>();
    {
        // Setup some default channels
        CHANNELS.put("global", new ChannelConfig());

        ChannelConfig local = new ChannelConfig();
        local.name = "&3Local";
        local.type = ChannelType.RANGE;
        local.prefix = "[&3Local&r]";
        local.range = 20;
        CHANNELS.put("local", local);

        ChannelConfig broadcast = new ChannelConfig();
        broadcast.name = "&4Broadcast";
        broadcast.type = ChannelType.BROADCAST;
        broadcast.prefix = "[&4Broadcast&r]";
        CHANNELS.put("broadcast", broadcast);

        ChannelConfig world = new ChannelConfig();
        world.name = "&5World";
        world.type = ChannelType.WORLD;
        world.prefix = "[&5World&r]";
        CHANNELS.put("world", world);
    }

    @Setting(value = "default-channel", comment = "The default channel players will speak to")
    public String DEFAULT_CHANNEL = "global";

    @Setting(value = "auto-join-channels", comment = "List of channels to join upon login")
    public Set<String> AUTO_JOIN_CHANNELS = new HashSet<>();
    {
        AUTO_JOIN_CHANNELS.add("local");
    }

    protected AtherysChatConfig() throws IOException {
        super("config/" + AtherysChat.ID, "config.conf");
    }
}

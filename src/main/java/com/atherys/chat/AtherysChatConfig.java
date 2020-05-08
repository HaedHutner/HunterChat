package com.atherys.chat;

import com.atherys.chat.config.ChannelConfig;
import com.atherys.core.utils.PluginConfig;
import ninja.leaping.configurate.objectmapping.Setting;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AtherysChatConfig extends PluginConfig {

    @Setting("channels")
    public Set<ChannelConfig> CHANNELS = new HashSet<>();
    {
        CHANNELS.add(new ChannelConfig());
    }

    @Setting("default-channel")
    public String DEFAULT_CHANNEL = "default";

    protected AtherysChatConfig() throws IOException {
        super("config/" + AtherysChat.ID, "config.conf");
    }
}

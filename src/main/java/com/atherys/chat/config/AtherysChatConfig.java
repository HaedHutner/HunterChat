package com.atherys.chat.config;

import com.atherys.chat.AtherysChat;
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

    @Setting(value = "default-channel", comment = "The default channel players will speak to")
    public String DEFAULT_CHANNEL = "default";

    @Setting(value = "auto-join-channels", comment = "List of channels to join upon login")
    public Set<String> AUTO_JOIN_CHANNELS = new HashSet<>();

    protected AtherysChatConfig() throws IOException {
        super("config/" + AtherysChat.ID, "config.conf");
    }
}

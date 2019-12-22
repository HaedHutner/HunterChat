package com.atherys.chat;

import com.atherys.core.utils.PluginConfig;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class AtherysChatConfig extends PluginConfig {

    protected AtherysChatConfig() throws IOException {
        super("config/" + AtherysChat.ID, "config.conf");
    }
}

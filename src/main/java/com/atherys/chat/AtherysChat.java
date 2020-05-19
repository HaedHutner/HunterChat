package com.atherys.chat;

import com.atherys.chat.command.ChatCommand;
import com.atherys.chat.facade.ChannelFacade;
import com.atherys.chat.facade.ChatMessagingFacade;
import com.atherys.chat.listener.PlayerListener;
import com.atherys.chat.service.ChannelService;
import com.atherys.core.command.CommandService;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import static com.atherys.chat.AtherysChat.*;

@Plugin(id = ID, version = VERSION, name = NAME, description = DESCRIPTION, dependencies = {
        @Dependency(id = "atheryscore")
})
public class AtherysChat {
    public static final String ID = "atheryschat";
    public static final String NAME = "A'therys Chat";
    public static final String DESCRIPTION = "A chat plugin for the A'therys Horizons server";
    public static final String VERSION = "%PLUGIN_VERSION%";

    private static AtherysChat instance;

    private static boolean init;

    @Inject
    private Logger logger;

    @Inject
    PluginContainer container;

    @Inject
    Injector spongeInjector;

    Injector chatInjector;

    Components components;

    public static AtherysChat getInstance() {
        return instance;
    }

    private void init () {
        instance = this;

        components = new Components();

        chatInjector = spongeInjector.createChildInjector();
        chatInjector.injectMembers(components);

        // Register listeners
        Sponge.getEventManager().registerListeners(this, components.playerListener);

        try {
            CommandService.getInstance().register(new ChatCommand(), this);
        } catch (CommandService.AnnotatedCommandException e) {
            e.printStackTrace();
        }

        components.config.init();

        init = true;
    }

    private void start() {
        getChannelService().init();
    }

    private void stop() {
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        init();
    }

    @Listener
    public void onStart(GameStartingServerEvent event) {
        if (init) {
            start();
        }
    }

    @Listener
    public void onStop(GameStoppedServerEvent event) {
        if (init) {
            stop();
        }
    }

    public ChannelFacade getChannelFacade() {
        return components.channelFacade;
    }

    public ChannelService getChannelService() {
        return components.channelService;
    }

    public Logger getLogger() {
        return logger;
    }

    public ChatMessagingFacade getChatMessagingFacade() {
        return components.chatMessagingFacade;
    }

    private static class Components {
        @Inject
        AtherysChatConfig config;

        @Inject
        ChannelFacade channelFacade;

        @Inject
        ChannelService channelService;

        @Inject
        ChatMessagingFacade chatMessagingFacade;

        @Inject
        PlayerListener playerListener;
    }
}

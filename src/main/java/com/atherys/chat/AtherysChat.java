package com.atherys.chat;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
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

    private void init () {
        instance = this;

        chatInjector = spongeInjector.createChildInjector();

        init = true;
    }

    private void start() {

    }

    private void stop() {

    }

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
}

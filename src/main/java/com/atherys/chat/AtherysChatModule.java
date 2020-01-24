package com.atherys.chat;

import com.atherys.chat.api.ChatPlaceholderRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.permission.PermissionService;

public class AtherysChatModule extends AbstractModule {
    @Override
    public void configure() {
        bind(PermissionService.class).toProvider(() -> {
            return Sponge.getServiceManager().provide(PermissionService.class).orElse(null);
        }).in(Scopes.SINGLETON);
    }
}

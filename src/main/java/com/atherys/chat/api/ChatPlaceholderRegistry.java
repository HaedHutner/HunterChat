package com.atherys.chat.api;

import org.spongepowered.api.registry.CatalogRegistryModule;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChatPlaceholderRegistry implements CatalogRegistryModule<ChatPlaceholder> {

    private Map<String, ChatPlaceholder> registry = new HashMap<>();

    public ChatPlaceholderRegistry() {
    }

    @Override
    public Optional<ChatPlaceholder> getById(String id) {
        return Optional.ofNullable(registry.get(id));
    }

    @Override
    public Collection<ChatPlaceholder> getAll() {
        return registry.values();
    }
}

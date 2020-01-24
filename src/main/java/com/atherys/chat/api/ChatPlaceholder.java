package com.atherys.chat.api;

import org.spongepowered.api.CatalogType;
import org.spongepowered.api.util.annotation.CatalogedBy;

import java.util.function.Function;

@CatalogedBy(ChatPlaceholders.class)
public class ChatPlaceholder implements CatalogType {

    private String id;

    private String name;

    private Function<ChatContext, Object> supplier;

    private ChatPlaceholder(String key, Function<ChatContext, Object> supplier) {
        this.id = key;
        this.name = key;
        this.supplier = supplier;
    }

    public static ChatPlaceholder of(String key, Function<ChatContext, Object> supplier) {
        return new ChatPlaceholder(key, supplier);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Function<ChatContext, Object> getSupplier() {
        return supplier;
    }
}

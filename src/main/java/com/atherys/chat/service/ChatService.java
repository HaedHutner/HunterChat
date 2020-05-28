package com.atherys.chat.service;

import com.atherys.chat.model.AtherysChannel;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.util.Map;
import java.util.Optional;

public interface ChatService {

    public void init();

    public void registerChannel(AtherysChannel channel);

    public void setDefaultChannels(Player player);

    public Map<String, AtherysChannel> getChannels();

    public Optional<AtherysChannel> getChannelById(String id);

    public AtherysChannel getPlayerChannel(Player player);

    public void addPlayerToChannel(Player player, AtherysChannel channel);

    public void removePlayerFromChannel(Player player, AtherysChannel channel);

    public AtherysChannel getPlayerSpeakingChannel(Player player);

    public void setPlayerSpeakingChannel(Player player, AtherysChannel channel);

}

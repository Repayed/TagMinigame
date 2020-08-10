package me.repayed.tagminigame.listeners;

import me.repayed.tagminigame.player.TagPlayerManager;
import me.repayed.tagminigame.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerLeaveListener implements Listener {

    private final TagPlayerManager tagPlayerManager;

    public PlayerLeaveListener(final TagPlayerManager tagPlayerManager) {
        this.tagPlayerManager = tagPlayerManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        final UUID playerUuid = event.getPlayer().getUniqueId();

        if(tagPlayerManager.containsPlayer(playerUuid)) {
            tagPlayerManager.removePlayer(playerUuid);
        }

        Bukkit.broadcastMessage(Chat.format("&6" + event.getPlayer().getDisplayName() + " &ehas left the game."));
    }

}

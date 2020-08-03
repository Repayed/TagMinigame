package me.repayed.tagminigame.listeners;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.game.GameArena;
import me.repayed.tagminigame.game.GameState;
import me.repayed.tagminigame.player.TagPlayer;
import me.repayed.tagminigame.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private GameArena gameArena = TagMinigame.getInstance().getGameArena();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (gameArena.getGameState() == GameState.WAITING || gameArena.getGameState() == GameState.STARTING) {
            if (!TagMinigame.getInstance().getTagPlayerManager().containsPlayer(new TagPlayer(player.getUniqueId()))) {
                TagMinigame.getInstance().getTagPlayerManager().addPlayer(new TagPlayer(player.getUniqueId()));
            }

            Bukkit.broadcastMessage(Chat.format("&eA new challenger has appeared! &6" + player.getDisplayName() + " has joined."));
            Bukkit.broadcastMessage(Chat.format("&7There are currently" + Bukkit.getOnlinePlayers() + " players."));
        }
    }
}

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

    private TagMinigame tagMinigame;
    private GameArena gameArena;

    public PlayerJoinListener(TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
        this.gameArena = this.tagMinigame.getGameArena();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (gameArena.getGameState() == GameState.WAITING || gameArena.getGameState() == GameState.STARTING) {
            if(!tagMinigame.getTagPlayerManager().containsPlayer(player.getUniqueId())) {
                tagMinigame.getTagPlayerManager().addPlayer(new TagPlayer(player.getUniqueId()));
            }

            Bukkit.broadcastMessage(Chat.format("&eA new challenger has appeared! &6" + player.getDisplayName() + " has joined."));
            Bukkit.broadcastMessage(Chat.format("&7There are currently" + Bukkit.getOnlinePlayers() + " players."));

            // if should start, start countdown.

        }
    }
}

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

    private final TagMinigame tagMinigame;
    private final GameArena gameArena;

    public PlayerJoinListener(final TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
        this.gameArena = this.tagMinigame.getGameArena();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (gameArena.getGameState() == GameState.WAITING || gameArena.getGameState() == GameState.STARTING) {
            tagMinigame.getTagPlayerManager().addPlayer(new TagPlayer(player.getUniqueId()));

            player.teleport(this.gameArena.getLobbyLocation());

            Bukkit.broadcastMessage(Chat.format("&eA new challenger has appeared! &6" + player.getDisplayName() + " has joined."));
            Bukkit.broadcastMessage(Chat.format("&7There are currently" + Bukkit.getOnlinePlayers() + " players."));

            if (this.gameArena.getGameState() == GameState.WAITING) {
                if (this.gameArena.shouldCountdownStart()) {
                    this.gameArena.startGameCountdown();
                }

                this.gameArena.startGame();

            }

        }
    }
}

// self note -> So umm, a good idea might be to move some of this game logic like if this arena.get game state whatever to the GameArena class
// to keep this class less clustered.

// possible issue with the startGameCountdown(); method and anything after since async (literally know nothing about it so plz future self learn about it.
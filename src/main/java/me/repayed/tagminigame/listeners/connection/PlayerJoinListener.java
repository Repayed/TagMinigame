package me.repayed.tagminigame.listeners.connection;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.game.GameArena;
import me.repayed.tagminigame.game.GameState;
import me.repayed.tagminigame.player.TagPlayer;
import me.repayed.tagminigame.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);

        if (gameArena.getGameState() == GameState.WAITING || gameArena.getGameState() == GameState.STARTING) {
            tagMinigame.getTagPlayerManager().addPlayer(new TagPlayer(player.getUniqueId()));

            player.teleport(this.gameArena.getLobbyLocation());

            Bukkit.broadcastMessage(Chat.format("&a&lGame &8┃ &f" + player.getDisplayName() + " &7has joined the game."));

            if (this.gameArena.getGameState() == GameState.WAITING) {
                if (this.gameArena.shouldCountdownStart()) {
                    this.gameArena.startGameCountdown();
                }

            }

        }
    }
}
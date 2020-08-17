package me.repayed.tagminigame.listeners.connection;

import me.repayed.tagminigame.game.GameArena;
import me.repayed.tagminigame.game.GameState;
import me.repayed.tagminigame.player.TagPlayer;
import me.repayed.tagminigame.player.TagPlayerManager;
import me.repayed.tagminigame.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    private final GameArena gameArena;
    private final TagPlayerManager tagPlayerManager;

    public PlayerLeaveListener(final GameArena gameArena, final TagPlayerManager tagPlayerManager) {
        this.gameArena = gameArena;
        this.tagPlayerManager = tagPlayerManager;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);

        this.tagPlayerManager.removePlayer(event.getPlayer().getUniqueId());

        Bukkit.broadcastMessage(Chat.format("&c&lGame &8┃ &f" + event.getPlayer().getDisplayName() + " &7has left the game."));

        if (this.gameArena.getGameState() == GameState.INGAME) {
            if (!(this.tagPlayerManager.getTagPlayers().stream()
                    .filter(TagPlayer::isPlaying).count() >= this.gameArena.getMinimumStartingPlayerCount())) {
                this.gameArena.endGame();
                this.gameArena.restartGame(false);
            }
        }
    }

}

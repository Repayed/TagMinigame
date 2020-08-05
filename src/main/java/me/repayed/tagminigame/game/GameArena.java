package me.repayed.tagminigame.game;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.files.ConfigFile;
import me.repayed.tagminigame.player.TagPlayer;
import me.repayed.tagminigame.player.TagPlayerManager;
import me.repayed.tagminigame.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameArena {
    private TagMinigame tagMinigameInstance;

    private ConfigFile configFile;

    private String arenaName;
    private GameState gameState;

    private TagPlayerManager tagPlayerManager;
    private final int MINIMUM_STARTING_PLAYER_COUNT;

    private Location lobbyLocation;
    private Location gameLocation;

    public GameArena(TagMinigame tagMinigame) {
        this.tagMinigameInstance = tagMinigame;
        this.configFile = tagMinigame.getConfigFile();

        this.arenaName = this.configFile.getArenaName();
        this.lobbyLocation = this.configFile.getLobbylocation();
        this.gameLocation = this.configFile.getGameLocation();

        MINIMUM_STARTING_PLAYER_COUNT = this.configFile.getNeededPlayers();

        this.tagPlayerManager = tagMinigame.getTagPlayerManager();

        this.gameState = GameState.WAITING;
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getMinimumStartingPlayerCount() {
        return this.MINIMUM_STARTING_PLAYER_COUNT;
    }

    public Location getLobbyLocation() {
        return this.lobbyLocation;
    }

    public Location getGameLocation() {
        return this.gameLocation;
    }

    public boolean shouldCountdownStart() {
        return Bukkit.getOnlinePlayers().size() >= MINIMUM_STARTING_PLAYER_COUNT && getGameState() == GameState.WAITING;
    }

    public void startGameCountdown() {
        if (shouldCountdownStart()) {
            setGameState(GameState.STARTING);

            new BukkitRunnable() {
                int count = 60;

                @Override
                public void run() {
                    if (count == 0) {
                        if (Bukkit.getOnlinePlayers().size() >= MINIMUM_STARTING_PLAYER_COUNT) {
                            Bukkit.broadcastMessage(Chat.format("&a&lTHE COUNTDOWN HAS ENDED! PREPARE FOR BATTLE!"));
                            setGameState(GameState.INGAME);
                            cancel();
                        } else {
                            Bukkit.broadcastMessage(Chat.format("&cA player has left the game. Waiting for more players."));
                            setGameState(GameState.WAITING);
                            cancel();
                        }
                    } else {
                        Bukkit.broadcastMessage(Chat.format("&eThe game will start in " + count + " seconds."));
                        count--;
                    }
                }

            }.runTaskTimer(tagMinigameInstance, 0, 20L);

        }
    }

    public void startGame() {
        if(getGameState() == GameState.INGAME) {
            for(TagPlayer tagPlayer : tagPlayerManager.getTagPlayers()) {
                Player player = Bukkit.getPlayer(tagPlayer.getUuid());
                player.teleport(getGameLocation());
            }
            // choose random tagger
            startGameCountdown();
        }
    }

    public void startExplosionCountdown() {
        if(getGameState() == GameState.INGAME) {

            new BukkitRunnable() {
                int count = 30;

                @Override
                public void run() {
                    if(count == 0) {
                        Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &chas now ended."));
                    } else {
                        if(count == 30) {
                            Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &chas now started."));
                        } else if(count == 15 || count == 10 || count <= 5) {
                            Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &cwill explode in " + count + " seconds"));
                        }
                    }
                }

            }.runTaskTimer(tagMinigameInstance, 0, 20L);
        }
    }

    public void chooseRandomTagger() {

    }

}

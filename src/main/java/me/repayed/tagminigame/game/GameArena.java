package me.repayed.tagminigame.game;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.files.ConfigFile;
import me.repayed.tagminigame.player.TagPlayer;
import me.repayed.tagminigame.player.TagPlayerManager;
import org.bukkit.Location;

public class GameArena {
    private ConfigFile configFile;

    private String arenaName;
    private GameState gameState;

    private TagPlayerManager tagPlayerManager;
    private final int MINIMUM_STARTING_PLAYER_COUNT;

    private Location lobbyLocation;
    private Location gameLocation;

    public GameArena() {
        this.configFile = TagMinigame.getInstance().getConfigFile();

        this.arenaName = this.configFile.getArenaName();
        this.lobbyLocation = this.configFile.getLobbylocation();
        this.gameLocation = this.configFile.getGameLocation();

        MINIMUM_STARTING_PLAYER_COUNT = this.configFile.getNeededPlayers();

        this.tagPlayerManager = TagMinigame.getInstance().getTagPlayerManager();

        this.gameState = GameState.WAITING;
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public GameState getGameState() {
        return this.gameState;
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
}

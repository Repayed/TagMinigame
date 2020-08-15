package me.repayed.tagminigame.files;

import me.repayed.tagminigame.TagMinigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigFile {
    private final TagMinigame tagMinigame;

    private Location lobbyLocation;
    private Location gameLocation;

    public ConfigFile(final TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;

        loadLobbyLocation();
        loadGameLocation();
    }

    private void loadLobbyLocation() {
        final String lobbyWorld = this.tagMinigame.getConfig().getString("options.arena.locations.lobby-location.world");
        final double lobbyXLocation = this.tagMinigame.getConfig().getInt("options.arena.locations.lobby-location.x");
        final double lobbyYLocation = this.tagMinigame.getConfig().getInt("options.arena.locations.lobby-location.y");
        final double lobbyZLocation = this.tagMinigame.getConfig().getInt("options.arena.locations.lobby-location.z");

        this.lobbyLocation = new Location(Bukkit.getWorld(lobbyWorld), lobbyXLocation, lobbyYLocation, lobbyZLocation);
    }

    private void loadGameLocation() {
        final String gameWorld = this.tagMinigame.getConfig().getString("options.arena.locations.game-location.world");
        final double gameXLocation = this.tagMinigame.getConfig().getInt("options.arena.locations.game-location.x");
        final double gameYLocation = this.tagMinigame.getConfig().getInt("options.arena.locations.game-location.y");
        final double gameZLocation = this.tagMinigame.getConfig().getInt("options.arena.locations.game-location.z");

        this.gameLocation = new Location(Bukkit.getWorld(gameWorld), gameXLocation, gameYLocation, gameZLocation);
    }

    public Location getLobbylocation() {
        return this.lobbyLocation;
    }

    public Location getGameLocation() {
        return this.gameLocation;
    }

    public int getNeededPlayers() {
        return this.tagMinigame.getConfig().getInt("options.arena.needed-players");
    }
}

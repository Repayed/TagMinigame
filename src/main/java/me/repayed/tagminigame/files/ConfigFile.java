package me.repayed.tagminigame.files;

import me.repayed.tagminigame.TagMinigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ConfigFile {
    private TagMinigame instance;

    private String arenaName;

    private Location lobbyLocation;
    private Location gameLocation;

    public ConfigFile() {
        this.instance = TagMinigame.getInstance();

        this.arenaName = this.instance.getConfig().getString("options.arena.name");
        loadLobbyLocation();
        loadGameLocation();
    }

    private void loadLobbyLocation() {
        final String lobbyWorld = this.instance.getConfig().getString("options.arena.locations.lobby-location.world");
        final double lobbyXLocation = this.instance.getConfig().getInt("options.arena.locations.lobby-location.x");
        final double lobbyYLocation = this.instance.getConfig().getInt("options.arena.locations.lobby-location.y");
        final double lobbyZLocation = this.instance.getConfig().getInt("options.arena.locations.lobby-location.z");

        this.lobbyLocation = new Location(Bukkit.getWorld(lobbyWorld), lobbyXLocation, lobbyYLocation, lobbyZLocation);
    }

    private void loadGameLocation() {
        final String gameWorld = this.instance.getConfig().getString("options.arena.locations.game-location.world");
        final double gameXLocation = this.instance.getConfig().getInt("options.arena.locations.game-location.x");
        final double gameYLocation = this.instance.getConfig().getInt("options.arena.locations.game-location.y");
        final double gameZLocation = this.instance.getConfig().getInt("options.arena.locations.game-location.z");

        this.gameLocation = new Location(Bukkit.getWorld(gameWorld), gameXLocation, gameYLocation, gameZLocation);
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public Location getLobbylocation() {
        return this.lobbyLocation;
    }

    public Location getGameLocation() {
        return this.gameLocation;
    }

    public int getNeededPlayers() {
        return this.instance.getConfig().getInt("options.arena.needed-players");
    }
}

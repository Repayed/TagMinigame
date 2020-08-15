package me.repayed.tagminigame;

import me.repayed.tagminigame.files.ConfigFile;
import me.repayed.tagminigame.game.GameArena;
import me.repayed.tagminigame.listeners.*;
import me.repayed.tagminigame.listeners.interaction.PlayerBreakBlockListener;
import me.repayed.tagminigame.listeners.interaction.PlayerHitPlayerListener;
import me.repayed.tagminigame.listeners.interaction.PlayerInteractListener;
import me.repayed.tagminigame.listeners.interaction.PlayerPlaceBlockListener;
import me.repayed.tagminigame.listeners.connection.PlayerJoinListener;
import me.repayed.tagminigame.listeners.connection.PlayerLeaveListener;
import me.repayed.tagminigame.player.TagPlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TagMinigame extends JavaPlugin {
    private ConfigFile configFile;

    private GameArena gameArena;
    private TagPlayerManager tagPlayerManager;

    @Override
    public void onEnable() {
        loadConfig();
        this.configFile = new ConfigFile(this);

        this.tagPlayerManager = new TagPlayerManager();
        this.gameArena = new GameArena(this);

        loadListeners();
    }

    public GameArena getGameArena() {
        return this.gameArena;
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public ConfigFile getConfigFile() {
        return this.configFile;
    }

    public TagPlayerManager getTagPlayerManager() {
        return this.tagPlayerManager;
    }

    private void loadListeners() {
        Arrays.asList(
                new PlayerJoinListener(this), new PlayerHitPlayerListener(this),
                new PlayerLeaveListener(this.getGameArena(), this.getTagPlayerManager()), new PlayerBreakBlockListener(this),
                new PlayerInteractListener(this), new PlayerPlaceBlockListener(this),
                new PlayerFoodLevelListener(), new PlayerInventoryListeners())
                .forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }


}

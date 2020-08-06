package me.repayed.tagminigame;

import me.repayed.tagminigame.files.ConfigFile;
import me.repayed.tagminigame.game.GameArena;
import me.repayed.tagminigame.listeners.*;
import me.repayed.tagminigame.player.TagPlayerManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TagMinigame extends JavaPlugin {
    private ConfigFile configFile;

    private GameArena gameArena;
    private TagPlayerManager tagPlayerManager;

    @Override
    public void onEnable() {
        loadConfig();
        this.configFile = new ConfigFile(this);

        gameArena = new GameArena(this);
        this.tagPlayerManager = new TagPlayerManager();

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

    private void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void loadListeners() {
        registerListener(new PlayerJoinListener(this));
        registerListener(new PlayerHitPlayerListener(this));
        registerListener(new PlayerLeaveListener(this.tagPlayerManager));
        registerListener(new PlayerBreakBlockListener(this));
        registerListener(new PlayerInteractListener(this));
        registerListener(new PlayerPlaceBlockListener(this));
    }

}

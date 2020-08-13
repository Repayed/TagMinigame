package me.repayed.tagminigame.game;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.files.ConfigFile;
import me.repayed.tagminigame.player.TagPlayer;
import me.repayed.tagminigame.player.TagPlayerManager;
import me.repayed.tagminigame.utils.Chat;
import me.repayed.tagminigame.utils.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class GameArena {
    private final TagMinigame tagMinigame;

    private final ConfigFile configFile;

    private GameState gameState;

    private final TagPlayerManager tagPlayerManager;
    private final int MINIMUM_STARTING_PLAYER_COUNT;

    private Location lobbyLocation;
    private Location gameLocation;

    public GameArena(final TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
        this.configFile = this.tagMinigame.getConfigFile();

        this.lobbyLocation = this.configFile.getLobbylocation();
        this.gameLocation = this.configFile.getGameLocation();

        this.MINIMUM_STARTING_PLAYER_COUNT = this.configFile.getNeededPlayers();

        this.tagPlayerManager = this.tagMinigame.getTagPlayerManager();

        this.gameState = GameState.WAITING;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getMinimumStartingPlayerCount() {
        return this.MINIMUM_STARTING_PLAYER_COUNT;
    }

    public Location getLobbyLocation() {
        return this.lobbyLocation;
    }

    private Location getGameLocation() {
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
                            cancel();
                            setGameState(GameState.INGAME);
                            startGame();
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

            }.runTaskTimer(this.tagMinigame, 0, 20L);

        }
    }

    private void startGame() {
        if (getGameState() == GameState.INGAME) {
            this.tagPlayerManager.getTagPlayers().forEach(tagPlayer -> {
                tagPlayer.setPlaying(true);

                Player realPlayer = Bukkit.getPlayer(tagPlayer.getUuid());
                realPlayer.setHealth(realPlayer.getMaxHealth());
                realPlayer.setGameMode(GameMode.SURVIVAL);
                realPlayer.getInventory().clear();

                realPlayer.teleport(getGameLocation());
            });

            setPlayerAsTagger(chooseRandomTagger());
            startExplosionCountdown();
        }
    }

    private boolean shouldEnd() {
        long amount = this.tagPlayerManager.getTagPlayers().stream().filter(TagPlayer::isPlaying).count();
        return amount <= 1;
    }

    private void startExplosionCountdown() {
        if (getGameState() == GameState.INGAME) {

            new BukkitRunnable() {
                int count = 30;

                @Override
                public void run() {
                    count--;

                    if (count == 0) {
                        Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &chas now ended."));
                        cancel();

                        if(getGameState() == GameState.INGAME) {
                            startExplosionCountdown();
                        }

                    } else {
                        if (count == 29) {
                            Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &chas now started."));
                        } else if (count == 15 || count == 10 || count <= 5) {
                            Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &cwill explode in " + count + " seconds"));
                        }
                    }
                }

            }.runTaskTimer(this.tagMinigame, 0, 20L);
        }
    }

    public void setPlayerAsTagger(UUID uuid) {
        if(uuid == null) {
            Bukkit.broadcastMessage("test is null");
        }

        Bukkit.broadcastMessage(Bukkit.getPlayer(uuid).getDisplayName());
        TagPlayer tagPlayer = this.tagPlayerManager.getTagPlayerByUUID(uuid);
        tagPlayer.setTagger(true);

        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage(Chat.format("&cYou have been made the tagger. &4&lYou're now IT!"));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);

        player.getInventory().setHelmet(new ItemBuilder(Material.TNT).withName("&c&lYou're it!").withHiddenEnchantment().build());
        player.getInventory().setItemInHand(new ItemBuilder(Material.TNT).withName("&c&lTAG SOMEONE ELSE!").withHiddenEnchantment().build());
    }

    public void removeTagger(UUID uuid) {
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setTagger(false);

        Player player = Bukkit.getPlayer(uuid);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
    }

    public void removePlayer(UUID uuid) {
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setPlaying(false);
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setTagger(false);

        Player player = Bukkit.getPlayer(uuid);

        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
    }

    private UUID chooseRandomTagger() {
        int taggerIndex = new Random().nextInt(this.tagPlayerManager.getTagPlayers().size());

        Iterator<TagPlayer> iterator = this.tagPlayerManager.getTagPlayers().iterator();
        for (int i = 0; i < taggerIndex - 1; i++) {
            if (iterator.next().isPlaying()) {
                Bukkit.broadcastMessage("Chose " + Bukkit.getPlayer(iterator.next().getUuid()).getDisplayName());
                return iterator.next().getUuid();
            }
        }
        return iterator.next().getUuid();
    }

}

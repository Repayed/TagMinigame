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

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class GameArena {
    private final TagMinigame tagMinigame;

    private final ConfigFile configFile;

    private final String arenaName;
    private GameState gameState;

    private final TagPlayerManager tagPlayerManager;
    private final int MINIMUM_STARTING_PLAYER_COUNT;

    private Location lobbyLocation;
    private Location gameLocation;

    public GameArena(final TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
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

    private void setGameState(GameState gameState) {
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

            }.runTaskTimer(this.tagMinigame, 0, 20L);

        }
    }

    public void startGame() {
        if (getGameState() == GameState.INGAME) {
            for (TagPlayer tagPlayer : tagPlayerManager.getTagPlayers()) {
                Player player = Bukkit.getPlayer(tagPlayer.getUuid());
                player.teleport(getGameLocation());
            }
            // choose random tagger
            startExplosionCountdown();
        }
    }

    public void startExplosionCountdown() {
        if (getGameState() == GameState.INGAME) {

            new BukkitRunnable() {
                int count = 30;

                @Override
                public void run() {
                    if (count == 0) {
                        Bukkit.broadcastMessage(Chat.format("&4&lEXPLOSION COUNTDOWN &chas now ended."));
                    } else {
                        if (count == 30) {
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
        TagPlayer tagPlayer = this.tagPlayerManager.getTagPlayerByUUID(uuid);
        tagPlayer.setTagger(true);
        Player player = Bukkit.getPlayer(tagPlayer.getUuid());


    }

    public void removeTagger(UUID uuid) {
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setTagger(false);
    }

    public void removeTagger(TagPlayer tagPlayer) {
        tagPlayer.setTagger(false);
    }

    public boolean isTagger(TagPlayer tagPlayer) {
        return tagPlayer.isPlaying() && tagPlayer.isTagger();
    }

    public void chooseRandomTagger() {
        int taggerIndex = new Random().nextInt(this.tagPlayerManager.getTagPlayers().size());

        Iterator<TagPlayer> iterator = this.tagPlayerManager.getTagPlayers().iterator();
        for (int i = 0; i < taggerIndex; i++) {
            if (iterator.next().isPlaying()) {
                iterator.next().setTagger(true);
            }
        }
    }

}

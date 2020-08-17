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

public final class GameArena {
    private final TagMinigame tagMinigame;

    private GameState gameState;

    private final TagPlayerManager tagPlayerManager;
    private final int MINIMUM_STARTING_PLAYER_COUNT;

    private Location lobbyLocation;
    private Location gameLocation;

    public GameArena(final TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
        final ConfigFile configFile = this.tagMinigame.getConfigFile();

        this.lobbyLocation = configFile.getLobbylocation();
        this.gameLocation = configFile.getGameLocation();

        this.MINIMUM_STARTING_PLAYER_COUNT = configFile.getNeededPlayers();

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
        return Bukkit.getOnlinePlayers().size() >= getMinimumStartingPlayerCount() && getGameState() == GameState.WAITING;
    }

    private boolean shouldGameRestart() {
        return Bukkit.getOnlinePlayers().size() >= getMinimumStartingPlayerCount();
    }

    public void startGameCountdown() {
        if (shouldCountdownStart()) {
            setGameState(GameState.STARTING);

            new BukkitRunnable() {
                int count = 60;

                @Override
                public void run() {
                    if (count == 0) {
                        if (Bukkit.getOnlinePlayers().size() >= getMinimumStartingPlayerCount()) {
                            Bukkit.broadcastMessage(Chat.format("&c&lGame &8┃ &7The game has &fstarted&7!"));
                            cancel();
                            setGameState(GameState.INGAME);
                            startGame();
                        } else {
                            Bukkit.broadcastMessage(Chat.format("&4&lGame &8┃ &7A player has left the game. Waiting for more players."));
                            setGameState(GameState.WAITING);
                            cancel();
                        }
                    } else {
                        if (count == 60 || count == 45 || count == 30 || count == 15 || count == 10 || count <= 5) {
                            Bukkit.broadcastMessage(Chat.format("&a&lGame &8┃ &7The game will start in &f" + count + "&7 seconds."));
                            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F));
                        }
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

    public final void endGame() {
        setGameState(GameState.ENDED);
    }

    public final void restartGame(boolean startGameCountdown) {
        if (getGameState() == GameState.ENDED) {
            Bukkit.broadcastMessage(Chat.format("&eRestarting the game..."));

            this.tagPlayerManager.getTagPlayers().forEach(tagPlayer -> {
                tagPlayer.setPlaying(false);

                if (Bukkit.getPlayer(tagPlayer.getUuid()).isOnline()) {
                    Player player = Bukkit.getPlayer(tagPlayer.getUuid());
                    player.teleport(getLobbyLocation());
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(20D);
                    player.getInventory().clear();
                    player.getInventory().setHelmet(null);

                    if (tagPlayer.isTagger()) {
                        removeTagger(player.getUniqueId());
                    }

                } else {
                    this.tagPlayerManager.removePlayer(tagPlayer.getUuid());
                }

            });

            setGameState(GameState.WAITING);

            if (startGameCountdown) {
                startGameCountdown();
            }
        }
    }

    private void startExplosionCountdown() {
        if (getGameState() == GameState.INGAME) {

            new BukkitRunnable() {
                int count = 30;

                @Override
                public void run() {
                    count--;

                    if (getGameState() != GameState.INGAME) cancel();

                    if (count == 0) {
                        Bukkit.broadcastMessage(Chat.format("&c&lGame &8┃ &7Countdown has &fended&7."));
                        eliminatePlayer(getCurrentlyTaggedPlayer().getUuid());
                        cancel();

                        if (getGameState() == GameState.INGAME) {
                            if (!shouldEnd()) {
                                startExplosionCountdown();
                                setPlayerAsTagger(chooseRandomTagger());
                            } else {
                                endGame();
                                broadcastWinner();

                                if (shouldGameRestart()) {
                                    restartGame(true);
                                }
                            }

                        }

                    } else {
                        if (count == 29) {
                            Bukkit.broadcastMessage(Chat.format("&c&lGame &8┃ &7Countdown has &fstarted&7."));
                        } else if (count == 15 || count == 10 || count <= 5) {
                            Bukkit.broadcastMessage(Chat.format("&c&lGame &8┃ &7TnT will explode in &f" + count + "&7 seconds."));
                        }
                    }
                }


            }.runTaskTimer(this.tagMinigame, 0, 20L);
        }

    }


    public final void setPlayerAsTagger(UUID uuid) {
        Bukkit.broadcastMessage(Bukkit.getPlayer(uuid).getDisplayName());
        TagPlayer tagPlayer = this.tagPlayerManager.getTagPlayerByUUID(uuid);
        tagPlayer.setTagger(true);

        Player player = Bukkit.getPlayer(uuid);
        player.sendMessage(Chat.format("&c&lGame &8┃ &7You have been made the &ctagger&7!"));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);

        player.getInventory().setHelmet(new ItemBuilder(Material.TNT).withName("&c&lYou're it!").withHiddenEnchantment().build());
        player.getInventory().setItemInHand(new ItemBuilder(Material.TNT).withName("&c&lTAG SOMEONE ELSE!").withHiddenEnchantment().build());
    }

    public final void removeTagger(UUID uuid) {
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setTagger(false);

        Player player = Bukkit.getPlayer(uuid);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
    }

    private TagPlayer getCurrentlyTaggedPlayer() {
        return this.tagPlayerManager.getTagPlayers().stream()
                .filter(tagPlayer -> tagPlayer.isPlaying() && tagPlayer.isTagger())
                .findAny()
                .orElse(null);
    }

    private void eliminatePlayer(UUID uuid) {
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setPlaying(false);
        this.tagPlayerManager.getTagPlayerByUUID(uuid).setTagger(false);

        Player player = Bukkit.getPlayer(uuid);

        player.getWorld().createExplosion(player.getLocation(), 0.0F);
        Bukkit.broadcastMessage(Chat.format("&4" + player.getDisplayName() + " &chas been eliminated."));

        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.setGameMode(GameMode.SPECTATOR);
    }

    private UUID chooseRandomTagger() {
        int taggerIndex = new Random().nextInt(this.tagPlayerManager.getTagPlayers().size());

        Iterator<TagPlayer> iterator = this.tagPlayerManager.getTagPlayers().iterator();
        for (int i = 0; i < taggerIndex - 1; i++) {
            if (iterator.next().isPlaying()) {
                Bukkit.broadcastMessage("&c&lGame &8┃ &f" + Bukkit.getPlayer(iterator.next().getUuid()).getDisplayName() + " &7has been chosen as the tagger.");
                return iterator.next().getUuid();
            }
        }
        return iterator.next().getUuid();
    }

    private TagPlayer getWinner() {
        return this.tagPlayerManager.getTagPlayers().stream()
                .filter(TagPlayer::isPlaying)
                .findAny()
                .orElse(null);
    }

    private void broadcastWinner() {
        Player player = Bukkit.getPlayer(getWinner().getUuid());

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(Chat.format("                             &c&lGAME &7[Ended]"));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(Chat.format("         &7The winner of this game is &f" + player.getDisplayName() + "&7!"));
        Bukkit.broadcastMessage("");
    }

}

package me.repayed.tagminigame.player;

import java.util.UUID;

public class TagPlayer {
    private UUID uuid;
    private boolean isInGame;
    private boolean isTagger;

    public TagPlayer(UUID uuid) {
        this.uuid = uuid;
        this.isTagger = false;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isInGame() {
        return this.isInGame;
    }

    public void setInGame(boolean isInGame) {
        this.isInGame = isInGame;
    }

    public boolean isTagger() {
        return this.isTagger;
    }

    public void setTagger(boolean isTagger) {
        this.isTagger = isTagger;
    }

}

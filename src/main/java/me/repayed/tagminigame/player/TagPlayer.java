package me.repayed.tagminigame.player;

import java.util.UUID;

public class TagPlayer {
    private UUID uuid;
    private boolean isPlaying;
    private boolean isTagger;

    public TagPlayer(UUID uuid) {
        this.uuid = uuid;
        this.isPlaying = false;
        this.isTagger = false;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isTagger() {
        return this.isTagger;
    }

    public void setTagger(boolean isTagger) {
        this.isTagger = isTagger;
    }

}

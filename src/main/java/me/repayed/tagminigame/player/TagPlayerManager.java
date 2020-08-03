package me.repayed.tagminigame.player;

import java.util.HashSet;
import java.util.Set;

public class TagPlayerManager {
    private Set<TagPlayer> tagPlayers;

    public TagPlayerManager() {
        this.tagPlayers = new HashSet<>();
    }

    public void addPlayer(TagPlayer tagPlayer) {
        this.tagPlayers.add(tagPlayer);
    }

    public void removePlayer(TagPlayer tagPlayer) {
        this.tagPlayers.remove(tagPlayer);
    }

    public boolean containsPlayer(TagPlayer tagPlayer) {
        return this.tagPlayers.contains(tagPlayer);
    }

    public Set<TagPlayer> getTagPlayers() {
        return this.tagPlayers;
    }

}

package me.repayed.tagminigame.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    public void removePlayer(UUID uuid) {
        this.tagPlayers.stream()
                .filter(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString()))
                .forEach(filteredPlayer -> removePlayer(filteredPlayer)); // figure out wtf a method reference lambda thing is
    }

    public boolean containsPlayer(TagPlayer tagPlayer) {
        return this.tagPlayers.stream()
                .anyMatch(player -> player.getUuid().toString().equalsIgnoreCase(tagPlayer.getUuid().toString()));
    }

    public boolean containsPlayer(UUID uuid) {
        return this.tagPlayers.stream()
                .anyMatch(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString()));
    }

    public TagPlayer getTagPlayerByUUID(UUID uuid) {
        return this.tagPlayers.stream()
                .filter(tagPlayer -> tagPlayer.getUuid().toString().equalsIgnoreCase(uuid.toString()))
                .findAny()
                .orElse(null);

    }

    public Set<TagPlayer> getTagPlayers() {
        return this.tagPlayers;
    }

}

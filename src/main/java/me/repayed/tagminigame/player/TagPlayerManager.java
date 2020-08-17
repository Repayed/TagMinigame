package me.repayed.tagminigame.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class TagPlayerManager {
    private final Set<TagPlayer> tagPlayers;

    public TagPlayerManager() {
        this.tagPlayers = new HashSet<>();
    }

    public final void addPlayer(TagPlayer tagPlayer) {
        this.tagPlayers.add(tagPlayer);
    }

    public final void removePlayer(UUID uuid) {
        if (containsPlayer(uuid)) {
            this.tagPlayers.removeIf(player -> player.getUuid().equals(uuid));
        }
    }

    private boolean containsPlayer(UUID uuid) {
        return this.tagPlayers.stream()
                .anyMatch(player -> player.getUuid().toString().equalsIgnoreCase(uuid.toString()));
    }

    public final TagPlayer getTagPlayerByUUID(UUID uuid) {
        return this.tagPlayers.stream()
                .filter(tagPlayer -> tagPlayer.getUuid().toString().equalsIgnoreCase(uuid.toString()))
                .findAny()
                .orElse(null);

    }

    public final Set<TagPlayer> getTagPlayers() {
        return this.tagPlayers;
    }

}

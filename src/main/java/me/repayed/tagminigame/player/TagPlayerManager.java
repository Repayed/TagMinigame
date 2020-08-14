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

//
//    @Deprecated
//    public void removePlayer(UUID uuid) {
//        if (containsPlayer(uuid)) {
//            this.tagPlayers.stream()
//                    .filter(player -> player.getUuid() == uuid)
//                    .forEach(this::removePlayer);
//        }
//    }

    public void removePlayer(UUID uuid) {
        if (containsPlayer(uuid)) {
            this.tagPlayers.removeIf(player -> player.getUuid().equals(uuid));
        }
    }

    private boolean containsPlayer(UUID uuid) {
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

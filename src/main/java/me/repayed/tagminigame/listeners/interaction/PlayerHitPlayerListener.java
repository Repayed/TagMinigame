package me.repayed.tagminigame.listeners.interaction;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.UUID;

public class PlayerHitPlayerListener implements Listener {

    private final TagMinigame tagMinigame;

    public PlayerHitPlayerListener(final TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setDamage(0.00);

            if (this.tagMinigame.getGameArena().getGameState() == GameState.INGAME) {
                UUID hitPlayerUUID = event.getEntity().getUniqueId();
                UUID hitterUUID = event.getDamager().getUniqueId();

                if (this.tagMinigame.getTagPlayerManager().getTagPlayerByUUID(hitterUUID).isTagger()) {
                    this.tagMinigame.getGameArena().removeTagger(hitterUUID);
                    this.tagMinigame.getGameArena().setPlayerAsTagger(hitPlayerUUID);
                }

            } else {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }


    }
}

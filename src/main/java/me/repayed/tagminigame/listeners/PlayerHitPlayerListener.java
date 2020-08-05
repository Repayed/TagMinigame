package me.repayed.tagminigame.listeners;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerHitPlayerListener implements Listener {

    private TagMinigame tagMinigame;

    public PlayerHitPlayerListener(TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            event.setDamage(0.00);

            if (this.tagMinigame.getGameArena().getGameState() == GameState.INGAME) {
                Player hitPlayer = (Player) event.getEntity();
                Player hitter = (Player) event.getDamager();

                if(this.tagMinigame.getTagPlayerManager().getTagPlayerByUUID(hitter.getUniqueId()).isTagger()) {
                    // make hit player the tagger. remove hitter from being tagger. (perhaps make a method in GameArena for this)
                }

            } else {
                event.setCancelled(true);
            }
        }


    }
}

package me.repayed.tagminigame.listeners;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerPlaceBlockListener implements Listener {

    private TagMinigame tagMinigame;

    public PlayerPlaceBlockListener(TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
    }

    @EventHandler
    public void playerPlaceBlock(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
        } else {
            if(this.tagMinigame.getGameArena().getGameState() == GameState.INGAME) {
                event.setCancelled(true);
            }
        }
    }
}

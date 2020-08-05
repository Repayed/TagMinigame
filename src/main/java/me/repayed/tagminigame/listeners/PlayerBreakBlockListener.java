package me.repayed.tagminigame.listeners;

import me.repayed.tagminigame.TagMinigame;
import me.repayed.tagminigame.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakBlockListener implements Listener {

    private TagMinigame tagMinigame;

    public PlayerBreakBlockListener(TagMinigame tagMinigame) {
        this.tagMinigame = tagMinigame;
    }

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        if(!event.getPlayer().isOp()) {
            event.setCancelled(true);
        } else {
            if(this.tagMinigame.getGameArena().getGameState() == GameState.INGAME) {
                event.setCancelled(true);
            }
        }
    }
}

package me.danjono.inventoryrollback.listeners;

import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.saving.SaveInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("inventoryrollback.saves.join")) return;
        Player player = event.getPlayer();

        new SaveInventory(event.getPlayer(), LogType.JOIN, null, player.getInventory(), player.getEnderChest()).createSave();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!event.getPlayer().hasPermission("inventoryrollback.saves.quit")) return;
        Player player = event.getPlayer();

        new SaveInventory(event.getPlayer(), LogType.QUIT, null, player.getInventory(), player.getEnderChest()).createSave();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!player.hasPermission("inventoryrollback.saves.death")) return;
        if (player.getHealth() == 0 || player.getHealth() > event.getFinalDamage() || event.getEntity().isDead()) return;

        new SaveInventory(player, LogType.DEATH, event.getDamageSource(), player.getInventory(), player.getEnderChest()).createSave();
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        if (!event.getPlayer().hasPermission("inventoryrollback.saves.world-change")) return;
        Player player = event.getPlayer();

        new SaveInventory(event.getPlayer(), LogType.WORLD_CHANGE, null, player.getInventory(), player.getEnderChest()).createSave();
    }
}

package me.danjono.inventoryrollback.listeners;

import me.danjono.inventoryrollback.gui.BackupMenu;
import me.danjono.inventoryrollback.gui.InventoryType;
import me.danjono.inventoryrollback.gui.MainMenu;
import me.danjono.inventoryrollback.gui.RollbackListMenu;
import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.items.ButtonType;
import me.danjono.inventoryrollback.items.Buttons;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.model.PlayerData;
import me.danjono.inventoryrollback.saving.RestoreInventory;
import me.danjono.inventoryrollback.utils.PersistentData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;

import java.time.Instant;
import java.util.UUID;

public class InventoryClickListener extends Buttons implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Component title = event.getView().title();
        InventoryType type = InventoryType.getType(title);

        if (type != null && event.getInventory().getLocation() == null) {
            for (Integer slot : event.getRawSlots()) {
                if (slot < event.getInventory().getSize()) continue;
                return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Component title = event.getView().title();
        InventoryType inventoryType = InventoryType.getType(title);

        if (inventoryType != null && event.getInventory().getLocation() == null && event.getWhoClicked() instanceof Player player) {
            ItemStack item = event.getCurrentItem();
            event.setCancelled(true);

            if (item != null) {
                switch (inventoryType) {
                    case MAIN_MENU -> {
                        if (event.getRawSlot() > -1 && event.getRawSlot() < 9) {
                            PersistentData data = new PersistentData(item);
                            if (data.hasUniqueId()) {
                                LogType type = data.getLogType();
                                player.openInventory(new RollbackListMenu(data.getUniqueId(), type, 1).showBackups());
                            }
                        } else if (event.getRawSlot() >= event.getInventory().getSize() && !event.isShiftClick()) {
                            event.setCancelled(false);
                        }
                    }
                    case ROLLBACK_LIST -> {
                        if (event.getRawSlot() > -1 && event.getRawSlot() < 45) {
                            PersistentData persistent = new PersistentData(item);
                            if (!persistent.hasUniqueId()) return;

                            Material material = item.getType();
                            if (material.equals(Material.CHEST)) {
                                UUID uuid = persistent.getUniqueId();
                                Instant timestamp = persistent.getTimestamp();
                                if (timestamp == null) return;
                                LogType type = persistent.getLogType();
                                Location location = persistent.getLocation();

                                ConfigurationNode node = PlayerData.loadNode(uuid, type, timestamp);
                                if (node == null) return;
                                RestoreInventory restore = new RestoreInventory(timestamp, node);

                                ItemStack[] enderChest = restore.enderChest();
                                ItemStack[] inventory = restore.inventory();

                                float saturation = restore.saturation();
                                int experience = restore.expPoints();
                                double health = restore.health();
                                int hunger = restore.hunger();

                                player.openInventory(new BackupMenu(player, uuid, type, timestamp, inventory, enderChest, location, health, hunger, saturation, experience).showItems());
                            } else if (material.equals(ButtonType.PAGE_SELECTOR.material())) {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(persistent.getUniqueId());
                                int page = persistent.getPage();

                                if (page == 0) {
                                    Inventory inventory = new MainMenu(player, target).getMenu();
                                    if (inventory != null) {
                                        player.openInventory(inventory);
                                    }
                                } else {
                                    LogType type = persistent.getLogType();
                                    player.openInventory(new RollbackListMenu(target.getUniqueId(), type, page).showBackups());
                                }
                            }
                        } else if (event.getRawSlot() >= event.getInventory().getSize() && !event.isShiftClick()) {
                            event.setCancelled(false);
                        }
                    }
                    case BACKUP -> {
                        if (event.getRawSlot() >= 45 && event.getRawSlot() < 54) {
                            PersistentData persistent = new PersistentData(item);
                            if (!persistent.hasUniqueId()) return;

                            OfflinePlayer target = Bukkit.getOfflinePlayer(persistent.getUniqueId());
                            LogType type = persistent.getLogType();

                            Material material = item.getType();
                            if (material.equals(ButtonType.PAGE_SELECTOR.material())) {
                                player.openInventory(new RollbackListMenu(target.getUniqueId(), type, 1).showBackups());
                            } else if (material.equals(ButtonType.TELEPORT.material())) {
                                Location location = persistent.getLocation();
                                player.teleportAsync(location, PlayerTeleportEvent.TeleportCause.COMMAND).thenAccept(result -> {
                                    if (result) {
                                        Message.COMMAND_RESTORE_TELEPORT_SUCCESS.send(player, location);
                                    } else {
                                        Message.COMMAND_RESTORE_TELEPORT_ERRORED.send(player, location);
                                    }
                                });
                            } else if (material.equals(ButtonType.ENDER_CHEST.material())) {
                                Player onlineTarget = target.getPlayer();
                                if (onlineTarget != null) {
                                    Instant timestamp = persistent.getTimestamp();
                                    if (timestamp == null) return;
                                    ConfigurationNode node = PlayerData.loadNode(target, type, timestamp);
                                    if (node == null) return;
                                    RestoreInventory restore = new RestoreInventory(timestamp, node);
                                    ItemStack[] enderChest = restore.enderChest();
                                    if (isInventoryEmpty(onlineTarget.getEnderChest())) {
                                        onlineTarget.getEnderChest().setContents(enderChest);

                                        Message.COMMAND_RESTORE_ENDER_CHEST_SUCCESS_SELF.send(player, target.getName());
                                        Message.COMMAND_RESTORE_ENDER_CHEST_SUCCESS_TARGET.send(onlineTarget, player.getName());
                                    } else {
                                        Message.COMMAND_RESTORE_ENDER_CHEST_ERRORED_NOT_EMPTY.send(player, target.getName());
                                    }
                                } else {
                                    Message.COMMAND_RESTORE_ENDER_CHEST_ERRORED_NOT_ONLINE.send(player, target.getName());
                                }
                            } else if (material.equals(ButtonType.HEALTH.material())) {
                                Player onlineTarget = target.getPlayer();
                                if (onlineTarget != null) {
                                    onlineTarget.setHealth(persistent.getHealth());

                                    Message.COMMAND_RESTORE_HEALTH_SUCCESS_SELF.send(player, target.getName());
                                    Message.COMMAND_RESTORE_HEALTH_SUCCESS_TARGET.send(onlineTarget, player.getName());
                                } else {
                                    Message.COMMAND_RESTORE_HEALTH_ERRORED.send(player, target.getName());
                                }
                            } else if (material.equals(ButtonType.HUNGER.material())) {
                                Player onlineTarget = target.getPlayer();
                                if (onlineTarget != null) {
                                    onlineTarget.setFoodLevel(persistent.getHunger());
                                    onlineTarget.setSaturation(persistent.getSaturation());

                                    Message.COMMAND_RESTORE_HUNGER_SUCCESS_SELF.send(player, target.getName());
                                    Message.COMMAND_RESTORE_HUNGER_SUCCESS_TARGET.send(onlineTarget, player.getName());
                                } else {
                                    Message.COMMAND_RESTORE_HUNGER_ERRORED.send(player, target.getName());
                                }
                            } else if (material.equals(ButtonType.EXPERIENCE.material())) {
                                Player onlineTarget = target.getPlayer();
                                if (onlineTarget != null) {
                                    onlineTarget.setExperienceLevelAndProgress(persistent.getExperience());

                                    Message.COMMAND_RESTORE_EXPERIENCE_SUCCESS_SELF.send(player, target.getName());
                                    Message.COMMAND_RESTORE_EXPERIENCE_SUCCESS_TARGET.send(onlineTarget, player.getName());
                                } else {
                                    Message.COMMAND_RESTORE_EXPERIENCE_ERRORED.send(player, target.getName());
                                }
                            }
                        } else if (((event.getRawSlot() < event.getInventory().getSize() - 9 || event.getRawSlot() >= event.getInventory().getSize()) && !event.isShiftClick()) || (event.getRawSlot() < event.getInventory().getSize() - 9 && event.isShiftClick())) {
                            event.setCancelled(false);
                        }
                    }
                    default ->
                            throw new IllegalStateException(InventoryType.class + " " + inventoryType + "is unknown!");
                }
            }
        }
    }

    private boolean isInventoryEmpty(Inventory inventory) {
        for (ItemStack item : inventory) {
            if (item == null) continue;
            return false;
        }

        return true;
    }
}

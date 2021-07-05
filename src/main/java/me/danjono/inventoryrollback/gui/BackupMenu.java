package me.danjono.inventoryrollback.gui;

import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.items.Buttons;
import me.danjono.inventoryrollback.model.LogType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public record BackupMenu(Player player, UUID uuid, LogType type, long timestamp, ItemStack[] inventory, ItemStack[] enderChest, Location location, double health, int hunger, float saturation, int experience) {

    public Inventory showItems() {
        Inventory inventory = Bukkit.createInventory(null, 54, InventoryType.BACKUP.getName());
        int item = 0, position = 0;

        try {
            for (int i = 0; i < this.inventory.length - 5; i++) {
                if (this.inventory[item] != null) {
                    inventory.setItem(position, this.inventory[item]);
                    position++;
                }

                item++;
            }
        } catch (NullPointerException exception) {
            throw new RuntimeException(exception);
        }

        item = 36;
        position = 44;

        for (int i = 36; i < this.inventory.length; i++) {
            if (this.inventory[item] != null) {
                inventory.setItem(position, this.inventory[item]);
                position--;
            }

            item++;
        }

        inventory.setItem(46, Buttons.getInventoryBackButton(Message.INVENTORY_ICONS_BACK.build(), uuid, type));
        inventory.setItem(48, Buttons.getTeleportButton(uuid, type, timestamp, location));
        inventory.setItem(50, Buttons.getEnderChestButton(uuid, type, timestamp));
        inventory.setItem(51, Buttons.getHealthButton(uuid, type, health));
        inventory.setItem(52, Buttons.getHungerButton(uuid, type, hunger, saturation));
        inventory.setItem(53, Buttons.getExperienceButton(uuid, type, experience));

        return inventory;
    }
}

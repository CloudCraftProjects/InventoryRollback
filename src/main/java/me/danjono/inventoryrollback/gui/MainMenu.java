package me.danjono.inventoryrollback.gui;

import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.items.Buttons;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.model.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public record MainMenu(Player player, OfflinePlayer target) {

    public Inventory getMenu() {
        Inventory mainMenu = Bukkit.createInventory(player, 9, InventoryType.MAIN_MENU.getName());
        UUID uuid = target.getUniqueId();

        boolean joins = PlayerData.hasData(this.target, LogType.JOIN);
        boolean quits = PlayerData.hasData(this.target, LogType.QUIT);
        boolean deaths = PlayerData.hasData(this.target, LogType.DEATH);
        boolean forceSaves = PlayerData.hasData(this.target, LogType.FORCE);
        boolean worldChanges = PlayerData.hasData(this.target, LogType.WORLD_CHANGE);

        if (!joins && !quits && !deaths && !worldChanges && !forceSaves) {
            player.sendMessage(Message.ERRORS_NO_BACKUP.build(target.getName()));
            return null;
        } else {
            mainMenu.setItem(0, Buttons.getPlayerHead(target, Message.INVENTORY_HEAD.build(target.getName())));

            int position = 2;
            if (deaths) {
                mainMenu.setItem(position, LogType.DEATH.getItem(uuid));
                position += 1;
            }

            if (joins) {
                mainMenu.setItem(position, LogType.JOIN.getItem(uuid));
                position += 1;
            }

            if (quits) {
                mainMenu.setItem(position, LogType.QUIT.getItem(uuid));
                position += 1;
            }

            if (worldChanges) {
                mainMenu.setItem(position, LogType.WORLD_CHANGE.getItem(uuid));
                position += 1;
            }

            if (forceSaves) {
                mainMenu.setItem(position, LogType.FORCE.getItem(uuid));
            }

            return mainMenu;
        }
    }
}

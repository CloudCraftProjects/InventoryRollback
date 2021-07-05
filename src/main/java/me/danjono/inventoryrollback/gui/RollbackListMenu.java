package me.danjono.inventoryrollback.gui;

import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.items.Buttons;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.model.PlayerData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RollbackListMenu {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy");

    private final UUID target;
    private final LogType type;
    private int pageNumber;

    private final FileConfiguration data;

    public RollbackListMenu(UUID target, LogType type, int page) {
        this.target = target;
        this.type = type;
        this.pageNumber = page;

        data = new PlayerData(target, type).getData();
    }

    public Inventory showBackups() {
        Inventory backupMenu = Bukkit.createInventory(null, 45, InventoryType.ROLLBACK_LIST.getName());
        ConfigurationSection section = data.getConfigurationSection("data");
        if (section == null) throw new RuntimeException("Could not find configuration section 'data' on " + target + "!");

        List<Long> timestamps = new ArrayList<>();
        for (String time : section.getKeys(false)) {
            timestamps.add(Long.parseLong(time));
        }

        Collections.reverse(timestamps);
        int max = 36, backups = timestamps.size(), pages = (int) Math.ceil(backups / (double) max), position = 0;

        if (pageNumber > pages) {
            pageNumber = pages;
        } else if (pageNumber <= 0) {
            pageNumber = 1;
        }

        for (int i = 0; i < max; i++) {
            try {
                long time = timestamps.get(((pageNumber - 1) * max) + i);
                List<Component> lore = new ArrayList<>();

                String deathReason = data.getString("data." + time + ".deathReason", "none");
                Location location = data.getLocation("data." + time + ".location");
                if (location == null) continue;

                lore.add(Message.COMMAND_RESTORE_SPECIFIC_DEATH_REASON.build(deathReason));
                lore.add(Message.LOCATION_WORLD.build(location.getWorld().getName()));
                lore.add(Message.LOCATION_X.build(location.getX()));
                lore.add(Message.LOCATION_Y.build(location.getY()));
                lore.add(Message.LOCATION_Z.build(location.getZ()));

                Component displayName = Message.COMMAND_RESTORE_SPECIFIC_DEATH_TIME.build(DATE_FORMAT.format(time));
                ItemStack item = Buttons.getInventoryButton(Material.CHEST, target, type, location, time, displayName, lore);

                backupMenu.setItem(position, item);
            } catch (IndexOutOfBoundsException ignored) {
            }

            position++;
        }

        if (pageNumber == 1) {
            ItemStack mainMenu = Buttons.getBackButton(Message.INVENTORY_ICONS_MAIN_MENU.build(), target, type, 0);
            backupMenu.setItem(position + 1, mainMenu);
        }

        if (pageNumber > 1) {
            ItemStack previousPage = Buttons.getBackButton(Message.INVENTORY_ICONS_PAGE_PREVIOUS.build(), target, type, pageNumber - 1, Component.text("Page " + (pageNumber - 1)));
            backupMenu.setItem(position + 1, previousPage);
        }

        if (pageNumber < pages) {
            ItemStack nextPage = Buttons.getNextButton(Message.INVENTORY_ICONS_PAGE_NEXT.build(), target, type, pageNumber + 1, Component.text("Page " + (pageNumber + 1)));
            backupMenu.setItem(position + 7, nextPage);
        }

        return backupMenu;
    }
}

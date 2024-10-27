package me.danjono.inventoryrollback.saving;

import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.model.PlayerData;
import me.danjono.inventoryrollback.utils.ItemByteSerializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;

public record SaveInventory(Player player, LogType type, DamageCause cause, PlayerInventory inventory,
                            Inventory enderChest) {

    public void createSave() {
        PlayerData data = new PlayerData(player, type);
        FileConfiguration config = data.getData();

        long timestamp = System.currentTimeMillis();
        int saves = config.getInt("saves"), maxSaves = type.getMaxSaves();

        if (data.getFile().exists() && saves >= maxSaves) {
            ConfigurationSection section = config.getConfigurationSection("data");

            if (section != null) {
                List<Long> saved = new ArrayList<>();

                for (String key : section.getKeys(false)) {
                    saved.add(Long.parseLong(key));
                }

                saved.sort(Long::compare);

                while (saved.size() >= maxSaves - 1) {
                    config.set("data." + saved.remove(0), null);
                }

                saves = saved.size();
            }
        }

        config.set("data." + timestamp + ".enderchest", ItemByteSerializer.getItemsAsBase64(enderChest.getContents()));
        config.set("data." + timestamp + ".inventory", ItemByteSerializer.getItemsAsBase64(inventory.getContents()));
        config.set("data." + timestamp + ".deathReason", cause == null ? null : cause.name());
        config.set("data." + timestamp + ".experience", player.calculateTotalExperiencePoints());
        config.set("data." + timestamp + ".saturation", player.getSaturation());
        config.set("data." + timestamp + ".location", player.getLocation());
        config.set("data." + timestamp + ".hunger", player.getFoodLevel());
        config.set("data." + timestamp + ".health", player.getHealth());
        config.set("data." + timestamp + ".type", type.name());
        config.set("saves", saves + 1);

        data.saveData(true);
    }
}

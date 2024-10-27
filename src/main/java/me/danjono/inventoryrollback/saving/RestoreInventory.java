package me.danjono.inventoryrollback.saving;

import me.danjono.inventoryrollback.utils.ItemByteSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public record RestoreInventory(FileConfiguration data, long timestamp) {

    public ItemStack[] retrieveMainInventory() {
        return ItemByteSerializer.getItemsFromBase64(data.getString("data." + timestamp + ".inventory"));
    }

    public ItemStack[] retrieveEnderChestInventory() {
        return ItemByteSerializer.getItemsFromBase64(data.getString("data." + timestamp + ".enderchest"));
    }

    public Double getHealth() {
        return data.getDouble("data." + timestamp + ".health");
    }

    public int getHunger() {
        return data.getInt("data." + timestamp + ".hunger");
    }

    public float getSaturation() {
        return (float) data.getDouble("data." + timestamp + ".saturation");
    }

    public int getExperience() {
        return data.getInt("data." + timestamp + ".experience");
    }
}

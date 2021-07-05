package me.danjono.inventoryrollback.saving;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import static me.danjono.inventoryrollback.utils.ByteSerializer.ITEMSTACK_SERIALIZER;

public record RestoreInventory(FileConfiguration data, long timestamp) {

    public ItemStack[] retrieveMainInventory() {
        return ITEMSTACK_SERIALIZER.objectArrayFromBase64(data.getString("data." + timestamp + ".inventory"));
    }

    public ItemStack[] retrieveArmor() {
        return ITEMSTACK_SERIALIZER.objectArrayFromBase64(data.getString("data." + timestamp + ".armor"));
    }

    public ItemStack[] retrieveEnderChestInventory() {
        return ITEMSTACK_SERIALIZER.objectArrayFromBase64(data.getString("data." + timestamp + ".enderchest"));
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
        return data.getInt("data." + timestamp + ".xp");
    }
}

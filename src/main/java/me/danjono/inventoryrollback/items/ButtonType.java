package me.danjono.inventoryrollback.items;
// Created by booky10 in InventoryRollback (11:30 05.07.21)

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public record ButtonType(Material material) {

    public static final ButtonType PAGE_SELECTOR = new ButtonType(Material.WHITE_BANNER);
    public static final ButtonType TELEPORT = new ButtonType(Material.ENDER_PEARL);
    public static final ButtonType ENDER_CHEST = new ButtonType(Material.ENDER_CHEST);
    public static final ButtonType HEALTH = new ButtonType(Material.MELON_SLICE);
    public static final ButtonType HUNGER = new ButtonType(Material.ROTTEN_FLESH);
    public static final ButtonType EXPERIENCE = new ButtonType(Material.EXPERIENCE_BOTTLE);

    public ItemStack asItem() {
        return new ItemStack(material());
    }
}

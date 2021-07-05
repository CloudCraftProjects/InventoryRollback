package me.danjono.inventoryrollback.model;

import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.items.Buttons;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.UUID;

public enum LogType {

    DEATH(50, "death", Material.BONE, Message.INVENTORY_ICONS_DEATH.build()),
    QUIT(10, "quit", Material.RED_BED, Message.INVENTORY_ICONS_QUIT.build()),
    JOIN(10, "join", Material.OAK_SAPLING, Message.INVENTORY_ICONS_JOIN.build()),
    FORCE(50, "force", Material.DIAMOND, Message.INVENTORY_ICONS_FORCE_SAVE.build()),
    WORLD_CHANGE(10, "world-change", Material.COMPASS, Message.INVENTORY_ICONS_WORLD_CHANGE.build());

    public static final LogType[] VALUES = values();

    private final int maxSaves;
    private final String folder;
    private final Material material;
    private final Component component;

    LogType(int maxSaves, String folder, Material material, Component component) {
        this.maxSaves = maxSaves;
        this.folder = folder;
        this.material = material;
        this.component = component;
    }

    public int getMaxSaves() {
        return maxSaves;
    }

    public String getFolderName() {
        return folder;
    }

    public Material getMaterial() {
        return material;
    }

    public Component getComponent() {
        return component;
    }

    public File getFolder(File parent) {
        return new File(parent, folder);
    }

    public File getFile(File parent, UUID uuid) {
        return new File(getFolder(parent), uuid + ".yml");
    }

    public ItemStack getItem(UUID uuid) {
        return Buttons.getLogTypeButton(uuid, this);
    }
}

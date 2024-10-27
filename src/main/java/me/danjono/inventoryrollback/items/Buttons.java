package me.danjono.inventoryrollback.items;

import com.google.common.collect.ImmutableList;
import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.utils.PersistentData;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Buttons {

    private static final List<Pattern> NEXT_PATTERNS, BACK_PATTERNS;

    static {
        NEXT_PATTERNS = ImmutableList.of(
                new Pattern(DyeColor.BLACK, PatternType.BASE),
                new Pattern(DyeColor.WHITE, PatternType.RHOMBUS),
                new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL),
                new Pattern(DyeColor.GRAY, PatternType.BORDER)
        );

        BACK_PATTERNS = ImmutableList.of(
                new Pattern(DyeColor.BLACK, PatternType.BASE),
                new Pattern(DyeColor.WHITE, PatternType.RHOMBUS),
                new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_RIGHT),
                new Pattern(DyeColor.GRAY, PatternType.BORDER)
        );
    }

    public static ItemStack getNextButton(Component displayName, UUID uuid, LogType type, int page, Component... lore) {
        ItemStack button = ButtonType.PAGE_SELECTOR.asItem();
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setPatterns(NEXT_PATTERNS);
        meta.lore(Arrays.asList(lore));
        meta.displayName(displayName);
        button.setItemMeta(meta);

        return new PersistentData(button).setUniqueId(uuid).setLogType(type).setPage(page).getItem();
    }

    public static ItemStack getBackButton(Component displayName, UUID uuid, LogType type, int page, Component... lore) {
        ItemStack button = ButtonType.PAGE_SELECTOR.asItem();
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setPatterns(BACK_PATTERNS);
        meta.lore(Arrays.asList(lore));
        meta.displayName(displayName);
        button.setItemMeta(meta);

        return new PersistentData(button).setUniqueId(uuid).setLogType(type).setPage(page).getItem();
    }

    public static ItemStack getMainBackButton(Component displayName, UUID uuid) {
        ItemStack button = ButtonType.PAGE_SELECTOR.asItem();
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        meta.setPatterns(BACK_PATTERNS);
        meta.displayName(displayName);
        button.setItemMeta(meta);

        return new PersistentData(button).setUniqueId(uuid).getItem();
    }

    public static ItemStack getInventoryBackButton(Component displayName, UUID uuid, LogType type) {
        ItemStack button = ButtonType.PAGE_SELECTOR.asItem();
        BannerMeta meta = (BannerMeta) button.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        meta.setPatterns(BACK_PATTERNS);
        meta.displayName(displayName);
        button.setItemMeta(meta);

        return new PersistentData(button).setUniqueId(uuid).setLogType(type).getItem();
    }

    public static ItemStack getInventoryButton(Material material, UUID uuid, LogType type, Location location, long timestamp, Component displayName, List<Component> lore) {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        item.setItemMeta(meta);
        item.lore(lore);

        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setTimestamp(timestamp).setLocation(location).getItem();
    }

    public static ItemStack getLogTypeButton(UUID uuid, LogType type, Component... lore) {
        ItemStack item = new ItemStack(type.getMaterial());

        ItemMeta meta = item.getItemMeta();
        meta.displayName(type.getComponent());
        meta.lore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return new PersistentData(item).setUniqueId(uuid).setLogType(type).getItem();
    }

    public static ItemStack getPlayerHead(OfflinePlayer player, Component displayName, Component... lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();

        skullMeta.lore(Arrays.asList(lore));
        skullMeta.displayName(displayName);
        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);

        return skull;
    }

    public static ItemStack getTeleportButton(UUID uuid, LogType type, long timestamp, Location location) {
        ItemStack item = ButtonType.TELEPORT.asItem();

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Message.COMMAND_RESTORE_TELEPORT_DESCRIPTION.build());
        item.setItemMeta(meta);

        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setTimestamp(timestamp).setLocation(location).getItem();
    }

    public static ItemStack getEnderChestButton(UUID uuid, LogType type, long timestamp) {
        ItemStack item = ButtonType.ENDER_CHEST.asItem();

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Message.INVENTORY_ICONS_RESTORE_ENDER_CHEST.build());
        item.setItemMeta(meta);

        PersistentData persistentData = new PersistentData(item);

        persistentData.setUniqueId(uuid);
        persistentData.setLogType(type);
        persistentData.setTimestamp(timestamp);
        item = persistentData.getItem();

        return item;
    }

    public static ItemStack getHealthButton(UUID uuid, LogType type, double health) {
        ItemStack item = ButtonType.HEALTH.asItem();

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Message.INVENTORY_ICONS_RESTORE_HEALTH.build());
        item.setItemMeta(meta);

        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setHealth(health).getItem();
    }

    public static ItemStack getHungerButton(UUID uuid, LogType type, int hunger, float saturation) {
        ItemStack item = ButtonType.HUNGER.asItem();

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Message.INVENTORY_ICONS_RESTORE_HUNGER.build());
        item.setItemMeta(meta);

        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setHunger(hunger).setSaturation(saturation).getItem();
    }

    public static ItemStack getExperienceButton(UUID uuid, LogType logType, int experience) {
        ItemStack item = ButtonType.EXPERIENCE.asItem();

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Message.INVENTORY_ICONS_RESTORE_EXPERIENCE_NAME.build());
        meta.lore(Collections.singletonList(Message.INVENTORY_ICONS_RESTORE_EXPERIENCE_LORE.build(experience)));
        item.setItemMeta(meta);

        return new PersistentData(item).setUniqueId(uuid).setLogType(logType).setExperience(experience).getItem();
    }
}

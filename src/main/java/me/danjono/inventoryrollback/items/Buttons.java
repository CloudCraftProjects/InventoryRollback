package me.danjono.inventoryrollback.items;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
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
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class Buttons {

    private static final List<Pattern> NEXT_PATTERNS = List.of(
            new Pattern(DyeColor.BLACK, PatternType.BASE),
            new Pattern(DyeColor.WHITE, PatternType.RHOMBUS),
            new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL),
            new Pattern(DyeColor.GRAY, PatternType.BORDER)
    );
    private static final List<Pattern> BACK_PATTERNS = List.of(
            new Pattern(DyeColor.BLACK, PatternType.BASE),
            new Pattern(DyeColor.WHITE, PatternType.RHOMBUS),
            new Pattern(DyeColor.BLACK, PatternType.HALF_VERTICAL_RIGHT),
            new Pattern(DyeColor.GRAY, PatternType.BORDER)
    );

    private static ItemStack buildPageSelectorStack(Component displayName, List<Pattern> backPatterns, Component... lore) {
        ItemStack button = ButtonType.PAGE_SELECTOR.asItem();
        button.setData(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers(backPatterns));
        button.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(lore)));
        button.setData(DataComponentTypes.CUSTOM_NAME, displayName);
        button.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(DataComponentTypes.BANNER_PATTERNS)
                .build());
        return button;
    }

    public static ItemStack getNextButton(Component displayName, UUID uuid, LogType type, int page, Component... lore) {
        ItemStack button = buildPageSelectorStack(displayName, NEXT_PATTERNS, lore);
        return new PersistentData(button).setUniqueId(uuid).setLogType(type).setPage(page).getItem();
    }

    public static ItemStack getBackButton(Component displayName, UUID uuid, LogType type, int page, Component... lore) {
        ItemStack button = buildPageSelectorStack(displayName, BACK_PATTERNS, lore);
        return new PersistentData(button).setUniqueId(uuid).setLogType(type).setPage(page).getItem();
    }

    public static ItemStack getInventoryBackButton(Component displayName, UUID uuid, LogType type) {
        ItemStack button = buildPageSelectorStack(displayName, BACK_PATTERNS);
        return new PersistentData(button).setUniqueId(uuid).setLogType(type).getItem();
    }

    public static ItemStack getInventoryButton(Material material, UUID uuid, LogType type, Location location, Instant timestamp, Component displayName, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        item.setData(DataComponentTypes.CUSTOM_NAME, displayName);
        item.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setTimestamp(timestamp).setLocation(location).getItem();
    }

    public static ItemStack getLogTypeButton(UUID uuid, LogType type, Component... lore) {
        ItemStack item = new ItemStack(type.getMaterial());
        item.setData(DataComponentTypes.CUSTOM_NAME, type.getComponent());
        item.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(lore)));
        return new PersistentData(item).setUniqueId(uuid).setLogType(type).getItem();
    }

    public static ItemStack getPlayerHead(OfflinePlayer player, Component displayName, Component... lore) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        skull.setData(DataComponentTypes.CUSTOM_NAME, displayName);
        skull.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(lore)));
        skull.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(player.getPlayerProfile()));
        return skull;
    }

    public static ItemStack getTeleportButton(UUID uuid, LogType type, Instant timestamp, Location location) {
        ItemStack item = ButtonType.TELEPORT.asItem();
        item.setData(DataComponentTypes.CUSTOM_NAME, Message.COMMAND_RESTORE_TELEPORT_DESCRIPTION.build());
        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setTimestamp(timestamp).setLocation(location).getItem();
    }

    public static ItemStack getEnderChestButton(UUID uuid, LogType type, Instant timestamp) {
        ItemStack item = ButtonType.ENDER_CHEST.asItem();
        item.setData(DataComponentTypes.CUSTOM_NAME, Message.INVENTORY_ICONS_RESTORE_ENDER_CHEST.build());
        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setTimestamp(timestamp).getItem();
    }

    public static ItemStack getHealthButton(UUID uuid, LogType type, double health) {
        ItemStack item = ButtonType.HEALTH.asItem();
        item.setData(DataComponentTypes.CUSTOM_NAME, Message.INVENTORY_ICONS_RESTORE_HEALTH.build());
        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setHealth(health).getItem();
    }

    public static ItemStack getHungerButton(UUID uuid, LogType type, int hunger, float saturation) {
        ItemStack item = ButtonType.HUNGER.asItem();
        item.setData(DataComponentTypes.CUSTOM_NAME, Message.INVENTORY_ICONS_RESTORE_HUNGER.build());
        return new PersistentData(item).setUniqueId(uuid).setLogType(type).setHunger(hunger).setSaturation(saturation).getItem();
    }

    public static ItemStack getExperienceButton(UUID uuid, LogType logType, int experience) {
        ItemStack item = ButtonType.EXPERIENCE.asItem();
        item.setData(DataComponentTypes.CUSTOM_NAME, Message.INVENTORY_ICONS_RESTORE_EXPERIENCE_NAME.build());
        item.setData(DataComponentTypes.LORE, ItemLore.lore()
                .addLine(Message.INVENTORY_ICONS_RESTORE_EXPERIENCE_LORE.build(experience))
                .build());
        return new PersistentData(item).setUniqueId(uuid).setLogType(logType).setExperience(experience).getItem();
    }
}

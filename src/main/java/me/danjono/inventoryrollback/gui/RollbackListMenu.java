package me.danjono.inventoryrollback.gui;

import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.items.Buttons;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.model.PlayerData;
import me.danjono.inventoryrollback.saving.ConfigurateUtil;
import me.danjono.inventoryrollback.saving.DeathCauseInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.danjono.inventoryrollback.saving.ConfigurateUtil.FILE_EXT;

public class RollbackListMenu {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final int MAX_PER_PAGE = 9 * 4;

    private final UUID targetId;
    private final LogType logType;
    private final Path playerDir;
    private int pageNumber;

    public RollbackListMenu(UUID targetId, LogType logType, int page) {
        this.targetId = targetId;
        this.logType = logType;
        this.playerDir = PlayerData.getPlayerDir(targetId, logType);
        this.pageNumber = page;
    }

    public Inventory showBackups() {
        Inventory backupMenu = Bukkit.createInventory(null, 45, InventoryType.ROLLBACK_LIST.getName());

        List<SummaryData> summaries = this.loadSummaries();
        summaries.sort(Comparator.comparing(SummaryData::timestamp).reversed());
        int pages = NumberConversions.ceil(summaries.size() / (double) MAX_PER_PAGE);

        if (this.pageNumber > pages) {
            this.pageNumber = pages;
        } else if (this.pageNumber <= 0) {
            this.pageNumber = 1;
        }

        int position = 0;
        for (int i = 0; i < MAX_PER_PAGE; i++) {
            try {
                SummaryData summary = summaries.get(((pageNumber - 1) * MAX_PER_PAGE) + i);
                List<Component> lore = new ArrayList<>();
                if (summary.deathCause != null) {
                    lore.add(Message.COMMAND_RESTORE_SPECIFIC_DEATH_REASON.build(summary.deathCause.asComponent()));
                }
                if (summary.deathLocation != null) {
                    Location loc = summary.deathLocation;
                    if (loc.isWorldLoaded()) {
                        lore.add(Message.LOCATION_WORLD.build(loc.getWorld().getName()));
                    }
                    lore.add(Message.LOCATION_X.build(loc.getX()));
                    lore.add(Message.LOCATION_Y.build(loc.getY()));
                    lore.add(Message.LOCATION_Z.build(loc.getZ()));
                }

                LocalDateTime localDateTime = LocalDateTime.ofInstant(summary.timestamp, ZoneId.systemDefault());
                Component displayName = Message.COMMAND_RESTORE_SPECIFIC_DEATH_TIME.build(
                        DATE_FORMAT.format(localDateTime));
                ItemStack item = Buttons.getInventoryButton(Material.CHEST, targetId, logType, summary.deathLocation, summary.timestamp, displayName, lore);

                backupMenu.setItem(position, item);
            } catch (IndexOutOfBoundsException ignored) {
            }

            position++;
        }

        if (pageNumber == 1) {
            ItemStack mainMenu = Buttons.getBackButton(Message.INVENTORY_ICONS_MAIN_MENU.build(), targetId, logType, 0);
            backupMenu.setItem(position + 1, mainMenu);
        }

        if (pageNumber > 1) {
            ItemStack previousPage = Buttons.getBackButton(Message.INVENTORY_ICONS_PAGE_PREVIOUS.build(), targetId, logType, pageNumber - 1, Component.text("Page " + (pageNumber - 1)));
            backupMenu.setItem(position + 1, previousPage);
        }

        if (pageNumber < pages) {
            ItemStack nextPage = Buttons.getNextButton(Message.INVENTORY_ICONS_PAGE_NEXT.build(), targetId, logType, pageNumber + 1, Component.text("Page " + (pageNumber + 1)));
            backupMenu.setItem(position + 7, nextPage);
        }

        return backupMenu;
    }

    private List<SummaryData> loadSummaries() {
        try (Stream<Path> list = Files.list(this.playerDir)) {
            ConfigurationOptions configOptions = ConfigurateUtil.getConfigOptions();
            return list.map(path -> {
                String fileName = path.getFileName().toString();
                String fileNameNoSuffix = fileName.substring(0, fileName.length() - FILE_EXT.length());
                long timestampMillis = Long.parseLong(fileNameNoSuffix);
                try {
                    BasicConfigurationNode node = GsonConfigurationLoader.builder()
                            .path(path).defaultOptions(configOptions).build().load();
                    return new SummaryData(timestampMillis, node);
                } catch (ConfigurateException exception) {
                    throw new RuntimeException("Error while loading summary " + fileName + " for " + this.targetId);
                }
            }).collect(Collectors.toList());
        } catch (IOException exception) {
            throw new RuntimeException("Error while listing saves for " + this.targetId + " type " + this.logType);
        }
    }

    private record SummaryData(
            Instant timestamp,
            @Nullable DeathCauseInfo deathCause,
            @Nullable Location deathLocation
    ) {
        private SummaryData(long timestampMillis, ConfigurationNode node) throws SerializationException {
            this(Instant.ofEpochMilli(timestampMillis),
                    node.node("death", "source").get(DeathCauseInfo.class),
                    node.node("death", "location").get(Location.class));
        }
    }
}

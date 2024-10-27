package me.danjono.inventoryrollback.model;

import me.danjono.inventoryrollback.InventoryRollbackMain;
import me.danjono.inventoryrollback.saving.ConfigurateUtil;
import org.bukkit.OfflinePlayer;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;

import static me.danjono.inventoryrollback.saving.ConfigurateUtil.FILE_EXT;

public final class PlayerData {

    private static final Path SAVES = InventoryRollbackMain.getInstance().getDataPath().resolve("saves");

    private PlayerData() {
    }

    public static @Nullable ConfigurationNode loadNode(OfflinePlayer target, LogType logType, Instant timestamp) {
        return loadNode(target.getUniqueId(), logType, timestamp);
    }

    public static @Nullable ConfigurationNode loadNode(UUID targetId, LogType logType, Instant timestamp) {
        Path path = getPlayerDir(targetId, logType).resolve(timestamp.toEpochMilli() + FILE_EXT);
        if (Files.notExists(path)) {
            return null; // not found
        }
        GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
                .defaultOptions(ConfigurateUtil.getConfigOptions())
                .path(path).build();
        try {
            return loader.load();
        } catch (ConfigurateException exception) {
            throw new RuntimeException("Error while loading " + logType + " snapshot for " + targetId + " at " + timestamp);
        }
    }

    public static boolean hasData(OfflinePlayer target, LogType logType) {
        return hasData(target.getUniqueId(), logType);
    }

    public static boolean hasData(UUID targetId, LogType logType) {
        Path playerDir = getPlayerDir(targetId, logType);
        try (Stream<Path> list = Files.list(playerDir)) {
            return list.iterator().hasNext();
        } catch (IOException exception) {
            throw new RuntimeException("Error while checking for data of " + targetId, exception);
        }
    }

    public static Path getPlayerDir(OfflinePlayer target, LogType logType) {
        return getPlayerDir(target.getUniqueId(), logType);
    }

    public static Path getPlayerDir(UUID targetId, LogType logType) {
        Path playerDir = SAVES.resolve(logType.getFolderName()).resolve(targetId.toString());
        if (Files.notExists(playerDir)) {
            try {
                Files.createDirectories(playerDir);
            } catch (IOException exception) {
                throw new RuntimeException("Error while creating directory for player " + targetId, exception);
            }
        }
        return playerDir;
    }
}

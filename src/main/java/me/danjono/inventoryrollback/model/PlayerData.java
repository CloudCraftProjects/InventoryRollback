package me.danjono.inventoryrollback.model;

import me.danjono.inventoryrollback.InventoryRollbackMain;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerData {

    private static final File SAVES = new File(InventoryRollbackMain.getInstance().getDataFolder(), "saves");
    private final FileConfiguration playerData;
    private final File playerFile;
    private final Object LOCK = (byte) 0;

    public PlayerData(OfflinePlayer player, LogType type) {
        this(player.getUniqueId(), type);
    }

    public PlayerData(UUID uniqueId, LogType type) {
        playerFile = type.getFile(SAVES, uniqueId);
        playerData = YamlConfiguration.loadConfiguration(playerFile);
    }

    public File getFile() {
        return playerFile;
    }

    public FileConfiguration getData() {
        return playerData;
    }

    public void saveData(boolean async) {
        synchronized (LOCK) {
            Runnable runnable = () -> {
                try {
                    playerData.save(playerFile);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            };

            if (async) {
                Bukkit.getScheduler().runTaskAsynchronously(InventoryRollbackMain.getInstance(), runnable);
            } else {
                runnable.run();
            }
        }
    }
}

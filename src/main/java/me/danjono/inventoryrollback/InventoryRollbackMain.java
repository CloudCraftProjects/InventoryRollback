package me.danjono.inventoryrollback;

import me.danjono.inventoryrollback.commands.InventoryRollbackCommand;
import me.danjono.inventoryrollback.i18n.TranslationManager;
import me.danjono.inventoryrollback.listeners.InventoryClickListener;
import me.danjono.inventoryrollback.listeners.SaveListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class InventoryRollbackMain extends JavaPlugin {

    private static InventoryRollbackMain instance;

    @Override
    public void onEnable() {
        // Setting instance
        instance = this;

        // Load translations from the .properties file inside the jar
        TranslationManager.get().reload();

        // Create the folders, where all of the backups will be stored
        createFolders(new File(getDataFolder(), "saves"));

        // Register the listener used to make the plugin work
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new SaveListener(), this);

        // Register the main plugin command
        Bukkit.getCommandMap().register("ir", new InventoryRollbackCommand());
    }

    @Override
    public void onDisable() {
        // Get inventory rollback command
        Command command = Bukkit.getCommandMap().getCommand(InventoryRollbackCommand.NAME);

        // Check if the command has been registered
        if (command != null) {
            // Unregister it, if so
            command.unregister(Bukkit.getCommandMap());
        }
    }

    private void createFolders(File saves) {
        new File(saves, "join").mkdirs();
        new File(saves, "quit").mkdirs();
        new File(saves, "death").mkdirs();
        new File(saves, "force").mkdirs();
        new File(saves, "world-change").mkdirs();
    }

    public static InventoryRollbackMain getInstance() {
        return instance;
    }
}

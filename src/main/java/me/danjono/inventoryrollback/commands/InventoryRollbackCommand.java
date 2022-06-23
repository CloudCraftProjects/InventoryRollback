package me.danjono.inventoryrollback.commands;

import me.danjono.inventoryrollback.InventoryRollbackMain;
import me.danjono.inventoryrollback.gui.MainMenu;
import me.danjono.inventoryrollback.i18n.Message;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.saving.SaveInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InventoryRollbackCommand extends Command {

    public static final String NAME = "inventoryrollback";
    public static final String PERMISSION = "inventoryrollback.command";
    public static final List<String> ALIASES = Collections.singletonList("ir");
    public static final String DESCRIPTION = "Use the InventoryRollback plugin.";

    public static final Component USAGE = Message.COMMAND_USAGE.build(NAME);
    public static final String USAGE_TEXT = LegacyComponentSerializer.legacySection().serialize(USAGE);

    public InventoryRollbackCommand() {
        super(NAME, DESCRIPTION, USAGE_TEXT, ALIASES);
        setPermission(PERMISSION);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length >= 1) {
            if ("restore".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("inventoryrollback.command.restore")) {
                    if (sender instanceof Player player) {
                        OfflinePlayer target;

                        if (args.length > 1) {
                            OfflinePlayer newTarget = Bukkit.getOfflinePlayer(args[1]);
                            if (newTarget.hasPlayedBefore()) {
                                target = newTarget;
                            } else {
                                Message.COMMAND_RESTORE_PLAYER_NOT_FOUND.send(sender, args[1]);
                                return true;
                            }
                        } else {
                            target = player;
                        }

                        Inventory inventory = new MainMenu(player, target).getMenu();
                        if (inventory != null) {
                            player.openInventory(inventory);
                        }
                    } else {
                        Message.COMMAND_MISC_PLAYERS_ONLY.send(sender);
                    }
                } else {
                    Message.ERRORS_NO_PERMISSION.send(sender);
                }
            } else if ("backup".equalsIgnoreCase(args[0])) {
                if (sender.hasPermission("inventoryrollback.command.backup")) {
                    Player target;

                    if (args.length > 1) {
                        OfflinePlayer newTarget = Bukkit.getOfflinePlayer(args[1]);
                        if (newTarget.hasPlayedBefore()) {
                            if ((target = newTarget.getPlayer()) == null) {
                                Message.COMMAND_FORCE_SAVE_ERRORED.send(sender, args[1]);
                                return true;
                            }
                        } else {
                            Message.COMMAND_RESTORE_PLAYER_NOT_FOUND.send(sender, args[1]);
                            return true;
                        }
                    } else if (sender instanceof Player player) {
                        target = player;
                    } else {
                        Message.COMMAND_RESTORE_PLAYER_NOT_FOUND.send(sender, PlainTextComponentSerializer.plainText().serialize(Message.MISC_NONE.build()));
                        return true;
                    }

                    new SaveInventory(target, LogType.FORCE, EntityDamageEvent.DamageCause.CUSTOM, target.getInventory(), target.getEnderChest()).createSave();
                    Message.COMMAND_FORCE_SAVE_SUCCESS.send(sender, target.getName());
                } else {
                    Message.ERRORS_NO_PERMISSION.send(sender);
                }
            } else if ("version".equalsIgnoreCase(args[0])) {
                String version = "v" + InventoryRollbackMain.getInstance().getDescription().getVersion();
                Message.COMMAND_VERSION.send(sender, version, "danjono", "booky10");
            } else {
                Message.COMMAND_USAGE.send(sender, label);
            }
        } else {
            Message.COMMAND_USAGE.send(sender, label);
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        Stream<String> completions;

        if (args.length == 1) {
            completions = Stream.of("help", "version", "restore", "backup");
        } else if (args.length == 2 && ("restore".equalsIgnoreCase(args[0]) || "backup".equalsIgnoreCase(args[0]))) {
            completions = Bukkit.getOnlinePlayers().stream().map(Player::getName);
        } else {
            return Collections.emptyList();
        }

        return completions.filter(suggestion -> suggestion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())).collect(Collectors.toList());
    }
}

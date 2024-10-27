package me.danjono.inventoryrollback.i18n;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;

import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public interface Message {

    Component PREFIX_COMPONENT = text()
            .append(text('[', GRAY))
            .append(text("InventoryRollback", AQUA))
            .append(text(']', GRAY))
            .build();

    static TextComponent prefixed(ComponentLike component) {
        return text()
                .append(PREFIX_COMPONENT)
                .append(space())
                .append(component)
                .build();
    }

    Args0 MISC_NONE = () -> translatable()
            .key("ir.misc.none")
            .color(RED)
            .build();

    Args0 ERRORS_NO_PERMISSION = () -> prefixed(translatable()
            .key("ir.errors.no-permission")
            .color(RED)
            .build()
    );

    Args1<String> ERRORS_NO_BACKUP = player -> prefixed(translatable()
            .key("ir.errors.no-backup")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args0 COMMAND_MISC_PLAYERS_ONLY = () -> prefixed(translatable()
            .key("ir.command.misc.players-only")
            .color(RED)
            .build()
    );

    Args1<String> COMMAND_USAGE = label -> prefixed(translatable()
            .key("ir.command.usage")
            .color(RED)
            .args(text(label))
            .build()
    );

    Args3<String, String, String> COMMAND_VERSION = (version, creator, rewriter) -> prefixed(translatable()
            .key("ir.command.version")
            .color(GREEN)
            .args(text(version, WHITE), text(creator, WHITE), text(rewriter, WHITE))
            .build()
    );

    Args1<String> COMMAND_FORCE_SAVE_SUCCESS = player -> prefixed(translatable()
            .key("ir.command.force-save.success")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_FORCE_SAVE_ERRORED = player -> prefixed(translatable()
            .key("ir.command.force-save.errored")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_PLAYER_NOT_FOUND = player -> prefixed(translatable()
            .key("ir.command.restore.player-not-found")
            .color(RED)
            .args(text(player, RED))
            .build()
    );

    Args1<String> COMMAND_RESTORE_ENDER_CHEST_SUCCESS_SELF = player -> prefixed(translatable()
            .key("ir.command.restore.ender-chest.success.self")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_ENDER_CHEST_SUCCESS_TARGET = player -> prefixed(translatable()
            .key("ir.command.restore.ender-chest.success.target")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_ENDER_CHEST_ERRORED_NOT_EMPTY = player -> prefixed(translatable()
            .key("ir.command.restore.ender-chest.errored.not-empty")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_ENDER_CHEST_ERRORED_NOT_ONLINE = player -> prefixed(translatable()
            .key("ir.command.restore.ender-chest.errored.not-online")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_HEALTH_SUCCESS_SELF = player -> prefixed(translatable()
            .key("ir.command.restore.health.success.self")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_HEALTH_SUCCESS_TARGET = player -> prefixed(translatable()
            .key("ir.command.restore.health.success.target")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_HEALTH_ERRORED = player -> prefixed(translatable()
            .key("ir.command.restore.health.errored")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_HUNGER_SUCCESS_SELF = player -> prefixed(translatable()
            .key("ir.command.restore.hunger.success.self")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_HUNGER_SUCCESS_TARGET = player -> prefixed(translatable()
            .key("ir.command.restore.hunger.success.target")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_HUNGER_ERRORED = player -> prefixed(translatable()
            .key("ir.command.restore.hunger.errored")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_EXPERIENCE_SUCCESS_SELF = player -> prefixed(translatable()
            .key("ir.command.restore.experience.success.self")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_EXPERIENCE_SUCCESS_TARGET = player -> prefixed(translatable()
            .key("ir.command.restore.experience.success.target")
            .color(GREEN)
            .args(text(player, WHITE))
            .build()
    );

    Args1<String> COMMAND_RESTORE_EXPERIENCE_ERRORED = player -> prefixed(translatable()
            .key("ir.command.restore.experience.errored")
            .color(RED)
            .args(text(player, WHITE))
            .build()
    );

    Args1<Location> COMMAND_RESTORE_TELEPORT_SUCCESS = location -> prefixed(translatable()
            .key("ir.command.restore.teleport.success")
            .color(GREEN)
            .args(formatLocation(location))
            .build()
    );

    Args1<Location> COMMAND_RESTORE_TELEPORT_ERRORED = location -> prefixed(translatable()
            .key("ir.command.restore.teleport.errored")
            .color(RED)
            .args(formatLocation(location))
            .build()
    );

    Args0 COMMAND_RESTORE_TELEPORT_DESCRIPTION = () -> translatable()
            .key("ir.command.restore.teleport.description")
            .color(GRAY)
            .build();

    Args1<Component> COMMAND_RESTORE_SPECIFIC_DEATH_REASON = reason -> translatable()
            .key("ir.command.restore.specific.death.reason")
            .color(GOLD)
            .args(text().color(WHITE).append(reason).build())
            .build();

    Args1<String> COMMAND_RESTORE_SPECIFIC_DEATH_TIME = time -> translatable()
            .key("ir.command.restore.specific.death.time")
            .color(GOLD)
            .args(text(time, WHITE))
            .build();

    Args0 INVENTORY_ICONS_DEATH = () -> translatable()
            .key("ir.inventory.icons.death")
            .color(RED)
            .build();

    Args0 INVENTORY_ICONS_JOIN = () -> translatable()
            .key("ir.inventory.icons.join")
            .color(GREEN)
            .build();

    Args0 INVENTORY_ICONS_QUIT = () -> translatable()
            .key("ir.inventory.icons.quit")
            .color(GOLD)
            .build();

    Args0 INVENTORY_ICONS_WORLD_CHANGE = () -> translatable()
            .key("ir.inventory.icons.world-change")
            .color(YELLOW)
            .build();

    Args0 INVENTORY_ICONS_FORCE_SAVE = () -> translatable()
            .key("ir.inventory.icons.force-save")
            .color(AQUA)
            .build();

    Args0 INVENTORY_ICONS_MAIN_MENU = () -> translatable()
            .key("ir.inventory.icons.main-menu")
            .color(WHITE)
            .build();

    Args0 INVENTORY_ICONS_PAGE_NEXT = () -> translatable()
            .key("ir.inventory.icons.page.next")
            .color(WHITE)
            .build();

    Args0 INVENTORY_ICONS_PAGE_PREVIOUS = () -> translatable()
            .key("ir.inventory.icons.page.previous")
            .color(WHITE)
            .build();

    Args0 INVENTORY_ICONS_BACK = () -> translatable()
            .key("ir.inventory.icons.back")
            .color(WHITE)
            .build();

    Args0 INVENTORY_ICONS_RESTORE_ENDER_CHEST = () -> translatable()
            .key("ir.inventory.icons.restore.ender-chest")
            .color(LIGHT_PURPLE)
            .build();

    Args0 INVENTORY_ICONS_RESTORE_HEALTH = () -> translatable()
            .key("ir.inventory.icons.restore.health")
            .color(RED)
            .build();

    Args0 INVENTORY_ICONS_RESTORE_HUNGER = () -> translatable()
            .key("ir.inventory.icons.restore.hunger")
            .color(GOLD)
            .build();

    Args0 INVENTORY_ICONS_RESTORE_EXPERIENCE_NAME = () -> translatable()
            .key("ir.inventory.icons.restore.experience.name")
            .color(GREEN)
            .build();

    Args1<Integer> INVENTORY_ICONS_RESTORE_EXPERIENCE_LORE = level -> translatable()
            .key("ir.inventory.icons.restore.experience.lore")
            .color(WHITE)
            .args(text(level, WHITE))
            .build();

    Args0 INVENTORY_NAMES_MAIN_MENU = () -> translatable()
            .key("ir.inventory.names.main-menu")
            .build();

    Args0 INVENTORY_NAMES_ROLLBACK_LIST = () -> translatable()
            .key("ir.inventory.names.rollback-list")
            .build();

    Args0 INVENTORY_NAMES_BACKUP = () -> translatable()
            .key("ir.inventory.names.backup")
            .build();

    Args1<String> INVENTORY_HEAD = name -> translatable()
            .key("ir.inventory.head")
            .color(GOLD)
            .args(text(name, GOLD))
            .build();

    Args1<String> LOCATION_WORLD = world -> translatable()
            .key("ir.location.world")
            .color(GOLD)
            .args(text(world, WHITE))
            .build();

    Args1<Double> LOCATION_X = world -> translatable()
            .key("ir.location.x")
            .color(GOLD)
            .args(text(world, WHITE))
            .build();

    Args1<Double> LOCATION_Y = world -> translatable()
            .key("ir.location.y")
            .color(GOLD)
            .args(text(world, WHITE))
            .build();

    Args1<Double> LOCATION_Z = world -> translatable()
            .key("ir.location.z")
            .color(GOLD)
            .args(text(world, WHITE))
            .build();

    static CustomComponentBuilder translatable() {
        return new CustomComponentBuilder();
    }

    static Component formatLocation(Location location) {
        return text(location.getX() + ", " + location.getY() + ", " + location.getZ(), YELLOW);
    }

    interface Args0 {

        Component build();

        default void send(Audience sender) {
            sender.sendMessage(build());
        }
    }

    interface Args1<A> {

        Component build(A arg0);

        default void send(Audience sender, A arg0) {
            sender.sendMessage(build(arg0));
        }
    }

    interface Args2<A, B> {

        Component build(A arg0, B arg1);

        default void send(Audience sender, A arg0, B arg1) {
            sender.sendMessage(build(arg0, arg1));
        }
    }

    interface Args3<A, B, C> {

        Component build(A arg0, B arg1, C arg2);

        default void send(Audience sender, A arg0, B arg1, C arg2) {
            sender.sendMessage(build(arg0, arg1, arg2));
        }
    }

    interface Args4<A, B, C, D> {

        Component build(A arg0, B arg1, C arg2, D arg3);

        default void send(Audience sender, A arg0, B arg1, C arg2, D arg3) {
            sender.sendMessage(build(arg0, arg1, arg2, arg3));
        }
    }

    interface Args5<A, B, C, D, E> {

        Component build(A arg0, B arg1, C arg2, D arg3, E arg4);

        default void send(Audience sender, A arg0, B arg1, C arg2, D arg3, E arg4) {
            sender.sendMessage(build(arg0, arg1, arg2, arg3, arg4));
        }
    }

    interface Args6<A, B, C, D, E, F> {

        Component build(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5);

        default void send(Audience sender, A arg0, B arg1, C arg2, D arg3, E arg4, F arg5) {
            sender.sendMessage(build(arg0, arg1, arg2, arg3, arg4, arg5));
        }
    }
}

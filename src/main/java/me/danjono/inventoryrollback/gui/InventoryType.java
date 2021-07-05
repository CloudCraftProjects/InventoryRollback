package me.danjono.inventoryrollback.gui;

import me.danjono.inventoryrollback.i18n.Message;
import net.kyori.adventure.text.Component;

public enum InventoryType {

    MAIN_MENU(Message.INVENTORY_NAMES_MAIN_MENU.build()),
    ROLLBACK_LIST(Message.INVENTORY_NAMES_ROLLBACK_LIST.build()),
    BACKUP(Message.INVENTORY_NAMES_BACKUP.build());

    private static final InventoryType[] VALUES = values();
    private final Component menuName;

    InventoryType(Component name) {
        this.menuName = name;
    }

    public Component getName() {
        return menuName;
    }

    public static InventoryType getType(Component name) {
        for (InventoryType type : VALUES) {
            if (!type.getName().equals(name)) continue;
            return type;
        }

        return null;
    }
}

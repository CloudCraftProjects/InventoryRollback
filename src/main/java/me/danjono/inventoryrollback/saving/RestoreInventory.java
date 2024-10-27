package me.danjono.inventoryrollback.saving;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;

import java.time.Instant;
import java.util.Base64;

@NullMarked
public record RestoreInventory(
        Instant timestamp,
        byte[] inventoryBytes,
        byte[] enderChestBytes,
        double health,
        int hunger,
        float saturation,
        int expPoints
) {

    public RestoreInventory(Instant timestamp, ConfigurationNode node) {
        this(timestamp,
                Base64.getDecoder().decode(node.node("inventory").getString()),
                Base64.getDecoder().decode(node.node("ender-chest").getString()),
                node.node("health").getDouble(),
                node.node("food", "hunger").getInt(),
                node.node("food", "saturation").getInt(),
                node.node("experience-points").getInt()
        );
    }

    public ItemStack[] inventory() {
        return ItemStack.deserializeItemsFromBytes(this.inventoryBytes);
    }

    public ItemStack[] enderChest() {
        return ItemStack.deserializeItemsFromBytes(this.enderChestBytes);
    }
}

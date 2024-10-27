package me.danjono.inventoryrollback.saving;

import me.danjono.inventoryrollback.InventoryRollbackMain;
import me.danjono.inventoryrollback.model.LogType;
import me.danjono.inventoryrollback.model.PlayerData;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.nio.file.Path;
import java.util.Base64;

import static me.danjono.inventoryrollback.saving.ConfigurateUtil.FILE_EXT;

@NullMarked
public record SaveInventory(
        Player player,
        LogType logType,
        @Nullable DamageSource source,
        PlayerInventory inventory,
        Inventory enderChest
) {

    public void createSave() {
        Path path = PlayerData.getPlayerDir(this.player, this.logType)
                .resolve(System.currentTimeMillis() + FILE_EXT);
        try {
            GsonConfigurationLoader.builder()
                    .path(path).indent(0).build()
                    .save(this.asNode());
        } catch (ConfigurateException exception) {
            throw new IllegalStateException("Error while creating save of " + this.logType + " for " + this.player);
        }
        InventoryRollbackMain.getInstance().getComponentLogger()
                .info("Created {} snapshot for {}", this.logType.name(), this.player.teamDisplayName());
    }

    private static String serializeItems(@Nullable ItemStack[] stacks) {
        byte[] bytes = ItemStack.serializeItemsAsBytes(stacks);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public ConfigurationNode asNode() throws SerializationException {
        ConfigurationNode node = ConfigurateUtil.createRootNode();
        node.node("inventory").set(String.class, serializeItems(this.inventory.getContents()));
        node.node("ender-chest").set(String.class, serializeItems(this.enderChest.getContents()));
        node.node("death", "source").set(DeathCauseInfo.class, DeathCauseInfo.fromSource(this.source));
        node.node("death", "location").set(Location.class, this.player.getLocation());
        node.node("health").set(double.class, this.player.getHealth());
        node.node("food", "hunger").set(int.class, this.player.getFoodLevel());
        node.node("food", "saturation").set(float.class, this.player.getSaturation());
        node.node("experience-points").set(int.class, this.player.calculateTotalExperiencePoints());
        node.node("player").set(EntityInfo.class, EntityInfo.fromEntity(this.player));
        return node;
    }
}

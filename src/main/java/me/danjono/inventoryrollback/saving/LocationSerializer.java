package me.danjono.inventoryrollback.saving;
// Created by booky10 in InventoryRollback (03:32 27.10.2024)

import net.kyori.adventure.key.Key;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static net.kyori.adventure.key.Key.key;

public final class LocationSerializer implements TypeSerializer<Location> {

    public static final TypeSerializer<Location> INSTANCE = new LocationSerializer();

    private LocationSerializer() {
    }

    @SuppressWarnings("PatternValidation")
    @Override
    public Location deserialize(Type type, ConfigurationNode node) throws SerializationException {
        Key worldKey = key(node.node("dimension").get(String.class, "overworld"));
        World world = Bukkit.getWorld(worldKey); // may be unloaded, is valid bukkit api
        String[] posSplit = StringUtils.split(node.node("pos").getString("0.0 0.0 0.0"), ' ');
        double posX = Double.parseDouble(posSplit[0]);
        double posY = Double.parseDouble(posSplit[1]);
        double posZ = Double.parseDouble(posSplit[2]);
        String[] rotSplit = StringUtils.split(node.node("rot").getString("0.0 0.0"), ' ');
        float yaw = Float.parseFloat(rotSplit[0]);
        float pitch = Float.parseFloat(rotSplit[0]);
        return new Location(world, posX, posY, posZ, yaw, pitch);
    }

    @Override
    public void serialize(Type type, @Nullable Location obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }
        if (obj.isWorldLoaded()) {
            node.node("dimension").set(String.class, obj.getWorld().key().asMinimalString());
        }
        node.node("pos").set(String.class, obj.getX() + " " + obj.getY() + " " + obj.getZ());
        node.node("rot").set(String.class, obj.getYaw() + " " + obj.getPitch());
    }
}

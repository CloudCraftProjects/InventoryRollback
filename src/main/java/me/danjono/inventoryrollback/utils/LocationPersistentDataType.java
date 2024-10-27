package me.danjono.inventoryrollback.utils;
// Created by booky10 in Smp (04:57 26.10.2024)

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.nio.ByteBuffer;
import java.util.UUID;

@NullMarked
public class LocationPersistentDataType implements PersistentDataType<byte[], Location> {

    public static final PersistentDataType<byte[], Location> INSTANCE = new LocationPersistentDataType();

    private LocationPersistentDataType() {
    }

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public byte[] toPrimitive(Location complex, PersistentDataAdapterContext context) {
        byte[] array = new byte[Long.BYTES * 2 + Double.BYTES * 3 + Float.BYTES * 2];
        UUID worldId = complex.getWorld().getUID();
        ByteBuffer.wrap(array)
                .putLong(worldId.getMostSignificantBits())
                .putLong(worldId.getLeastSignificantBits())
                .putDouble(complex.getX())
                .putDouble(complex.getY())
                .putDouble(complex.getZ())
                .putFloat(complex.getYaw())
                .putFloat(complex.getPitch());
        return array;
    }

    @Override
    public Location fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer buf = ByteBuffer.wrap(primitive);
        UUID worldId = new UUID(buf.getLong(), buf.getLong());
        World world = Bukkit.getWorld(worldId);
        if (world == null) {
            throw new IllegalStateException("Can't resolve world with id " + worldId);
        }
        double posX = buf.getDouble();
        double posY = buf.getDouble();
        double posZ = buf.getDouble();
        float yaw = buf.getFloat();
        float pitch = buf.getFloat();
        return new Location(world, posX, posY, posZ, yaw, pitch);
    }
}

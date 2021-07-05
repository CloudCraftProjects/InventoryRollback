package me.danjono.inventoryrollback.utils;
// Created by booky10 in InventoryRollback (15:23 05.07.21)

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.function.IntFunction;

public record ByteSerializer<T extends ConfigurationSerializable>(IntFunction<T[]> arrayFunction) {

    public static final ByteSerializer<Location> LOCATION_SERIALIZER = new ByteSerializer<>(Location[]::new);
    public static final ByteSerializer<ItemStack> ITEMSTACK_SERIALIZER = new ByteSerializer<>(ItemStack[]::new);

    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public byte[] objectToByteArray(T object) {
        byte[] bytes;

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream data = new BukkitObjectOutputStream(output)) {
                data.writeObject(object);
            }

            bytes = output.toByteArray();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return bytes;
    }

    @SuppressWarnings("unchecked")
    public T objectFromByteArray(byte[] bytes) {
        T object;

        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            try (BukkitObjectInputStream data = new BukkitObjectInputStream(input)) {
                object = (T) data.readObject();
            } catch (ClassNotFoundException exception) {
                throw new RuntimeException(exception);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return object;
    }

    public byte[] objectArrayToByteArray(T[] objectArray) {
        byte[] bytes;

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream data = new BukkitObjectOutputStream(output)) {
                data.writeInt(objectArray.length);

                for (T object : objectArray) {
                    data.writeObject(object);
                }
            }

            bytes = output.toByteArray();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return bytes;
    }

    @SuppressWarnings("unchecked")
    public T[] objectArrayFromByteArray(byte[] bytes) {
        T[] objectArray;

        try (ByteArrayInputStream input = new ByteArrayInputStream(bytes)) {
            try (BukkitObjectInputStream data = new BukkitObjectInputStream(input)) {
                objectArray = arrayFunction.apply(data.readInt());

                for (int i = 0; i < objectArray.length; i++) {
                    objectArray[i] = (T) data.readObject();
                }
            } catch (ClassNotFoundException exception) {
                throw new RuntimeException(exception);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return objectArray;
    }

    public String objectToBase64(T object) {
        byte[] bytes;

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream data = new BukkitObjectOutputStream(output)) {
                data.writeObject(object);
            }

            bytes = output.toByteArray();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return ENCODER.encodeToString(bytes);
    }

    @SuppressWarnings("unchecked")
    public T objectFromBase64(String base64) {
        T object;

        try (ByteArrayInputStream input = new ByteArrayInputStream(DECODER.decode(base64))) {
            try (BukkitObjectInputStream data = new BukkitObjectInputStream(input)) {
                object = (T) data.readObject();
            } catch (ClassNotFoundException exception) {
                throw new RuntimeException(exception);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return object;
    }

    public String objectArrayToBase64(T[] objectArray) {
        byte[] bytes;

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            try (BukkitObjectOutputStream data = new BukkitObjectOutputStream(output)) {
                data.writeInt(objectArray.length);

                for (T object : objectArray) {
                    data.writeObject(object);
                }
            }

            bytes = output.toByteArray();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return ENCODER.encodeToString(bytes);
    }

    @SuppressWarnings("unchecked")
    public T[] objectArrayFromBase64(String base64) {
        T[] objectArray;

        try (ByteArrayInputStream input = new ByteArrayInputStream(DECODER.decode(base64))) {
            try (BukkitObjectInputStream data = new BukkitObjectInputStream(input)) {
                objectArray = arrayFunction.apply(data.readInt());

                for (int i = 0; i < objectArray.length; i++) {
                    objectArray[i] = (T) data.readObject();
                }
            } catch (ClassNotFoundException exception) {
                throw new RuntimeException(exception);
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return objectArray;
    }
}

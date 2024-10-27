package me.danjono.inventoryrollback.saving;
// Created by booky10 in InventoryRollback (02:11 27.10.2024)

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Supplier;

@NullMarked
public record EntityInfo(
        UUID uniqueId,
        Component name
) {

    public static final TypeSerializer<EntityInfo> SERIALIZER = new TypeSerializer<>() {
        @Override
        public EntityInfo deserialize(Type type, ConfigurationNode node) throws SerializationException {
            UUID uniqueId = node.node("uuid").get(UUID.class,
                    (Supplier<UUID>) () -> new UUID(0L, 0L));
            Component name = node.node("name").get(Component.class,
                    (Supplier<Component>) Component::empty);
            return new EntityInfo(uniqueId, name);
        }

        @Override
        public void serialize(Type type, @Nullable EntityInfo obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                node.set(null);
                return;
            }
            node.node("uuid").set(UUID.class, obj.uniqueId);
            node.node("name").set(Component.class, obj.name);
        }
    };

    public static @Nullable EntityInfo fromEntity(@Nullable Entity entity) {
        if (entity != null) {
            return new EntityInfo(entity.getUniqueId(), entity.teamDisplayName());
        }
        return null;
    }
}

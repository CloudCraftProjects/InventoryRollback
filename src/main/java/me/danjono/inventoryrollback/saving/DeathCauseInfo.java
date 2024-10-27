package me.danjono.inventoryrollback.saving;
// Created by booky10 in InventoryRollback (02:11 27.10.2024)

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

import static io.papermc.paper.registry.RegistryAccess.registryAccess;
import static io.papermc.paper.registry.RegistryKey.DAMAGE_TYPE;
import static net.kyori.adventure.key.Key.key;
import static net.kyori.adventure.text.Component.text;

@NullMarked
public record DeathCauseInfo(
        DamageType damageType,
        @Nullable Location damageLocation,
        @Nullable EntityInfo directEntity,
        @Nullable EntityInfo causingEntity
) {

    public static final TypeSerializer<DeathCauseInfo> SERIALIZER = new TypeSerializer<>() {
        @SuppressWarnings("PatternValidation")
        @Override
        public DeathCauseInfo deserialize(Type type, ConfigurationNode node) throws SerializationException {
            Key damageTypeKey = key(node.node("damage-type").getString("generic_kill"));
            DamageType damageType = registryAccess().getRegistry(DAMAGE_TYPE).getOrThrow(damageTypeKey);
            Location damageLocation = node.node("damage-location").get(Location.class);
            EntityInfo directEntity = node.node("direct-entity").get(EntityInfo.class);
            EntityInfo causingEntity = node.node("causing-entity").get(EntityInfo.class);
            return new DeathCauseInfo(damageType, damageLocation, directEntity, causingEntity);
        }

        @Override
        public void serialize(Type type, @Nullable DeathCauseInfo obj, ConfigurationNode node) throws SerializationException {
            if (obj == null) {
                node.set(null);
                return;
            }
            node.node("damage-type").set(String.class, obj.damageType.key().asMinimalString());
            if (obj.damageLocation != null) {
                node.node("damage-location").set(Location.class, obj.damageLocation);
            }
            if (obj.directEntity != null) {
                node.node("direct-entity").set(EntityInfo.class, obj.directEntity);
            }
            if (obj.causingEntity != null) {
                node.node("causing-entity").set(EntityInfo.class, obj.causingEntity);
            }
        }
    };

    public static @Nullable DeathCauseInfo fromSource(@Nullable DamageSource source) {
        if (source != null) {
            return new DeathCauseInfo(source.getDamageType(), source.getDamageLocation(),
                    EntityInfo.fromEntity(source.getDirectEntity()),
                    EntityInfo.fromEntity(source.getCausingEntity()));
        }
        return null;
    }

    public Component asComponent() {
        return text(this.damageType.getKey().asMinimalString());
    }
}

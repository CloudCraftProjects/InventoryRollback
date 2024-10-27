package me.danjono.inventoryrollback.saving;
// Created by booky10 in InventoryRollback (02:38 27.10.2024)

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

public final class ConfigurateUtil {

    public static final String FILE_EXT = ".json";
    public static final TypeSerializerCollection SERIALIZERS = TypeSerializerCollection.builder()
            .register(Component.class, ComponentSerializer.INSTANCE)
            .register(Location.class, LocationSerializer.INSTANCE)
            .register(DeathCauseInfo.class, DeathCauseInfo.SERIALIZER)
            .register(EntityInfo.class, EntityInfo.SERIALIZER)
            .build();
    private static final ConfigurationOptions CONFIG_OPTIONS = ConfigurationOptions.defaults()
            .serializers(SERIALIZERS);

    private ConfigurateUtil() {
    }

    public static ConfigurationOptions getConfigOptions() {
        return ConfigurationOptions.defaults().serializers(SERIALIZERS);
    }

    public static ConfigurationNode createRootNode() {
        return BasicConfigurationNode.root(CONFIG_OPTIONS);
    }
}

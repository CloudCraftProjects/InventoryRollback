package me.danjono.inventoryrollback.saving;
// Created by booky10 in InventoryRollback (02:24 27.10.2024)

import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.function.Predicate;

@NullMarked
public class ComponentSerializer extends ScalarSerializer<Component> {

    public static final TypeSerializer<Component> INSTANCE = new ComponentSerializer();

    private ComponentSerializer() {
        super(new TypeToken<Component>() {});
    }

    @Override
    public Component deserialize(Type type, Object obj) {
        return MiniMessage.miniMessage().deserialize(String.valueOf(obj));
    }

    @Override
    protected Object serialize(Component item, Predicate<Class<?>> typeSupported) {
        return MiniMessage.miniMessage().serialize(item);
    }
}

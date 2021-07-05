package me.danjono.inventoryrollback.i18n;
// Created by booky10 in InventoryRollback (11:57 05.07.21)

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.translation.GlobalTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CustomComponentBuilder {

    private final Map<TextDecoration, TextDecoration.State> decorations = new HashMap<>();
    private Component[] args = new Component[0];
    private TextColor color;
    private String key;

    public CustomComponentBuilder() {
        this(null);
    }

    public CustomComponentBuilder(String key) {
        this.key = key;

        decorations.put(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public CustomComponentBuilder key(@NotNull String key) {
        this.key = key;
        return this;
    }

    public @NotNull CustomComponentBuilder color(@Nullable TextColor color) {
        this.color = color;
        return this;
    }

    public @NotNull CustomComponentBuilder decorate(@NotNull TextDecoration decoration) {
        return decoration(decoration, true);
    }

    public @NotNull CustomComponentBuilder decoration(@NotNull TextDecoration decoration, boolean state) {
        decorations.put(decoration, TextDecoration.State.byBoolean(state));
        return this;
    }

    public CustomComponentBuilder args(Component... args) {
        this.args = args;
        return this;
    }

    public @NotNull Component build() {
        return GlobalTranslator.render(Component.translatable(key, color, args).decorations(decorations), TranslationManager.DEFAULT_LOCALE);
    }
}

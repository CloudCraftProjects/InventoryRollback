package me.danjono.inventoryrollback.i18n;
// Created by booky10 in InventoryRollback (11:57 05.07.21)

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.util.UTF8ResourceBundleControl;

import java.util.Locale;
import java.util.ResourceBundle;

public class TranslationManager {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final TranslationManager INSTANCE = new TranslationManager();
    private ColoredTranslationRegistry registry;

    private TranslationManager() {
    }

    public static TranslationManager get() {
        return INSTANCE;
    }

    public void reload() {
        if (registry != null) {
            GlobalTranslator.translator().removeSource(registry);
        }

        registry = new ColoredTranslationRegistry(Key.key("ir", "main"));
        registry.defaultLocale(DEFAULT_LOCALE);

        ResourceBundle bundle = ResourceBundle.getBundle("ir", DEFAULT_LOCALE, UTF8ResourceBundleControl.get());
        registry.registerAll(DEFAULT_LOCALE, bundle, false);

        GlobalTranslator.translator().addSource(registry);
    }
}

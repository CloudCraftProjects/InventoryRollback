package me.danjono.inventoryrollback.i18n;
// Created by booky10 in InventoryRollback (11:57 05.07.21)

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import static net.kyori.adventure.util.UTF8ResourceBundleControl.utf8ResourceBundleControl;

public class TranslationManager {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final TranslationManager INSTANCE = new TranslationManager();
    private TranslationStore<MessageFormat> store;

    private TranslationManager() {
    }

    public static TranslationManager get() {
        return INSTANCE;
    }

    public void reload() {
        if (this.store != null) {
            GlobalTranslator.translator().removeSource(this.store);
        }
        this.store = TranslationStore.messageFormat(Key.key("ir", "main"));
        this.store.defaultLocale(DEFAULT_LOCALE);

        ResourceBundle bundle = ResourceBundle.getBundle("ir", DEFAULT_LOCALE, utf8ResourceBundleControl());
        this.store.registerAll(DEFAULT_LOCALE, bundle.keySet(),
                key -> new MessageFormat(bundle.getString(key)));

        GlobalTranslator.translator().addSource(this.store);
    }
}

package me.danjono.inventoryrollback.i18n;
// Created by booky10 in InventoryRollback (11:58 05.07.21)

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class ColoredTranslationRegistry implements Examinable, TranslationRegistry {

    private final Key name;
    private final Map<String, Translation> translations = new ConcurrentHashMap<>();
    private Locale defaultLocale = Locale.ENGLISH;

    public ColoredTranslationRegistry(Key name) {
        this.name = name;
    }

    @Override
    public void register(@NotNull String key, @NotNull Locale locale, @NotNull MessageFormat format) {
        translations.computeIfAbsent(key, Translation::new).register(locale, format);
    }

    @Override
    public void unregister(@NotNull String key) {
        translations.remove(key);
    }

    @Override
    public @NotNull Key name() {
        return name;
    }

    @Override
    public boolean contains(@NotNull String key) {
        return translations.containsKey(key);
    }

    @Override
    public void registerAll(@NotNull Locale locale, @NotNull ResourceBundle bundle, boolean escapeSingleQuotes) {
        registerAll(locale, bundle.keySet(), key -> {
            String format = ChatColor.translateAlternateColorCodes('&', bundle.getString(key));
            return new MessageFormat(format, locale);
        });
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        Translation translation = this.translations.get(key);
        if (translation == null) return null;
        return translation.translate(locale);
    }

    @Override
    public void defaultLocale(@NotNull Locale defaultLocale) {
        this.defaultLocale = requireNonNull(defaultLocale, "defaultLocale");
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("translations", this.translations));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ColoredTranslationRegistry that)) return false;

        return name.equals(that.name)
                && translations.equals(that.translations)
                && defaultLocale.equals(that.defaultLocale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, translations, defaultLocale);
    }

    @Override
    public String toString() {
        return examine(StringExaminer.simpleEscaping());
    }

    public final class Translation implements Examinable {

        private final Map<Locale, MessageFormat> formats = new ConcurrentHashMap<>();
        private final String key;

        public Translation(@NotNull String key) {
            this.key = requireNonNull(key, "translation key");
        }

        public void register(@NotNull Locale locale, @NotNull MessageFormat format) {
            if (formats.putIfAbsent(requireNonNull(locale, "locale"), requireNonNull(format, "message format")) != null) {
                throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", key, locale));
            }
        }

        public @Nullable MessageFormat translate(@NotNull Locale locale) {
            MessageFormat format = formats.get(requireNonNull(locale, "locale"));
            if (format == null) {
                format = formats.get(new Locale(locale.getLanguage()));
                if (format == null) {
                    format = formats.get(defaultLocale);
                }
            }
            return format;
        }

        @Override
        public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of(
                    ExaminableProperty.of("key", key),
                    ExaminableProperty.of("formats", formats)
            );
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Translation that)) return false;
            return key.equals(that.key) && formats.equals(that.formats);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, formats);
        }

        @Override
        public String toString() {
            return examine(StringExaminer.simpleEscaping());
        }
    }
}

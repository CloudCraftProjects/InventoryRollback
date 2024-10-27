package me.danjono.inventoryrollback.utils;

import me.danjono.inventoryrollback.InventoryRollbackMain;
import me.danjono.inventoryrollback.model.LogType;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class PersistentData {

    private static final NamespacedKey UUID_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "uuid");
    private static final NamespacedKey LOG_TYPE_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "log_type");
    private static final NamespacedKey LOCATION_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "location");
    private static final NamespacedKey PAGE_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "page");
    private static final NamespacedKey HUNGER_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "hunger");
    private static final NamespacedKey TIMESTAMP_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "timestamp");
    private static final NamespacedKey HEALTH_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "health");
    private static final NamespacedKey SATURATION_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "saturation");
    private static final NamespacedKey EXPERIENCE_KEY = new NamespacedKey(InventoryRollbackMain.getInstance(), "experience");

    private ItemStack item;

    public PersistentData(ItemStack item) {
        this.item = item;
    }

    public boolean hasUniqueId() {
        return item.getItemMeta().getPersistentDataContainer().has(UUID_KEY, PersistentDataType.LONG_ARRAY);
    }

    public UUID getUniqueId() {
        long[] data = item.getItemMeta().getPersistentDataContainer().get(UUID_KEY, PersistentDataType.LONG_ARRAY);
        return data == null ? null : new UUID(data[0], data[1]);
    }

    public PersistentData setUniqueId(UUID uuid) {
        ItemMeta meta = item.getItemMeta();
        long[] data = new long[]{uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()};
        meta.getPersistentDataContainer().set(UUID_KEY, PersistentDataType.LONG_ARRAY, data);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasLogType() {
        return item.getItemMeta().getPersistentDataContainer().has(LOG_TYPE_KEY, PersistentDataType.INTEGER);
    }

    public LogType getLogType() {
        Integer data = item.getItemMeta().getPersistentDataContainer().get(LOG_TYPE_KEY, PersistentDataType.INTEGER);
        return data == null ? null : LogType.VALUES[data];
    }

    public PersistentData setLogType(LogType type) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(LOG_TYPE_KEY, PersistentDataType.INTEGER, type.ordinal());
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasLocation() {
        return item.getItemMeta().getPersistentDataContainer().has(LOCATION_KEY, PersistentDataType.BYTE_ARRAY);
    }

    public Location getLocation() {
        return item.getItemMeta().getPersistentDataContainer().get(LOCATION_KEY, LocationPersistentDataType.INSTANCE);
    }

    public PersistentData setLocation(Location location) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(LOCATION_KEY, LocationPersistentDataType.INSTANCE, location);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasPage() {
        return item.getItemMeta().getPersistentDataContainer().has(PAGE_KEY, PersistentDataType.INTEGER);
    }

    public int getPage() {
        Integer data = item.getItemMeta().getPersistentDataContainer().get(PAGE_KEY, PersistentDataType.INTEGER);
        return data == null ? -1 : data;
    }

    public PersistentData setPage(int page) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(PAGE_KEY, PersistentDataType.INTEGER, page);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasHunger() {
        return item.getItemMeta().getPersistentDataContainer().has(HUNGER_KEY, PersistentDataType.INTEGER);
    }

    public int getHunger() {
        Integer data = item.getItemMeta().getPersistentDataContainer().get(HUNGER_KEY, PersistentDataType.INTEGER);
        return data == null ? -1 : data;
    }

    public PersistentData setHunger(int hunger) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(HUNGER_KEY, PersistentDataType.INTEGER, hunger);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasExperience() {
        return item.getItemMeta().getPersistentDataContainer().has(EXPERIENCE_KEY, PersistentDataType.INTEGER);
    }

    public int getExperience() {
        Integer data = item.getItemMeta().getPersistentDataContainer().get(EXPERIENCE_KEY, PersistentDataType.INTEGER);
        return data == null ? -1 : data;
    }

    public PersistentData setExperience(int experience) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(EXPERIENCE_KEY, PersistentDataType.INTEGER, experience);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasTimestamp() {
        return item.getItemMeta().getPersistentDataContainer().has(TIMESTAMP_KEY, PersistentDataType.LONG);
    }

    public long getTimestamp() {
        Long data = item.getItemMeta().getPersistentDataContainer().get(TIMESTAMP_KEY, PersistentDataType.LONG);
        return data == null ? -1 : data;
    }

    public PersistentData setTimestamp(long timestamp) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(TIMESTAMP_KEY, PersistentDataType.LONG, timestamp);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasHealth() {
        return item.getItemMeta().getPersistentDataContainer().has(HEALTH_KEY, PersistentDataType.DOUBLE);
    }

    public double getHealth() {
        Double data = item.getItemMeta().getPersistentDataContainer().get(HEALTH_KEY, PersistentDataType.DOUBLE);
        return data == null ? -1 : data;
    }

    public PersistentData setHealth(double health) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(HEALTH_KEY, PersistentDataType.DOUBLE, health);
        item.setItemMeta(meta);
        return this;
    }

    public boolean hasSaturation() {
        return item.getItemMeta().getPersistentDataContainer().has(SATURATION_KEY, PersistentDataType.FLOAT);
    }

    public float getSaturation() {
        Float data = item.getItemMeta().getPersistentDataContainer().get(SATURATION_KEY, PersistentDataType.FLOAT);
        return data == null ? -1 : data;
    }

    public PersistentData setSaturation(float saturation) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(SATURATION_KEY, PersistentDataType.FLOAT, saturation);
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}

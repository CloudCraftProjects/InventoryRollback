package me.danjono.inventoryrollback.utils;
// Created by booky10 in InventoryRollback (15:23 05.07.21)

import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public final class ItemByteSerializer {

    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private ItemByteSerializer() {
    }

    public static String getItemsAsBase64(ItemStack[] objectArray) {
        byte[] bytes = ItemStack.serializeItemsAsBytes(objectArray);
        return ENCODER.encodeToString(bytes);
    }

    public static ItemStack[] getItemsFromBase64(String base64) {
        byte[] bytes = DECODER.decode(base64);
        return ItemStack.deserializeItemsFromBytes(bytes);
    }
}

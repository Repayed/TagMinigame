package me.repayed.tagminigame.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta itemMeta;

    public ItemBuilder(final Material material) {
        this.item = new ItemStack(material);
        this.itemMeta = this.item.getItemMeta();
    }

    public ItemBuilder withName(String name) {
        this.itemMeta.setDisplayName(Chat.format(name));
        return this;
    }

    public ItemBuilder withLore(String... lore) {
        List<String> fixedLore = new ArrayList<>();

        Arrays.stream(lore).forEach(line -> {
            Chat.format(line);
            fixedLore.add(line);
        });

        this.itemMeta.setLore(fixedLore);
        return this;
    }

    public ItemBuilder withHiddenEnchantment() {
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.itemMeta);
        return this.item;
    }

}

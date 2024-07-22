package com.njdge.fastbuilder;

import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
@Getter
public class FastBuilderItems {
    public static ItemBuilder block = new ItemBuilder(Material.SANDSTONE).amount(64);
    public static ItemBuilder pickaxe = new ItemBuilder(Material.WOOD_PICKAXE).unbreakable().enchantment(Enchantment.DIG_SPEED, 3);
    public static ItemBuilder replay = new ItemBuilder(Material.BOOK).name(CC.YELLOW + "Your " + CC.AQUA + "Replays").lore(CC.GRAY + "Access your latest replays\nand rewatch your best attempts or builds");
    public static ItemBuilder shop = new ItemBuilder(Material.EMERALD).name(CC.YELLOW + "Shop").lore(CC.GRAY + "Buy cosmetics and more");
}

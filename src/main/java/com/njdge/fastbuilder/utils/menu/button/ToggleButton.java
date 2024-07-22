package com.njdge.fastbuilder.utils.menu.button;

import com.njdge.fastbuilder.utils.ItemBuilder;
import com.njdge.fastbuilder.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ToggleButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(isEnabled(player) ? Material.REDSTONE_TORCH_ON : Material.LEVER)
                .name("&b&l" + getOptionName())
                .build();
    }

    public abstract String getOptionName();

    public abstract String getDescription();

    public abstract boolean isEnabled(Player player);

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        playNeutral(player);
        onClick(player, slot, clickType, hotbarSlot);
    }

    public abstract void onClick(Player player, int slot, ClickType clickType, int hotbarSlot);
}

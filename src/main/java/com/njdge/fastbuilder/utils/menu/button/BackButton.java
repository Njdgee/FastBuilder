package com.njdge.fastbuilder.utils.menu.button;

import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.ItemBuilder;
import com.njdge.fastbuilder.utils.menu.Button;
import com.njdge.fastbuilder.utils.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@AllArgsConstructor
public class BackButton extends Button {

	private final Material material;
	private int durability = 0;
	private final Menu back;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(material)
				.name("&c&lGo Back")
				.durability(durability)
				.lore("&7Click here to return to the last page.")
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		Button.playNeutral(player);
		back.openMenu(player);
	}

}

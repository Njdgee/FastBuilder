package com.njdge.fastbuilder.utils.menu.pagination;

import com.njdge.fastbuilder.utils.ItemBuilder;
import com.njdge.fastbuilder.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@AllArgsConstructor
public class PageButton extends Button {

	private int mod;
	private PaginatedMenu menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		if (this.mod > 0) {
			if (hasNext(player)) {
				return new ItemBuilder(Material.ARROW)
						.name("&aNext page")
						.lore("")
						.lore("&eClick here to jump to the next page")
						.build();
			} else {
				return new ItemBuilder(Material.ARROW)
						.name("&7Next page")
						.lore("")
						.lore("&eYou are already on the last page!")
						.build();
			}
		} else {
			if (hasPrevious(player)) {
				return new ItemBuilder(Material.ARROW)
						.name("&aPrevious page")
						.lore("")
						.lore("&eClick here to jump to the previous page")
						.build();
			} else {
				return new ItemBuilder(Material.ARROW)
						.name("&7Previous page")
						.lore("")
						.lore("&eYou are on the first page!")
						.build();
			}
		}
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		if (this.mod > 0) {
			if (hasNext(player)) {
				this.menu.modPage(player, this.mod);
				Button.playNeutral(player);
			} else {
				Button.playFail(player);
			}
		} else {
			if (hasPrevious(player)) {
				this.menu.modPage(player, this.mod);
				Button.playNeutral(player);
			} else {
				Button.playFail(player);
			}
		}
	}

	private boolean hasNext(Player player) {
		int pg = this.menu.getPage() + this.mod;
		return this.menu.getPages(player) >= pg;
	}

	private boolean hasPrevious(Player player) {
		int pg = this.menu.getPage() + this.mod;
		return pg > 0;
	}

}

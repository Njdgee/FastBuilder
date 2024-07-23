package com.njdge.fastbuilder.utils.menu.pagination;


import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.ItemBuilder;
import com.njdge.fastbuilder.utils.menu.Button;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class PageFilterButton<T> extends Button {

	private FilterablePaginatedMenu<T> menu;

	@Override
	public ItemStack getButtonItem(Player player) {
		if (menu.getFilters() == null || menu.getFilters().isEmpty()) {
			return new ItemStack(Material.AIR);
		}

		List<String> lore = new ArrayList<>();
		lore.add(CC.MENU_BAR);

		for (PageFilter filter : menu.getFilters()) {
			String color;
			String decoration = "";
			String icon;

			if (filter.isEnabled()) {
				color = ChatColor.GREEN.toString();
				icon = StringEscapeUtils.unescapeJava("\u2713");
			} else {
				color = ChatColor.RED.toString();
				icon = StringEscapeUtils.unescapeJava("\u2717");
			}

			if (menu.getFilters().get(menu.getScrollIndex()).equals(filter)) {
				decoration = ChatColor.YELLOW + StringEscapeUtils.unescapeJava("» ") + " ";
			}
			List<String> lore1 = new ArrayList<>();
			lore1.add(" ");

			lore.add(decoration + color + icon + " " + filter.getName());
		}

		lore.addAll(new ArrayList<String>() {{
			add(CC.MENU_BAR);
			add("");
			add("&eLeft click to change page filter");
			add("&eRight click to switch page filter status");
			add("");
		}});

		return new ItemBuilder(Material.HOPPER)
				.name("&bPage filter")
				.lore(lore)
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		if (menu.getFilters() == null || menu.getFilters().isEmpty()) {
			player.sendMessage(CC.translate("&cNo filter available"));
		} else {
			if (clickType == ClickType.LEFT) {
				if (menu.getScrollIndex() == menu.getFilters().size() - 1) {
					menu.setScrollIndex(0);
				} else {
					menu.setScrollIndex(menu.getScrollIndex() + 1);
				}
			} else if (clickType == ClickType.RIGHT) {
				PageFilter<T> filter = menu.getFilters().get(menu.getScrollIndex());
				filter.setEnabled(!filter.isEnabled());
			}
		}
	}

	@Override
	public boolean shouldUpdate(Player player, ClickType clickType) {
		return true;
	}

}

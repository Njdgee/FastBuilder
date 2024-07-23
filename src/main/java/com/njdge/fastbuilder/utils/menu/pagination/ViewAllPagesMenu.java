package com.njdge.fastbuilder.utils.menu.pagination;

import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.menu.Button;
import com.njdge.fastbuilder.utils.menu.Menu;
import com.njdge.fastbuilder.utils.menu.button.BackButton;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ViewAllPagesMenu extends Menu {

	@NonNull
	@Getter
	PaginatedMenu menu;

	@Override
	public String getTitle(Player player) {
		return CC.translate("&bView all pages");
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		HashMap<Integer, Button> buttons = new HashMap<>();

		buttons.put(0, new BackButton(Material.REDSTONE, menu));

		int index = 10;

		for (int i = 1; i <= menu.getPages(player); i++) {
			buttons.put(index++, new JumpToPageButton(i, menu, menu.getPage() == i));

			if ((index - 8) % 9 == 0) {
				index += 2;
			}
		}

		return buttons;
	}

	@Override
	public boolean isAutoUpdate() {
		return true;
	}

}

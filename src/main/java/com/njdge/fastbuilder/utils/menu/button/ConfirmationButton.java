package com.njdge.fastbuilder.utils.menu.button;

import com.njdge.fastbuilder.utils.ItemBuilder;
import com.njdge.fastbuilder.utils.menu.Button;
import com.njdge.fastbuilder.utils.menu.Menu;
import com.njdge.fastbuilder.utils.menu.TypeCallback;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ConfirmationButton extends Button {

	private boolean confirm;
	private TypeCallback<Boolean> callback;
	private boolean closeAfterResponse;

	@Override
	public ItemStack getButtonItem(Player player) {
		return new ItemBuilder(Material.STAINED_GLASS_PANE)
				.durability(this.confirm ? 5 : 14)
				.name(this.confirm ? "&aConfirm" : "&cCancel")
				.build();
	}

	@Override
	public void clicked(Player player, ClickType clickType) {
		if (this.confirm) {
			player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20f, 0.1f);
		} else {
			player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20f, 0.1F);
		}

		if (this.closeAfterResponse) {
			Menu menu = Menu.currentlyOpenedMenus.get(player.getUniqueId());

			if (menu != null) {
				menu.setClosedByMenu(true);
			}

			player.closeInventory();
		}

		this.callback.callback(this.confirm);
	}

}

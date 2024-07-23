package com.njdge.fastbuilder.utils.menu.menus;


import com.njdge.fastbuilder.utils.CC;
import com.njdge.fastbuilder.utils.menu.Button;
import com.njdge.fastbuilder.utils.menu.Menu;
import com.njdge.fastbuilder.utils.menu.TypeCallback;
import com.njdge.fastbuilder.utils.menu.button.ConfirmationButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ConfirmMenu extends Menu {

	private final String title;
	private final TypeCallback<Boolean> response;
	private final boolean closeAfterResponse;
	private final Button centerButtons;

	public ConfirmMenu(TypeCallback<Boolean> response, boolean closeAfter, Button centerButtons) {
		this.title = CC.YELLOW + "Confirm?";
		this.response = response;
		this.closeAfterResponse = closeAfter;
		this.centerButtons = centerButtons;
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		HashMap<Integer, Button> buttons = new HashMap<>();

		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				buttons.put(getSlot(x, y), new ConfirmationButton(true, response, closeAfterResponse));
				buttons.put(getSlot(8 - x, y), new ConfirmationButton(false, response, closeAfterResponse));
			}
		}

		if (centerButtons != null) {
			buttons.put(getSlot(4, 1), centerButtons);
		}

		return buttons;
	}

	@Override
	public String getTitle(Player player) {
		return title;
	}

}

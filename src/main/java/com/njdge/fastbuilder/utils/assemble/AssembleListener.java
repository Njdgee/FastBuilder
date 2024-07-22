package com.njdge.fastbuilder.utils.assemble;

import com.njdge.fastbuilder.utils.assemble.events.AssembleBoardCreateEvent;
import com.njdge.fastbuilder.utils.assemble.events.AssembleBoardDestroyEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class AssembleListener implements Listener {

	private final Assemble assemble;

	/**
	 * Assemble Listener.
	 *
	 * @param assemble instance.
	 */
	public AssembleListener(Assemble assemble) {
		this.assemble = assemble;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Call Events if enabled.
		if (assemble.isCallEvents()) {
			AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(event.getPlayer());

			Bukkit.getPluginManager().callEvent(createEvent);
			if (createEvent.isCancelled()) {
				return;
			}
		}

		getAssemble().getBoards().put(event.getPlayer().getUniqueId(), new AssembleBoard(event.getPlayer(), getAssemble()));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		// Call Events if enabled.
		if (assemble.isCallEvents()) {
			AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(event.getPlayer());

			Bukkit.getPluginManager().callEvent(destroyEvent);
			if (destroyEvent.isCancelled()) {
				return;
			}
		}

		getAssemble().getBoards().remove(event.getPlayer().getUniqueId());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

}

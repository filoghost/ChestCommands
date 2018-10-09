package com.gmail.filoghost.chestcommands.task;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.api.Icon;

public class ExecuteCommandsTask implements Runnable {

	private Player player;
	private Icon icon;
	

	public ExecuteCommandsTask(Player player, Icon icon) {
		this.player = player;
		this.icon = icon;
	}
	

	@Override
	public void run() {
		boolean close = icon.onClick(player);
		
		if (close) {
			player.closeInventory();
		}
	}


}

package com.gmail.filoghost.chestcommands.internal;

import java.util.List;

import org.bukkit.entity.Player;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class ExtendedIconMenu extends IconMenu {
	
	private String permission;
	private List<IconCommand> openActions;
	
	public ExtendedIconMenu(String title, int rows, String fileName) {
		super(title, rows);
		this.permission = Permissions.OPEN_MENU_BASE + fileName;
	}

	public List<IconCommand> getOpenActions() {
		return openActions;
	}

	public void setOpenActions(List<IconCommand> openAction) {
		this.openActions = openAction;
	}
	
	public String getPermission() {
		return permission;
	}

	@Override
	public void open(Player player) {
		if (openActions != null) {
			for (IconCommand openAction : openActions) {
				openAction.execute(player);
			}
		}
		
		super.open(player);
	}
	
	public void sendNoPermissionMessage(Player player) {
		String noPermMessage = ChestCommands.getLang().no_open_permission;
		if (noPermMessage != null && !noPermMessage.isEmpty()) {
			player.sendMessage(noPermMessage.replace("{permission}", this.permission));
		}
	}
	
	

}

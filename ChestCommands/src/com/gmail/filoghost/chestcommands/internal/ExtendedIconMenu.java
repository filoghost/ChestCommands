package com.gmail.filoghost.chestcommands.internal;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;

public class ExtendedIconMenu extends IconMenu {
	
	private String fileName;
	private String permission;
	private List<IconCommand> openActions;
	
	public ExtendedIconMenu(String title, int rows, String fileName) {
		super(title, rows);
		this.fileName = fileName;
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

	public String getFileName() {
		return fileName;
	}

	@Override
	public void open(Player player) {
		if (openActions != null) {
			for (IconCommand openAction : openActions) {
				openAction.execute(player);
			}
		}
		
			
		Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), icons.length, title);
			
		for (int i = 0; i < icons.length; i++) {
				if (icons[i] != null) {
					
					if (icons[i] instanceof ExtendedIcon) {
						ExtendedIcon extIcon = (ExtendedIcon) icons[i];
						
						if (!extIcon.canViewIcon(player)) {
							continue;
						}
					}
					
					inventory.setItem(i, ChestCommands.getAttributeRemover().removeAttributes(icons[i].createItemstack()));
				}
			}
			
		player.openInventory(inventory);
	}
	
	public void sendNoPermissionMessage(CommandSender sender) {
		String noPermMessage = ChestCommands.getLang().no_open_permission;
		if (noPermMessage != null && !noPermMessage.isEmpty()) {
			sender.sendMessage(noPermMessage.replace("{permission}", this.permission));
		}
	}
	
	

}

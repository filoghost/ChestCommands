package com.gmail.filoghost.chestcommands.internal;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.nms.AttributeRemover;

public class ExtendedIconMenu extends IconMenu {
	
	private String fileName;
	private String permission;
	private List<IconCommand> openActions;
	
	private int refreshTicks;
	
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

	public int getRefreshTicks() {
		return refreshTicks;
	}

	public void setRefreshTicks(int refreshTicks) {
		this.refreshTicks = refreshTicks;
	}

	@Override
	public void open(Player player) {
		try {
			if (openActions != null) {
				for (IconCommand openAction : openActions) {
					openAction.execute(player);
				}
			}
			
			Inventory inventory = Bukkit.createInventory(new MenuInventoryHolder(this), icons.length, title);
	
			for (int i = 0; i < icons.length; i++) {
				if (icons[i] != null) {
						
					if (icons[i] instanceof ExtendedIcon && !((ExtendedIcon) icons[i]).canViewIcon(player)) {
						continue;
					}
						
					inventory.setItem(i, AttributeRemover.hideAttributes(icons[i].createItemstack(player)));
				}
			}
		
			player.openInventory(inventory);
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An internal error occurred while opening the menu. The staff should check the console for errors.");
		}
	}
	
	public void refresh(Player player, Inventory inventory) {
		try {
			for (int i = 0; i < icons.length; i++) {
				if (icons[i] != null && icons[i] instanceof ExtendedIcon) {
					ExtendedIcon extIcon = (ExtendedIcon) icons[i];
						
					if (extIcon.hasViewPermission() || extIcon.hasVariables()) {
						// Then we have to refresh it
						if (extIcon.canViewIcon(player)) {
							
							if (inventory.getItem(i) == null) {
								ItemStack newItem = AttributeRemover.hideAttributes(extIcon.createItemstack(player));
								inventory.setItem(i, newItem);
							} else {
								// Performance, only update name and lore.
								ItemStack oldItem = AttributeRemover.hideAttributes(inventory.getItem(i));
								ItemMeta meta = oldItem.getItemMeta();
								meta.setDisplayName(extIcon.calculateName(player));
								meta.setLore(extIcon.calculateLore(player));
								oldItem.setItemMeta(meta);
							}
							
						} else {
							inventory.setItem(i, null);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			player.sendMessage(ChatColor.RED + "An internal error occurred while refreshing the menu. The staff should check the console for errors.");
		}
	}
	
	
	public void sendNoPermissionMessage(CommandSender sender) {
		String noPermMessage = ChestCommands.getLang().no_open_permission;
		if (noPermMessage != null && !noPermMessage.isEmpty()) {
			sender.sendMessage(noPermMessage.replace("{permission}", this.permission));
		}
	}

}

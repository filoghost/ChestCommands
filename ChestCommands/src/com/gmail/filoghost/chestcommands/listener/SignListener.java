package com.gmail.filoghost.chestcommands.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.filoghost.chestcommands.ChestCommands;
import com.gmail.filoghost.chestcommands.Permissions;
import com.gmail.filoghost.chestcommands.api.IconMenu;
import com.gmail.filoghost.chestcommands.internal.ExtendedIconMenu;
import com.gmail.filoghost.chestcommands.util.Utils;

public class SignListener implements Listener {	

	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onInteract(PlayerInteractEvent event) {
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && isSign(event.getClickedBlock().getType())) {
			
			Sign sign = (Sign) event.getClickedBlock().getState();
			if (sign.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[menu]")) {
				
				sign.getLine(1);
				ExtendedIconMenu iconMenu = ChestCommands.getFileNameToMenuMap().get(Utils.addYamlExtension(sign.getLine(1)));
				if (iconMenu != null) {
					
					if (event.getPlayer().hasPermission(iconMenu.getPermission())) {
						iconMenu.open(event.getPlayer());
					} else {
						iconMenu.sendNoPermissionMessage(event.getPlayer());
					}
					
				} else {
					sign.setLine(0, ChatColor.RED + ChatColor.stripColor(sign.getLine(0)));
					event.getPlayer().sendMessage(ChestCommands.getLang().menu_not_found);
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equalsIgnoreCase("[menu]") && event.getPlayer().hasPermission(Permissions.SIGN_CREATE)) {
				
			if (event.getLine(1).isEmpty()) {
				event.setLine(0, ChatColor.RED + event.getLine(0));
				event.getPlayer().sendMessage(ChatColor.RED + "You must set a valid menu name in the second line.");
				return;
			}
				
			IconMenu iconMenu = ChestCommands.getFileNameToMenuMap().get(Utils.addYamlExtension(event.getLine(1)));
			if (iconMenu == null) {
				event.setLine(0, ChatColor.RED + event.getLine(0));
				event.getPlayer().sendMessage(ChatColor.RED + "That menu was not found.");
				return;
			}
				
			event.setLine(0, ChatColor.DARK_BLUE + event.getLine(0));
			event.getPlayer().sendMessage(ChatColor.GREEN + "Successfully created a sign for the menu " + Utils.addYamlExtension(event.getLine(1)) + ".");
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSignChangeMonitor(SignChangeEvent event) {
		// Prevent players with permissions for creating colored signs from creating menu signs.
		if (event.getLine(0).equalsIgnoreCase(ChatColor.DARK_BLUE + "[menu]") && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE)) {
			event.setLine(0, ChatColor.stripColor(event.getLine(0)));
		}
	}
	
	private boolean isSign(Material material) {
		return material == Material.WALL_SIGN || material == Material.SIGN_POST;
	}
	
}

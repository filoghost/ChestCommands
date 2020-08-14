/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon.requirement;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.commons.Strings;
import org.bukkit.entity.Player;

public class RequiredPermission implements Requirement {
	
	private final String permission;
	private final boolean negated;
	private String noPermissionMessage;
	
	public RequiredPermission(String permission) {
		if (permission != null) {
			permission = permission.trim();
		}
		
		if (Strings.isEmpty(permission)) {
			this.permission = null;
			negated = false;
		} else {
			if (permission.startsWith("-")) {
				this.permission = permission.substring(1);
				negated = true;
			} else {
				this.permission = permission;
				negated = false;
			}
		}
	}
	
	
	public void setNoPermissionMessage(String noPermissionMessage) {
		this.noPermissionMessage = noPermissionMessage;
	}


	@Override
	public boolean hasCost(Player player) {
		if (hasPermission(player)) {
			return true;
		} else {
			if (noPermissionMessage != null) {
				player.sendMessage(noPermissionMessage);
			} else {
				player.sendMessage(ChestCommands.getLang().default_no_icon_permission);
			}
			return false;
		}
	}
	
	public boolean hasPermission(Player player) {
		if (isEmpty()) {
			return true;
		}
		
		if (negated) {
			return !player.hasPermission(permission);
		} else {
			return player.hasPermission(permission);
		}
	}
	
	@Override
	public boolean takeCost(Player player) {
		return true;
	}

	public boolean isEmpty() {
		return this.permission == null;
	}	

}

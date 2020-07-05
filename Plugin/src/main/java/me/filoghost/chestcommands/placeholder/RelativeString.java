package me.filoghost.chestcommands.placeholder;

import org.bukkit.entity.Player;

public class RelativeString {

	private final String string;
	private final boolean hasPlaceholders;

	public static RelativeString of(String string) {
		if (string != null) {
			return new RelativeString(string);
		} else {
			return null;
		}
	}
	
	private RelativeString(String string) {
		this.string = string;
		this.hasPlaceholders = PlaceholderManager.hasPlaceholders(string);
	}
	
	public String getRawValue() {
		return string;
	}
	
	public String getValue(Player player) {
		if (hasPlaceholders) {
			return PlaceholderManager.replacePlaceholders(string, player);
		} else {
			return string;
		}
	}
	
	public boolean hasPlaceholders() {
		return hasPlaceholders;
	}

}

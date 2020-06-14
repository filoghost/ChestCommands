package me.filoghost.chestcommands.variable;

import org.bukkit.entity.Player;

public class RelativeString {

	private final String string;
	private final boolean hasVariables;

	public static RelativeString of(String string) {
		if (string != null) {
			return new RelativeString(string);
		} else {
			return null;
		}
	}
	
	private RelativeString(String string) {
		this.string = string;
		this.hasVariables = VariableManager.hasVariables(string);
	}
	
	public String getRawValue() {
		return string;
	}
	
	public String getValue(Player player) {
		if (hasVariables) {
			return VariableManager.setVariables(string, player);
		} else {
			return string;
		}
	}
	
	public boolean hasVariables() {
		return hasVariables;
	}

}

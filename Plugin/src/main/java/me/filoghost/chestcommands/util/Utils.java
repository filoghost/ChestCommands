package me.filoghost.chestcommands.util;

import org.bukkit.Material;

public class Utils {

	public static boolean isClassLoaded(String name) {
		try {
			Class.forName(name);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	public static String formatEnum(Enum<?> enumValue) {
		return Strings.capitalizeFully(enumValue.name().replace("_", " "));
	}

}

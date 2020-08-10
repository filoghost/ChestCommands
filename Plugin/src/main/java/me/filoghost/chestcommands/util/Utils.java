package me.filoghost.chestcommands.util;

import me.filoghost.commons.Strings;

public class Utils {

	public static String formatEnum(Enum<?> enumValue) {
		return Strings.capitalizeFully(enumValue.name().replace("_", " "));
	}

	public static String addYamlExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		if (fileName.toLowerCase().endsWith(".yml")) {
			return fileName;
		} else {
			return fileName + ".yml";
		}
	}

}

package me.filoghost.chestcommands.util;

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

package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.parsing.icon.IconSettings;

public class ErrorFormat {

	public static String invalidMenuSetting(String menuName, String invalidSetting, String errorMessage) {
		return menuError(menuName, "has an invalid menu setting \"" + invalidSetting + "\": " + errorMessage);
	}

	public static String missingMenuSetting(String menuName, String missingSetting) {
		return menuError(menuName, "is missing the menu setting \"" + missingSetting + "\"");
	}

	private static String menuError(String menuName, String errorMessage) {
		return "The menu \"" + menuName + "\" " + errorMessage + ".";
	}

	public static String invalidAttribute(IconSettings iconSettings, String attributeName, String errorMessage) {
		return iconError(iconSettings, "has an invalid attribute \"" + attributeName + "\": " + errorMessage);
	}

	public static String missingAttribute(IconSettings iconSettings, String attributeName) {
		return iconError(iconSettings, "is missing the attribute \"" + attributeName + "\"");
	}

	public static String invalidListElement(IconSettings iconSettings, String attributeName, String listElement, String errorMessage) {
		return iconError(iconSettings,
				"contains an invalid list element (\"" + listElement + "\") "
				+ "in the attribute \"" + attributeName + "\": " + errorMessage);
	}

	public static String iconError(IconSettings iconSettings, String errorMessage) {
		return "The icon \"" + iconSettings.getIconName() + "\" "
				+ "in the menu \"" + iconSettings.getMenuName() + "\" "
				+ errorMessage + ".";
	}
}

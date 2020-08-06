/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.logging;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.config.framework.mapped.MappedConfig;
import me.filoghost.chestcommands.config.framework.mapped.MappedField;
import me.filoghost.chestcommands.config.framework.mapped.converter.Converter;
import me.filoghost.chestcommands.parsing.icon.AttributeType;
import me.filoghost.chestcommands.parsing.icon.IconSettings;

import java.nio.file.Path;

public class ErrorMessages {

	public static class Config {

		public static final String readIOException = "I/O exception while reading file";
		public static final String createDefaultIOException = "I/O exception while creating default file";
		public static final String writeDataIOException = "I/O exception while writing data to file";
		public static final String createDataFolderIOException = "plugin failed to load, couldn't create data folder";
		public static final String invalidYamlSyntax = "invalid YAML syntax";

		public static final String valueNotSet = "value is not set";
		public static final String valueNotList = "value is not a list";
		public static final String valueNotBoolean = "value is not a boolean";
		public static final String valueNotNumber = "value is not a number";
		public static final String valueNotString = "value is not a string";
		public static final String valueNotSection = "value is not a configuration section";

		public static String menuListIOException(Path menuFolder) {
			return "couldn't fetch menu files inside the folder \"" + menuFolder + "\"";
		}

		public static String initException(Path file) {
			return "error while initializing config file \"" + formatPath(file) + "\"";
		}

		public static String emptyPlaceholder(Path configFile) {
			return "error in \"" + configFile + "\": placeholder cannot be empty (skipped).";
		}

		public static String tooLongPlaceholder(Path configFile, String placeholder) {
			return "error in \"" + configFile + "\": placeholder cannot be longer than 100 character (" + placeholder + ").";
		}

		public static String createParentFolderIOException(Path folder) {
			return "I/O exception while creating parent directory \"" + formatPath(folder) + "\"";
		}

		public static String mapperInitError(MappedConfig mappedConfig) {
			return "couldn't initialize config mapper for class \"" + mappedConfig.getClass() + "\"";
		}

		public static String fieldReadError(MappedConfig mappedConfig) {
			return "couldn't read field values from class \"" + mappedConfig.getClass() + "\"";
		}

		public static String fieldInjectError(MappedConfig mappedConfig) {
			return "couldn't inject fields values in class \"" + mappedConfig.getClass() + "\"";
		}

		public static String mapperFieldCannotBeNull(MappedField mappedField) {
			return "mapped field \"" + mappedField.getFieldName() + "\" cannot be null by default";
		}

		public static String converterFailed(Object value, Converter converter) {
			return "value of type \"" + value.getClass() + "\" couldn't be converted by \"" + converter.getClass() + "\"";
		}

	}


	public static class Upgrade {

		public static final String genericExecutorError = "error while running automatic configuration upgrades";
		public static final String menuListIOException = "couldn't obtain a list of menu files";
		public static final String failedSomeUpgrades = "note: one or more automatic upgrades may have not been applied, configuration files or menus may require manual changes";
		public static final String failedToPrepareUpgradeTasks = "error while trying to prepare an automatic configuration upgrade";

		public static String metadataReadError(Path metadataFile) {
			return "couldn't read upgrades metadata file \"" + formatPath(metadataFile) + "\"";
		}

		public static String metadataSaveError(Path metadataFile) {
			return "couldn't save upgrades metadata file \"" + formatPath(metadataFile) + "\"";
		}

		public static String failedSingleUpgrade(Path file) {
			return "error while trying to automatically upgrade \"" + formatPath(file) + "\"";
		}

		public static String loadError(Path file) {
			return "couldn't load file to upgrade \"" + formatPath(file) + "\"";
		}

		public static String backupError(Path file) {
			return "couldn't create backup of file \"" + formatPath(file) + "\"";
		}

		public static String saveError(Path file) {
			return "couldn't save upgraded file \"" + formatPath(file) + "\"";
		}

	}


	public static class Parsing {

		public static final String invalidDouble = "value is not a valid decimal";
		public static final String invalidShort = "value is not a valid short integer";
		public static final String invalidInteger = "value not a valid integer";

		public static final String strictlyPositive = "value must be greater than zero";
		public static final String zeroOrPositive = "value must be zero or greater";

		public static final String invalidColorFormat = "value must match the format \"red, green, blue\"";
		public static final String invalidPatternFormat = "value must match the format \"pattern:color\"";

		public static final String unknownAttribute = "unknown attribute";

		public static String unknownMaterial(String materialString) {
			return "unknown material \"" + materialString + "\"";
		}

		public static String unknownPatternType(String patternTypeString) {
			return "unknown pattern type \"" + patternTypeString + "\"";
		}

		public static String unknownDyeColor(String dyeColorString) {
			return "unknown dye color \"" + dyeColorString + "\"";
		}

		public static String unknownEnchantmentType(String typeString) {
			return "unknown enchantment type \"" + typeString + "\"";
		}

		public static String invalidEnchantmentLevel(String levelString) {
			return "invalid enchantment level \"" + levelString + "\",";
		}

		public static String invalidDurability(String durabilityString) {
			return "invalid durability \"" + durabilityString + "\"";
		}

		public static String invalidAmount(String amountString) {
			return "invalid amount \"" + amountString + "\"";
		}

		public static String invalidColorNumber(String numberString, String colorName) {
			return "invalid " + colorName + " color \"" + numberString + "\"";
		}

		public static String invalidColorRange(String valueString, String colorName) {
			return "invalid " + colorName + " color \"" + valueString + "\", value must be between 0 and 255";
		}

	}

	public static class Menu {

		public static String invalidSetting(Path menuFile, String invalidSetting) {
			return menuError(menuFile, "has an invalid menu setting \"" + invalidSetting + "\"");
		}

		public static String missingSetting(Path menuFile, String missingSetting) {
			return menuError(menuFile, "is missing the menu setting \"" + missingSetting + "\"");
		}

		public static String missingSettingsSection(Path menuFile) {
			return menuError(menuFile, "is missing the menu setting section");
		}

		private static String menuError(Path menuFile, String errorMessage) {
			return "the menu \"" + formatPath(menuFile) + "\" " + errorMessage;
		}

		public static String invalidAttribute(IconSettings iconSettings, AttributeType attributeType) {
			return invalidAttribute(iconSettings, attributeType.getAttributeName());
		}

		public static String invalidAttribute(IconSettings iconSettings, String attributeName) {
			return iconError(iconSettings, "has an invalid attribute \"" + attributeName + "\"");
		}

		public static String missingAttribute(IconSettings iconSettings, AttributeType attributeType) {
			return iconError(iconSettings, "is missing the attribute \"" + attributeType.getAttributeName() + "\"");
		}

		public static String invalidAttributeListElement(IconSettings iconSettings, String attributeName, String listElement) {
			return iconError(iconSettings,
					"contains an invalid list element (\"" + listElement + "\") "
					+ "in the attribute \"" + attributeName + "\"");
		}

		public static String iconOverridesAnother(IconSettings iconSettings) {
			return iconError(iconSettings, "is overriding another icon with the same position");
		}

		private static String iconError(IconSettings iconSettings, String errorMessage) {
			return "the icon \"" + iconSettings.getIconName() + "\" in the menu \""
					+ formatPath(iconSettings.getMenuFile()) + "\" " + errorMessage;
		}

		public static String duplicateMenuName(Path menuFile1, Path menuFile2) {
			return "two menus (\"" + menuFile1 + "\" and \"" + menuFile2 + "\") "
					+ "have the same file name. Only of them will work when referenced by name";
		}

		public static String duplicateMenuCommand(Path menuFile1, Path menuFile2, String command) {
			return "two menus (\"" + menuFile1 + "\" and \"" + menuFile2 + "\") "
					+ "have the same command \"" + command + "\". Only one will be opened when the command is executed";
		}
	}


	private static String formatPath(Path path) {
		if (path.startsWith(ChestCommands.getDataFolderPath())) {
			// Remove "/plugins/ChestCommands" prefix
			return path.subpath(ChestCommands.getDataFolderPath().getNameCount(), path.getNameCount()).toString();
		} else {
			return path.toString();
		}
	}

}
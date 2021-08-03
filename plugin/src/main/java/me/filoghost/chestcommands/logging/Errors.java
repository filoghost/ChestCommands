/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.logging;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.parsing.icon.AttributeType;
import me.filoghost.chestcommands.parsing.icon.IconSettings;
import me.filoghost.fcommons.config.ConfigErrors;
import me.filoghost.fcommons.config.ConfigPath;
import org.bukkit.ChatColor;

import java.nio.file.Path;

public class Errors {

    public static class Config {

        public static final String createDataFolderIOException = "plugin failed to load, couldn't create data folder";

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

    }


    public static class Upgrade {

        public static final String genericExecutorError = "error while running automatic configuration upgrades";
        public static final String menuListIOException = "couldn't obtain a list of menu files";
        public static final String failedSomeUpgrades =
                "note: one or more automatic upgrades may have not been applied, "
                + "configuration files or menus may require manual changes";
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

        public static final String invalidDecimal = "value is not a valid decimal";
        public static final String invalidShort = "value is not a valid short integer";
        public static final String invalidInteger = "value is not a valid integer";

        public static final String strictlyPositive = "value must be greater than zero";
        public static final String zeroOrPositive = "value must be zero or greater";

        public static final String invalidColorFormat = "value must match the format \"red, green, blue\"";
        public static final String invalidPatternFormat = "value must match the format \"pattern:color\"";

        public static final String unknownAttribute = "unknown attribute";
        public static final String materialCannotBeAir = "material cannot be air";

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

        public static String invalidBossBarTime(String timeString) {
            return "invalid dragon bar time \"" + timeString + "\"";
        }

        public static String invalidSoundPitch(String pitchString) {
            return "invalid sound pitch \"" + pitchString + "\"";
        }

        public static String invalidSoundVolume(String volumeString) {
            return "invalid sound volume \"" + volumeString + "\"";
        }

        public static String unknownSound(String soundString) {
            return "unknown sound \"" + soundString + "\"";
        }

    }

    public static class Menu {

        public static String invalidSetting(Path menuFile, ConfigPath invalidSetting) {
            return menuError(menuFile, "has an invalid menu setting \"" + invalidSetting + "\"");
        }

        public static String missingSetting(Path menuFile, ConfigPath missingSetting) {
            return menuError(menuFile, "is missing the menu setting \"" + missingSetting + "\"");
        }

        public static String missingSettingsSection(Path menuFile) {
            return menuError(menuFile, "is missing the menu setting section");
        }

        public static String invalidSettingListElement(Path menuFile, ConfigPath invalidSetting, String listElement) {
            return menuError(menuFile,
                    "contains an invalid list element (\"" + listElement + "\") "
                            + "in the menu setting \"" + invalidSetting + "\"");
        }

        private static String menuError(Path menuFile, String errorMessage) {
            return "the menu \"" + formatPath(menuFile) + "\" " + errorMessage;
        }

        public static String invalidAttribute(IconSettings iconSettings, AttributeType attributeType) {
            return invalidAttribute(iconSettings, attributeType.getConfigKey());
        }

        public static String invalidAttribute(IconSettings iconSettings, ConfigPath attributeConfigKey) {
            return iconError(iconSettings, "has an invalid attribute \"" + attributeConfigKey + "\"");
        }

        public static String missingAttribute(IconSettings iconSettings, AttributeType attributeType) {
            return iconError(iconSettings, "is missing the attribute \"" + attributeType.getConfigKey() + "\"");
        }

        public static String invalidAttributeListElement(IconSettings iconSettings, ConfigPath attributeConfigKey, String listElement) {
            return iconError(iconSettings,
                    "contains an invalid list element (\"" + listElement + "\") "
                    + "in the attribute \"" + attributeConfigKey + "\"");
        }

        public static String iconOverridesAnother(IconSettings iconSettings) {
            return iconError(iconSettings, "is overriding another icon with the same position");
        }

        private static String iconError(IconSettings iconSettings, String errorMessage) {
            return "the icon \"" + iconSettings.getConfigPath() + "\" in the menu \""
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

    public static class User {

        public static final String notifyStaffRequest = "Please inform the staff.";

        public static String configurationError(String errorMessage) {
            return ChatColor.RED + "Error: " + errorMessage + ". " + Errors.User.notifyStaffRequest;
        }

    }

    public static String formatPath(Path path) {
        return ConfigErrors.formatPath(ChestCommands.getDataFolderPath(), path);
    }

}

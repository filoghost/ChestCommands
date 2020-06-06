/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.api.Icon;
import me.filoghost.chestcommands.config.AsciiPlaceholders;
import me.filoghost.chestcommands.config.ConfigUtil;
import me.filoghost.chestcommands.exception.FormatException;
import me.filoghost.chestcommands.internal.CommandsClickHandler;
import me.filoghost.chestcommands.internal.RequiredItem;
import me.filoghost.chestcommands.internal.icon.ExtendedIcon;
import me.filoghost.chestcommands.internal.icon.IconCommand;
import me.filoghost.chestcommands.serializer.EnchantmentSerializer.EnchantmentDetails;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.FormatUtils;
import me.filoghost.chestcommands.util.ItemStackReader;
import me.filoghost.chestcommands.util.ItemUtils;
import me.filoghost.chestcommands.util.Validate;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParseException;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParser;

public class IconSerializer {

	private static class Nodes {

		public static final String[] MATERIAL = {"MATERIAL", "ID"};
		public static final String AMOUNT = "AMOUNT";
		public static final String[] DURABILITY = {"DURABILITY", "DATA-VALUE"};
		public static final String[] NBT_DATA = {"NBT-DATA", "NBT"};
		public static final String NAME = "NAME";
		public static final String LORE = "LORE";
		public static final String[] ENCHANTMENTS = {"ENCHANTMENTS", "ENCHANTMENT"};
		public static final String COLOR = "COLOR";
		public static final String SKULL_OWNER = "SKULL-OWNER";
		public static final String BANNER_COLOR = "BANNER-COLOR";
		public static final String BANNER_PATTERNS = "BANNER-PATTERNS";
		public static final String[] ACTIONS = {"ACTIONS", "COMMAND"};
		public static final String PRICE = "PRICE";
		public static final String EXP_LEVELS = "LEVELS";
		public static final String[] REQUIRED_ITEMS = {"REQUIRED-ITEMS", "REQUIRED-ITEM"};
		public static final String PERMISSION = "PERMISSION";
		public static final String PERMISSION_MESSAGE = "PERMISSION-MESSAGE";
		public static final String VIEW_PERMISSION = "VIEW-PERMISSION";
		public static final String KEEP_OPEN = "KEEP-OPEN";
		public static final String POSITION_X = "POSITION-X";
		public static final String POSITION_Y = "POSITION-Y";
	}

	public static class Coords {

		private Integer x, y;

		protected Coords(Integer x, Integer y) {
			this.x = x;
			this.y = y;
		}

		public boolean isSetX() {
			return x != null;
		}

		public boolean isSetY() {
			return y != null;
		}

		public Integer getX() {
			return x;
		}

		public Integer getY() {
			return y;
		}
	}


	public static Icon loadIconFromSection(ConfigurationSection section, String iconName, String menuFileName, ErrorCollector errorCollector) {
		Validate.notNull(section, "ConfigurationSection cannot be null");

		// The icon is valid even without a Material
		ExtendedIcon icon = new ExtendedIcon();

		String material = ConfigUtil.getAnyString(section, Nodes.MATERIAL);
		if (material != null) {
			try {
				ItemStackReader itemReader = new ItemStackReader(material, true);
				icon.setMaterial(itemReader.getMaterial());
				icon.setDataValue(itemReader.getDataValue());
				icon.setAmount(itemReader.getAmount());
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid ID: " + e.getMessage());
			}
		}

		if (section.isSet(Nodes.AMOUNT)) {
			icon.setAmount(section.getInt(Nodes.AMOUNT));
		}
		
		Integer durability = ConfigUtil.getAnyInt(section, Nodes.DURABILITY);
		if (durability != null) {
			icon.setDataValue(durability.shortValue());
		}

		String nbtData = ConfigUtil.getAnyString(section, Nodes.NBT_DATA);
		if (nbtData != null) {
			try {
				// Check that NBT has valid syntax before applying it to the icon
				MojangsonParser.parse(nbtData);
				icon.setNBTData(nbtData);
			} catch (MojangsonParseException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid NBT-DATA: " + e.getMessage());
			}
		}

		icon.setName(AsciiPlaceholders.placeholdersToSymbols(FormatUtils.colorizeName(section.getString(Nodes.NAME))));
		icon.setLore(AsciiPlaceholders.placeholdersToSymbols(FormatUtils.colorizeLore(section.getStringList(Nodes.LORE))));

		List<String> serializedEnchantments = ConfigUtil.getStringListOrInlineList(section, ";", Nodes.ENCHANTMENTS);
		
		if (serializedEnchantments != null && !serializedEnchantments.isEmpty()) {
			Map<Enchantment, Integer> enchantments = new HashMap<>();
			
			for (String serializedEnchantment : serializedEnchantments) {
				if (serializedEnchantment != null && !serializedEnchantment.isEmpty()) {
					EnchantmentDetails enchantment = EnchantmentSerializer.parseEnchantment(serializedEnchantment, iconName, menuFileName, errorCollector);
					if (enchantment != null) {
						enchantments.put(enchantment.getEnchantment(), enchantment.getLevel());
					}
				}
			}
			
			if (!enchantments.isEmpty()) {
				icon.setEnchantments(enchantments);
			}
		}

		if (section.isSet(Nodes.COLOR)) {
			try {
				icon.setColor(ItemUtils.parseColor(section.getString(Nodes.COLOR)));
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid COLOR: " + e.getMessage());
			}
		}

		icon.setSkullOwner(section.getString(Nodes.SKULL_OWNER));

		String bannerColor = ConfigUtil.getAnyString(section, Nodes.BANNER_COLOR);
		if (bannerColor != null) {
			try {
				icon.setBannerColor(ItemUtils.parseDyeColor(bannerColor));
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid BANNER-COLOR: " + e.getMessage());
			}
		}

		if (section.isSet(Nodes.BANNER_PATTERNS)) {
			try {
				icon.setBannerPatterns(ItemUtils.parseBannerPatternList(section.getStringList(Nodes.BANNER_PATTERNS)));
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid BANNER-PATTERNS: " + e.getMessage());
			}
		}

		icon.setPermission(section.getString(Nodes.PERMISSION));
		icon.setPermissionMessage(FormatUtils.addColors(section.getString(Nodes.PERMISSION_MESSAGE)));
		icon.setViewPermission(section.getString(Nodes.VIEW_PERMISSION));

		boolean closeOnClick = !section.getBoolean(Nodes.KEEP_OPEN);
		icon.setCloseOnClick(closeOnClick);

		List<String> serializedCommands = ConfigUtil.getStringListOrInlineList(section, ChestCommands.getSettings().multiple_commands_separator, Nodes.ACTIONS);
		
		if (serializedCommands != null && !serializedCommands.isEmpty()) {
			List<IconCommand> commands = new ArrayList<>();
			
			for (String serializedCommand : serializedCommands) {
				if (serializedCommand != null && !serializedCommand.isEmpty()) {
					commands.add(CommandSerializer.matchCommand(serializedCommand));
				}
			}

			if (!commands.isEmpty()) {
				icon.setClickHandler(new CommandsClickHandler(commands, closeOnClick));
			}
		}

		double price = section.getDouble(Nodes.PRICE);
		if (price > 0.0) {
			icon.setMoneyPrice(price);
		} else if (price < 0.0) {
			errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has a negative PRICE: " + price);
		}

		int levels = section.getInt(Nodes.EXP_LEVELS);
		if (levels > 0) {
			icon.setExpLevelsPrice(levels);
		} else if (levels < 0) {
			errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has negative LEVELS: " + levels);
		}

		List<String> serializedRequiredItems = ConfigUtil.getStringListOrSingle(section, Nodes.REQUIRED_ITEMS);
		
		if (serializedRequiredItems != null && !serializedRequiredItems.isEmpty()) {
			List<RequiredItem> requiredItems = new ArrayList<>();
			
			for (String serializedItem : serializedRequiredItems) {
				try {
					ItemStackReader itemReader = new ItemStackReader(serializedItem, true);
					RequiredItem requiredItem = new RequiredItem(itemReader.getMaterial(), itemReader.getAmount());
					if (itemReader.hasExplicitDataValue()) {
						requiredItem.setRestrictiveDataValue(itemReader.getDataValue());
					}
					requiredItems.add(requiredItem);
				} catch (FormatException e) {
					errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has invalid REQUIRED-ITEMS: " + e.getMessage());
				}
			}
			
			if (!requiredItems.isEmpty()) {
				icon.setRequiredItems(requiredItems);
			}
		}

		return icon;
	}


	public static Coords loadCoordsFromSection(ConfigurationSection section) {
		Validate.notNull(section, "ConfigurationSection cannot be null");

		Integer x = null;
		Integer y = null;

		if (section.isInt(Nodes.POSITION_X)) {
			x = section.getInt(Nodes.POSITION_X);
		}

		if (section.isInt(Nodes.POSITION_Y)) {
			y = section.getInt(Nodes.POSITION_Y);
		}

		return new Coords(x, y);
	}

}

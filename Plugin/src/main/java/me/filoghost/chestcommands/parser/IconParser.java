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
package me.filoghost.chestcommands.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import me.filoghost.chestcommands.ChestCommands;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.config.AsciiPlaceholders;
import me.filoghost.chestcommands.config.ConfigUtil;
import me.filoghost.chestcommands.menu.AdvancedIcon;
import me.filoghost.chestcommands.menu.RequiredItem;
import me.filoghost.chestcommands.parser.EnchantmentParser.EnchantmentDetails;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.FormatUtils;
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParseException;
import me.filoghost.chestcommands.util.nbt.parser.MojangsonParser;

public class IconParser {

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


	public static AdvancedIcon loadIconFromSection(ConfigurationSection section, String iconName, String menuFileName, ErrorCollector errorCollector) {
		Preconditions.notNull(section, "section");

		// The icon is valid even without a Material
		AdvancedIcon icon = new AdvancedIcon();

		String material = ConfigUtil.getAnyString(section, Nodes.MATERIAL);
		if (material != null) {
			try {
				ItemStackParser itemReader = new ItemStackParser(material, true);
				icon.setMaterial(itemReader.getMaterial());
				icon.setDurability(itemReader.getDurability());
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
			icon.setDurability(durability.shortValue());
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
					EnchantmentDetails enchantment = EnchantmentParser.parseEnchantment(serializedEnchantment, iconName, menuFileName, errorCollector);
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
				icon.setLeatherColor(ItemMetaParser.parseColor(section.getString(Nodes.COLOR)));
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid COLOR: " + e.getMessage());
			}
		}

		icon.setSkullOwner(section.getString(Nodes.SKULL_OWNER));

		String bannerColor = ConfigUtil.getAnyString(section, Nodes.BANNER_COLOR);
		if (bannerColor != null) {
			try {
				icon.setBannerColor(ItemMetaParser.parseDyeColor(bannerColor));
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid BANNER-COLOR: " + e.getMessage());
			}
		}

		if (section.isSet(Nodes.BANNER_PATTERNS)) {
			try {
				icon.setBannerPatterns(ItemMetaParser.parseBannerPatternList(section.getStringList(Nodes.BANNER_PATTERNS)));
			} catch (FormatException e) {
				errorCollector.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid BANNER-PATTERNS: " + e.getMessage());
			}
		}

		icon.setPermission(section.getString(Nodes.PERMISSION));
		icon.setPermissionMessage(FormatUtils.addColors(section.getString(Nodes.PERMISSION_MESSAGE)));
		icon.setViewPermission(section.getString(Nodes.VIEW_PERMISSION));

		boolean closeOnClick = !section.getBoolean(Nodes.KEEP_OPEN);
		icon.setCloseOnClick(closeOnClick);

		List<String> serializedActions = ConfigUtil.getStringListOrInlineList(section, ChestCommands.getSettings().multiple_commands_separator, Nodes.ACTIONS);
		
		if (serializedActions != null && !serializedActions.isEmpty()) {
			List<Action> actions = new ArrayList<>();
			
			for (String serializedAction : serializedActions) {
				if (serializedAction != null && !serializedAction.isEmpty()) {
					actions.add(ActionParser.parseAction(serializedAction));
				}
			}

			if (!actions.isEmpty()) {
				icon.setClickActions(actions);
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
					ItemStackParser itemReader = new ItemStackParser(serializedItem, true);
					RequiredItem requiredItem = new RequiredItem(itemReader.getMaterial(), itemReader.getAmount());
					if (itemReader.hasExplicitDurability()) {
						requiredItem.setRestrictiveDurability(itemReader.getDurability());
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
		Preconditions.notNull(section, "section");

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

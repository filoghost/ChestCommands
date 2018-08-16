package com.gmail.filoghost.chestcommands.serializer;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import com.gmail.filoghost.chestcommands.api.Icon;
import com.gmail.filoghost.chestcommands.config.AsciiPlaceholders;
import com.gmail.filoghost.chestcommands.exception.FormatException;
import com.gmail.filoghost.chestcommands.internal.CommandsClickHandler;
import com.gmail.filoghost.chestcommands.internal.RequiredItem;
import com.gmail.filoghost.chestcommands.internal.icon.ExtendedIcon;
import com.gmail.filoghost.chestcommands.internal.icon.IconCommand;
import com.gmail.filoghost.chestcommands.util.ErrorLogger;
import com.gmail.filoghost.chestcommands.util.ItemStackReader;
import com.gmail.filoghost.chestcommands.util.Utils;
import com.gmail.filoghost.chestcommands.util.Validate;

public class IconSerializer {
	
	private static class Nodes {
		
		public static final
				String ID = "ID",
				DATA_VALUE = "DATA-VALUE",
				AMOUNT = "AMOUNT",
				NAME = "NAME",
				LORE = "LORE",
				ENCHANT = "ENCHANTMENT",
				COLOR = "COLOR",
				SKULL_OWNER = "SKULL-OWNER",
				COMMAND = "COMMAND",
				PRICE = "PRICE",
				POINTS = "POINTS",
				EXP_LEVELS = "LEVELS",
				REQUIRED_ITEM = "REQUIRED-ITEM",
				PERMISSION = "PERMISSION",
				PERMISSION_MESSAGE = "PERMISSION-MESSAGE",
				VIEW_PERMISSION = "VIEW-PERMISSION",
				KEEP_OPEN = "KEEP-OPEN",
				POSITION_X = "POSITION-X",
				POSITION_Y = "POSITION-Y";
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

	public static Icon loadIconFromSection(ConfigurationSection section, String iconName, String menuFileName, ErrorLogger errorLogger) {
		Validate.notNull(section, "ConfigurationSection cannot be null");
		
		// The icon is valid even without a Material.
		ExtendedIcon icon = new ExtendedIcon();
		
		if (section.isSet(Nodes.ID)) {
			try {
				ItemStackReader itemReader = new ItemStackReader(section.getString(Nodes.ID), true);
				icon.setMaterial(itemReader.getMaterial());
				icon.setDataValue(itemReader.getDataValue());
				icon.setAmount(itemReader.getAmount());
			} catch (FormatException e) {
				errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid ID: " + e.getMessage());
			}
		}
		
		if (section.isSet(Nodes.DATA_VALUE)) {
			icon.setDataValue((short) section.getInt(Nodes.DATA_VALUE));
		}
		
		if (section.isSet(Nodes.AMOUNT)) {
			icon.setAmount(section.getInt(Nodes.AMOUNT));
		}
		
		icon.setName(AsciiPlaceholders.placeholdersToSymbols(Utils.colorizeName(section.getString(Nodes.NAME))));
		icon.setLore(AsciiPlaceholders.placeholdersToSymbols(Utils.colorizeLore(section.getStringList(Nodes.LORE))));
		
		if (section.isSet(Nodes.ENCHANT)) {
			icon.setEnchantments(EnchantmentSerializer.loadEnchantments(section.getString(Nodes.ENCHANT), iconName, menuFileName, errorLogger));
		}
		
		if (section.isSet(Nodes.COLOR)) {
			try {
				icon.setColor(Utils.parseColor(section.getString(Nodes.COLOR)));
			} catch (FormatException e) {
				errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid COLOR: " + e.getMessage());
			}
		}
		
		icon.setSkullOwner(section.getString(Nodes.SKULL_OWNER));
		
		icon.setPermission(section.getString(Nodes.PERMISSION));
		icon.setPermissionMessage(Utils.addColors(section.getString(Nodes.PERMISSION_MESSAGE)));
		icon.setViewPermission(section.getString(Nodes.VIEW_PERMISSION));
		
		boolean closeOnClick = !section.getBoolean(Nodes.KEEP_OPEN);
		icon.setCloseOnClick(closeOnClick);
		
		if (section.isSet(Nodes.COMMAND)) {
			
			List<IconCommand> commands;
			
			if (section.isList(Nodes.COMMAND)) {
				commands = Utils.newArrayList();
				
				for (String commandString : section.getStringList(Nodes.COMMAND)) {
					if (commandString.isEmpty()) {
						continue;
					}
					commands.add(CommandSerializer.matchCommand(commandString));
				}
				
			} else {
				commands = CommandSerializer.readCommands(section.getString(Nodes.COMMAND));
			}
			
			icon.setClickHandler(new CommandsClickHandler(commands, closeOnClick));
		}
		
		double price = section.getDouble(Nodes.PRICE);
		if (price > 0.0) {
			icon.setMoneyPrice(price);
		} else if (price < 0.0) {
			errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has a negative PRICE: " + price);
		}
		
		int points = section.getInt(Nodes.POINTS);
		if (points > 0) {
			icon.setPlayerPointsPrice(points);
		} else if (points < 0) {
			errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has negative POINTS: " + points);
		}
		
		int levels = section.getInt(Nodes.EXP_LEVELS);
		if (levels > 0) {
			icon.setExpLevelsPrice(levels);
		} else if (levels < 0) {
			errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has negative LEVELS: " + levels);
		}
		
		if (section.isSet(Nodes.REQUIRED_ITEM)) {
			try {
				ItemStackReader itemReader = new ItemStackReader(section.getString(Nodes.REQUIRED_ITEM), true);
				RequiredItem requiredItem = new RequiredItem(itemReader.getMaterial(), itemReader.getAmount());
				if (itemReader.hasExplicitDataValue()) {
					requiredItem.setRestrictiveDataValue(itemReader.getDataValue());
				}
				icon.setRequiredItem(requiredItem);
			} catch (FormatException e) {
				errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid REQUIRED-ITEM: " + e.getMessage());
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
	
//	/**
//	 * Reads a list of strings or a single String as list.
//	 */
//	private static List<String> readAsList(ConfigurationSection section, String node) {
//		if (section.isList(node)) {
//			return section.getStringList(node);
//		} else if (section.isString(node)) {
//			return Arrays.asList(section.getString(node));
//		} else {
//			return null;
//		}
//	}
	
	public static void saveToSection(Icon icon, ConfigurationSection section) {
		Validate.notNull(icon, "Icon cannot be null");
		Validate.notNull(section, "ConfigurationSection cannot be null");
		
		section.set(Nodes.ID, serializeIconID(icon));
		
		if (icon.getEnchantments().size() > 0) {
			section.set(Nodes.ENCHANT, 1);
		}
		
		//TODO not finished
	}
	
	public static String serializeIconID(Icon icon) {
		if (icon.getMaterial() == null) {
			return "Not set";
		}
		
		StringBuilder output = new StringBuilder();
		output.append(Utils.formatMaterial(icon.getMaterial()));
		
		if (icon.getDataValue() > 0) {
			output.append(":");
			output.append(icon.getDataValue());
		}
		
		if (icon.getAmount() != 1) {
			output.append(", ");
			output.append(icon.getAmount());
		}
		
		return output.toString();
	}
	
}

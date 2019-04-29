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
import com.gmail.filoghost.chestcommands.util.nbt.parser.MojangsonParseException;
import com.gmail.filoghost.chestcommands.util.nbt.parser.MojangsonParser;

public class IconSerializer {
	
	private static class Nodes {
		
		public static final	String
				ID = "ID",
				AMOUNT = "AMOUNT",
				DATA_VALUE = "DATA-VALUE",
				DURABILITY = "DURABILITY",
				DAMAGE = "DAMAGE",
				CUSTOM_MODEL_DATA = "CUSTOM-MODEL-DATA",
            
				UNBREAKABLE = "UNBREAKABLE", // These correspond to the Unbreakable NBT tag on item stacks.
				UNBREAKABLE_UNBREAKING = "UNBREAKING",
				UNBREAKABLE_DAMAGED = "DAMAGED", 
            
				NBT_DATA = "NBT-DATA",
				NAME = "NAME",
				LORE = "LORE",
				ENCHANT = "ENCHANTMENT",
				COLOR = "COLOR",
				SKULL_OWNER = "SKULL-OWNER",
				COMMAND = "COMMAND",
				PRICE = "PRICE",
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
		
		// The icon is valid even without a Material
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
		
		if (section.isSet(Nodes.AMOUNT)) {
			icon.setAmount(section.getInt(Nodes.AMOUNT));
		}
		
		if (section.isSet(Nodes.DURABILITY)) {
			icon.setDataValue((short) section.getInt(Nodes.DURABILITY));
		} else if (section.isSet(Nodes.DATA_VALUE)) { // Alias
			icon.setDataValue((short) section.getInt(Nodes.DATA_VALUE));
		}
        
        boolean isUnbreakable = section.getBoolean(Nodes.UNBREAKABLE, section.getBoolean(Nodes.UNBREAKABLE_DAMAGED,section.getBoolean(Nodes.UNBREAKABLE_UNBREAKING, true)));
		icon.setIsUnbreakable(isUnbreakable);
        
        if (section.isSet(Nodes.DAMAGE)) {
			icon.setDamageValue(section.getInt(Nodes.DAMAGE));
		}
        
		if (section.isSet(Nodes.CUSTOM_MODEL_DATA)) {
			icon.setCustomModelDataValue(section.getInt(Nodes.CUSTOM_MODEL_DATA));
		}
        
		if (section.isSet(Nodes.NBT_DATA)) {
			String nbtData = section.getString(Nodes.NBT_DATA);
			try {
				// Check that NBT has valid syntax before applying it to the icon
				MojangsonParser.parse(nbtData);
				icon.setNBTData(nbtData);
			} catch (MojangsonParseException e) {
				errorLogger.addError("The icon \"" + iconName + "\" in the menu \"" + menuFileName + "\" has an invalid NBT-DATA: " + e.getMessage());
			}
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
	
}

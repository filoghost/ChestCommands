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
package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.config.framework.ConfigValue;
import me.filoghost.chestcommands.config.framework.ConfigValueType;
import me.filoghost.chestcommands.config.framework.exception.ConfigValueException;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.parsing.attribute.ActionsAttribute;
import me.filoghost.chestcommands.parsing.attribute.AmountAttribute;
import me.filoghost.chestcommands.parsing.attribute.AttributeErrorHandler;
import me.filoghost.chestcommands.parsing.attribute.BannerColorAttribute;
import me.filoghost.chestcommands.parsing.attribute.BannerPatternsAttribute;
import me.filoghost.chestcommands.parsing.attribute.ClickPermissionAttribute;
import me.filoghost.chestcommands.parsing.attribute.ClickPermissionMessageAttribute;
import me.filoghost.chestcommands.parsing.attribute.DurabilityAttribute;
import me.filoghost.chestcommands.parsing.attribute.EnchantmentsAttribute;
import me.filoghost.chestcommands.parsing.attribute.ExpLevelsAttribute;
import me.filoghost.chestcommands.parsing.attribute.IconAttribute;
import me.filoghost.chestcommands.parsing.attribute.KeepOpenAttribute;
import me.filoghost.chestcommands.parsing.attribute.LeatherColorAttribute;
import me.filoghost.chestcommands.parsing.attribute.LoreAttribute;
import me.filoghost.chestcommands.parsing.attribute.MaterialAttribute;
import me.filoghost.chestcommands.parsing.attribute.NBTDataAttribute;
import me.filoghost.chestcommands.parsing.attribute.NameAttribute;
import me.filoghost.chestcommands.parsing.attribute.PositionAttribute;
import me.filoghost.chestcommands.parsing.attribute.PriceAttribute;
import me.filoghost.chestcommands.parsing.attribute.RequiredItemsAttribute;
import me.filoghost.chestcommands.parsing.attribute.SkullOwnerAttribute;
import me.filoghost.chestcommands.parsing.attribute.ViewPermissionAttribute;

import java.util.HashMap;
import java.util.Map;

public enum AttributeType {

	POSITION_X("POSITION-X", ConfigValueType.INTEGER, PositionAttribute::new),
	POSITION_Y("POSITION-Y", ConfigValueType.INTEGER, PositionAttribute::new),
	MATERIAL("MATERIAL", ConfigValueType.STRING, MaterialAttribute::new),
	DURABILITY("DURABILITY", ConfigValueType.SHORT, DurabilityAttribute::new),
	AMOUNT("AMOUNT", ConfigValueType.INTEGER, AmountAttribute::new),
	NAME("NAME", ConfigValueType.STRING, NameAttribute::new),
	LORE("LORE", ConfigValueType.STRING_LIST, LoreAttribute::new),
	NBT_DATA("NBT-DATA", ConfigValueType.STRING, NBTDataAttribute::new),
	LEATHER_COLOR("COLOR", ConfigValueType.STRING, LeatherColorAttribute::new),
	SKULL_OWNER("SKULL-OWNER", ConfigValueType.STRING, SkullOwnerAttribute::new),
	BANNER_COLOR("BANNER-COLOR", ConfigValueType.STRING, BannerColorAttribute::new),
	BANNER_PATTERNS("BANNER-PATTERNS", ConfigValueType.STRING_LIST, BannerPatternsAttribute::new),
	PRICE("PRICE", ConfigValueType.DOUBLE, PriceAttribute::new),
	EXP_LEVELS("LEVELS", ConfigValueType.INTEGER, ExpLevelsAttribute::new),
	CLICK_PERMISSION("PERMISSION", ConfigValueType.STRING, ClickPermissionAttribute::new),
	CLICK_PERMISSION_MESSAGE("PERMISSION-MESSAGE", ConfigValueType.STRING, ClickPermissionMessageAttribute::new),
	VIEW_PERMISSION("VIEW-PERMISSION", ConfigValueType.STRING, ViewPermissionAttribute::new),
	KEEP_OPEN("KEEP-OPEN", ConfigValueType.BOOLEAN, KeepOpenAttribute::new),
	ACTIONS("ACTIONS", ConfigValueType.STRING_LIST, ActionsAttribute::new),
	ENCHANTMENTS("ENCHANTMENTS", ConfigValueType.STRING_LIST, EnchantmentsAttribute::new),
	REQUIRED_ITEMS("REQUIRED-ITEMS", ConfigValueType.STRING_LIST, RequiredItemsAttribute::new);

	private static final Map<String, AttributeType> parsersByAttributeName;
	static {
		parsersByAttributeName = new HashMap<>();
		for (AttributeType attributeParser : values()) {
			parsersByAttributeName.put(attributeParser.getAttributeName(), attributeParser);
		}
	}

	private final String attributeName;
	private final AttributeParser attributeParser;

	<V> AttributeType(String attributeName, ConfigValueType<V> configValueType, AttributeFactory<V, ?> attributeFactory) {
		this.attributeName = attributeName;
		this.attributeParser = (ConfigValue configValue, AttributeErrorHandler errorHandler) -> {
			return attributeFactory.create(configValue.asRequired(configValueType), errorHandler);
		};
	}

	public String getAttributeName() {
		return attributeName;
	}

	public AttributeParser getParser() {
		return attributeParser;
	}

	public static AttributeType fromAttributeName(String attributeName) {
		return parsersByAttributeName.get(attributeName);
	}


	@FunctionalInterface
	private interface AttributeFactory<V, A extends IconAttribute> {

		A create(V value, AttributeErrorHandler errorHandler) throws ParseException;

	}


	@FunctionalInterface
	public interface AttributeParser {

		IconAttribute parse(ConfigValue configValue, AttributeErrorHandler errorHandler) throws ParseException, ConfigValueException;

	}

}

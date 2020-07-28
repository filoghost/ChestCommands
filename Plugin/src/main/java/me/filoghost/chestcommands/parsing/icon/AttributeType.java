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

import me.filoghost.chestcommands.config.framework.ConfigSection;
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
import java.util.List;
import java.util.Map;

public enum AttributeType {

		POSITION_X("POSITION-X", ValueExtractor.INT, PositionAttribute::new),
		POSITION_Y("POSITION-Y", ValueExtractor.INT, PositionAttribute::new),
		MATERIAL("MATERIAL", ValueExtractor.STRING, MaterialAttribute::new),
		DURABILITY("DURABILITY", ValueExtractor.SHORT, DurabilityAttribute::new),
		AMOUNT("AMOUNT", ValueExtractor.INT, AmountAttribute::new),
		NAME("NAME", ValueExtractor.STRING, NameAttribute::new),
		LORE("LORE", ValueExtractor.STRING_LIST, LoreAttribute::new),
		NBT_DATA("NBT-DATA", ValueExtractor.STRING, NBTDataAttribute::new),
		LEATHER_COLOR("COLOR", ValueExtractor.STRING, LeatherColorAttribute::new),
		SKULL_OWNER("SKULL-OWNER", ValueExtractor.STRING, SkullOwnerAttribute::new),
		BANNER_COLOR("BANNER-COLOR", ValueExtractor.STRING, BannerColorAttribute::new),
		BANNER_PATTERNS("BANNER-PATTERNS", ValueExtractor.STRING_LIST, BannerPatternsAttribute::new),
		PRICE("PRICE", ValueExtractor.DOUBLE, PriceAttribute::new),
		EXP_LEVELS("LEVELS", ValueExtractor.INT, ExpLevelsAttribute::new),
		CLICK_PERMISSION("PERMISSION", ValueExtractor.STRING, ClickPermissionAttribute::new),
		CLICK_PERMISSION_MESSAGE("PERMISSION-MESSAGE", ValueExtractor.STRING, ClickPermissionMessageAttribute::new),
		VIEW_PERMISSION("VIEW-PERMISSION", ValueExtractor.STRING, ViewPermissionAttribute::new),
		KEEP_OPEN("KEEP-OPEN", ValueExtractor.BOOLEAN, KeepOpenAttribute::new),
		ACTIONS("ACTIONS", ValueExtractor.STRING_LIST, ActionsAttribute::new),
		ENCHANTMENTS("ENCHANTMENTS", ValueExtractor.STRING_LIST, EnchantmentsAttribute::new),
		REQUIRED_ITEMS("REQUIRED-ITEMS", ValueExtractor.STRING_LIST, RequiredItemsAttribute::new);

	private static final Map<String, AttributeType> parsersByAttributeName;
	static {
		parsersByAttributeName = new HashMap<>();
		for (AttributeType attributeParser : values()) {
			parsersByAttributeName.put(attributeParser.getAttributeName(), attributeParser);
		}
	}

	private final String attributeName;
	private final AttributeParser attributeParser;

	<V> AttributeType(String attributeName, ValueExtractor<V> valueExtractor, AttributeFactory<V, ?> attributeFactory) {
		this.attributeName = attributeName;
		this.attributeParser = (ConfigSection config, String node, AttributeErrorHandler errorHandler) -> {
			V configValue = valueExtractor.getValue(config, node);
			return attributeFactory.create(configValue, errorHandler);
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
	private interface ValueExtractor<V> {

		V getValue(ConfigSection config, String key) throws ConfigValueException;

		ValueExtractor<Integer> INT = ConfigSection::getRequiredInt;
		ValueExtractor<Double> DOUBLE = ConfigSection::getRequiredDouble;
		ValueExtractor<Short> SHORT = ConfigSection::getRequiredShort;
		ValueExtractor<Boolean> BOOLEAN = ConfigSection::getRequiredBoolean;
		ValueExtractor<String> STRING = ConfigSection::getRequiredString;
		ValueExtractor<List<String>> STRING_LIST = ConfigSection::getRequiredStringList;

	}


	@FunctionalInterface
	private interface AttributeFactory<V, A extends IconAttribute> {

		A create(V value, AttributeErrorHandler errorHandler) throws ParseException;

	}


	@FunctionalInterface
	public interface AttributeParser {

		IconAttribute parse(ConfigSection config, String node, AttributeErrorHandler errorHandler) throws ParseException, ConfigValueException;

	}

}

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
import me.filoghost.chestcommands.icon.InternalConfigurableIcon;
import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.parsing.attribute.ActionsAttribute;
import me.filoghost.chestcommands.parsing.attribute.AmountAttribute;
import me.filoghost.chestcommands.parsing.attribute.ApplicableIconAttribute;
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
import me.filoghost.chestcommands.util.Preconditions;
import me.filoghost.chestcommands.util.logging.ErrorCollector;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IconSettings {

	private final Path menuFile;
	private final String iconName;

	private PositionAttribute positionX;
	private PositionAttribute positionY;
	private MaterialAttribute materialAttribute;
	private final List<ApplicableIconAttribute> applicableAttributes;

	public static final Map<String, AttributeParser> iconNodeHandlers = new HashMap<>();
	static {
		addIconNodeHandler(IconSettingsNode.POSITION_X, ValueExtractor.INT, PositionAttribute::new, IconSettings::setPositionX);
		addIconNodeHandler(IconSettingsNode.POSITION_Y, ValueExtractor.INT, PositionAttribute::new, IconSettings::setPositionY);
		addIconNodeHandler(IconSettingsNode.MATERIAL, ValueExtractor.STRING, MaterialAttribute::new, IconSettings::setMaterialAttribute);

		addApplicableIconNodeHandler(IconSettingsNode.DURABILITY, ValueExtractor.SHORT, DurabilityAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.AMOUNT, ValueExtractor.INT, AmountAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.NAME, ValueExtractor.STRING, NameAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.LORE, ValueExtractor.STRING_LIST, LoreAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.NBT_DATA, ValueExtractor.STRING, NBTDataAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.LEATHER_COLOR, ValueExtractor.STRING, LeatherColorAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.SKULL_OWNER, ValueExtractor.STRING, SkullOwnerAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.BANNER_COLOR, ValueExtractor.STRING, BannerColorAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.BANNER_PATTERNS, ValueExtractor.STRING_LIST, BannerPatternsAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.PRICE, ValueExtractor.DOUBLE, PriceAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.EXP_LEVELS, ValueExtractor.INT, ExpLevelsAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.CLICK_PERMISSION, ValueExtractor.STRING, ClickPermissionAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.CLICK_PERMISSION_MESSAGE, ValueExtractor.STRING, ClickPermissionMessageAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.VIEW_PERMISSION, ValueExtractor.STRING, ViewPermissionAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.KEEP_OPEN, ValueExtractor.BOOLEAN, KeepOpenAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.ACTIONS, ValueExtractor.STRING_LIST, ActionsAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.ENCHANTMENTS, ValueExtractor.STRING_LIST, EnchantmentsAttribute::new);
		addApplicableIconNodeHandler(IconSettingsNode.REQUIRED_ITEMS, ValueExtractor.STRING_LIST, RequiredItemsAttribute::new);
	}

	public Path getMenuFile() {
		return menuFile;
	}

	public String getIconName() {
		return iconName;
	}

	public PositionAttribute getPositionX() {
		return positionX;
	}

	private void setPositionX(PositionAttribute positionAttribute) {
		this.positionX = positionAttribute;
	}

	public PositionAttribute getPositionY() {
		return positionY;
	}

	private void setPositionY(PositionAttribute positionAttribute) {
		this.positionY = positionAttribute;
	}

	public MaterialAttribute getMaterialAttribute() {
		return materialAttribute;
	}

	private void setMaterialAttribute(MaterialAttribute materialAttribute) {
		this.materialAttribute = materialAttribute;
	}

	public void addApplicableAttribute(ApplicableIconAttribute iconAttribute) {
		applicableAttributes.add(iconAttribute);
	}

	public void applyAttributesTo(InternalConfigurableIcon icon) {
		if (materialAttribute != null) {
			materialAttribute.apply(icon);
		}
		for (ApplicableIconAttribute attribute : applicableAttributes) {
			attribute.apply(icon);
		}
	}

	public static <V, A extends ApplicableIconAttribute> void addApplicableIconNodeHandler(
			String node,
			ValueExtractor<V> valueExtractor,
			AttributeFactory<V, A> attributeFactory) {
		addIconNodeHandler(node, valueExtractor, attributeFactory, IconSettings::addApplicableAttribute);
	}


	public static <V, A extends IconAttribute> void addIconNodeHandler(
			String node,
			ValueExtractor<V> valueExtractor,
			AttributeFactory<V, A> attributeFactory,
			AttributeApplier<A> callback) {
		addIconNodeHandler(node, (iconSettings, config, configNode, parseContext) -> {
			V value = valueExtractor.getValue(config, configNode);
			A iconAttribute = attributeFactory.create(value, parseContext);
			callback.apply(iconSettings, iconAttribute);
		});
	}

	private static void addIconNodeHandler(String node, AttributeParser iconNodeHandler) {
		Preconditions.checkState(!iconNodeHandlers.containsKey(node), "Handler already exists for attribute " + node);
		iconNodeHandlers.put(node, iconNodeHandler);
	}

	public IconSettings(Path menuFile, String iconName) {
		this.menuFile = menuFile;
		this.iconName = iconName;
		this.applicableAttributes = new ArrayList<>();
	}

	public void loadFrom(ConfigSection config, ErrorCollector errorCollector) {
		for (String attributeName : config.getKeys(false)) {
			try {
				AttributeParser attributeParser = iconNodeHandlers.get(attributeName);
				if (attributeParser == null) {
					throw new ParseException(ErrorMessages.Parsing.unknownAttribute);
				}

				attributeParser.parse(this, config, attributeName, (String listElement, ParseException e) -> {
					errorCollector.add(ErrorMessages.Menu.invalidAttributeListElement(this, attributeName, listElement), e);
				});

			} catch (ParseException | ConfigValueException e) {
				errorCollector.add(ErrorMessages.Menu.invalidAttribute(this, attributeName), e);
			}
		}
	}


	private interface ValueExtractor<V> {

		V getValue(ConfigSection config, String key) throws ConfigValueException;

		ValueExtractor<Integer> INT = ConfigSection::getRequiredInt;
		ValueExtractor<Double> DOUBLE = ConfigSection::getRequiredDouble;
		ValueExtractor<Short> SHORT = ConfigSection::getRequiredShort;
		ValueExtractor<Boolean> BOOLEAN = ConfigSection::getRequiredBoolean;
		ValueExtractor<String> STRING = ConfigSection::getRequiredString;
		ValueExtractor<List<String>> STRING_LIST = ConfigSection::getRequiredStringList;

	}

	private interface AttributeFactory<V, A extends IconAttribute> {

		A create(V value, AttributeErrorHandler errorHandler) throws ParseException;

	}

	private interface AttributeApplier<A extends IconAttribute> {

		void apply(IconSettings iconSettings, A attribute);

	}

	private interface AttributeParser {

		void parse(IconSettings iconSettings, ConfigSection config, String node, AttributeErrorHandler errorHandler) throws ParseException, ConfigValueException;

	}

}

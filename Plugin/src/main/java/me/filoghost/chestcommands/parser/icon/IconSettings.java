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
package me.filoghost.chestcommands.parser.icon;

import me.filoghost.chestcommands.config.ConfigSection;
import me.filoghost.chestcommands.config.ConfigValueException;
import me.filoghost.chestcommands.menu.icon.AdvancedIcon;
import me.filoghost.chestcommands.parser.ParseException;
import me.filoghost.chestcommands.parser.icon.attributes.ActionsAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.AmountAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.BannerColorAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.BannerPatternsAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.ClickPermissionAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.ClickPermissionMessageAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.DurabilityAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.EnchantmentsAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.ExpLevelsAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.KeepOpenAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.LeatherColorAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.LoreAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.MaterialAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.NBTDataAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.NameAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.PositionAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.PriceAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.RequiredItemsAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.SkullOwnerAttribute;
import me.filoghost.chestcommands.parser.icon.attributes.ViewPermissionAttribute;
import me.filoghost.chestcommands.util.ErrorCollector;
import me.filoghost.chestcommands.util.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IconSettings {

	private final String menuName;
	private final String iconName;

	private PositionAttribute positionX;
	private PositionAttribute positionY;
	private MaterialAttribute materialAttribute;
	private final List<ApplicableIconAttribute> applicableAttributes;

	public static final Map<String, IconNodeHandler> iconNodeHandlers = new HashMap<>();
	static {
		addIconNodeHandler(IconNode.POSITION_X, ValueExtractor.INT, PositionAttribute::new, IconSettings::setPositionX);
		addIconNodeHandler(IconNode.POSITION_Y, ValueExtractor.INT, PositionAttribute::new, IconSettings::setPositionY);
		addIconNodeHandler(IconNode.MATERIAL, ValueExtractor.STRING, MaterialAttribute::new, IconSettings::setMaterialAttribute);

		addApplicableIconNodeHandler(IconNode.DURABILITY, ValueExtractor.SHORT, DurabilityAttribute::new);
		addApplicableIconNodeHandler(IconNode.AMOUNT, ValueExtractor.INT, AmountAttribute::new);
		addApplicableIconNodeHandler(IconNode.NAME, ValueExtractor.STRING, NameAttribute::new);
		addApplicableIconNodeHandler(IconNode.LORE, ValueExtractor.STRING_LIST, LoreAttribute::new);
		addApplicableIconNodeHandler(IconNode.NBT_DATA, ValueExtractor.STRING, NBTDataAttribute::new);
		addApplicableIconNodeHandler(IconNode.LEATHER_COLOR, ValueExtractor.STRING, LeatherColorAttribute::new);
		addApplicableIconNodeHandler(IconNode.SKULL_OWNER, ValueExtractor.STRING, SkullOwnerAttribute::new);
		addApplicableIconNodeHandler(IconNode.BANNER_COLOR, ValueExtractor.STRING, BannerColorAttribute::new);
		addApplicableIconNodeHandler(IconNode.BANNER_PATTERNS, ValueExtractor.STRING_LIST, BannerPatternsAttribute::new);
		addApplicableIconNodeHandler(IconNode.PRICE, ValueExtractor.DOUBLE, PriceAttribute::new);
		addApplicableIconNodeHandler(IconNode.EXP_LEVELS, ValueExtractor.INT, ExpLevelsAttribute::new);
		addApplicableIconNodeHandler(IconNode.CLICK_PERMISSION, ValueExtractor.STRING, ClickPermissionAttribute::new);
		addApplicableIconNodeHandler(IconNode.CLICK_PERMISSION_MESSAGE, ValueExtractor.STRING, ClickPermissionMessageAttribute::new);
		addApplicableIconNodeHandler(IconNode.VIEW_PERMISSION, ValueExtractor.STRING, ViewPermissionAttribute::new);
		addApplicableIconNodeHandler(IconNode.KEEP_OPEN, ValueExtractor.BOOLEAN, KeepOpenAttribute::new);
		addApplicableIconNodeHandler(IconNode.ACTIONS, ValueExtractor.STRING_LIST, ActionsAttribute::new);
		addApplicableIconNodeHandler(IconNode.ENCHANTMENTS, ValueExtractor.STRING_LIST, EnchantmentsAttribute::new);
		addApplicableIconNodeHandler(IconNode.REQUIRED_ITEMS, ValueExtractor.STRING_LIST, RequiredItemsAttribute::new);
	}

	public String getMenuName() {
		return menuName;
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

	public void applyAttributesTo(AdvancedIcon icon) {
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
			AttributeCallback<A> callback) {
		addIconNodeHandler(node, (iconSettings, config, configNode, parseContext) -> {
			V value = valueExtractor.getValue(config, configNode);
			A iconAttribute = attributeFactory.create(value, parseContext);
			callback.apply(iconSettings, iconAttribute);
		});
	}

	private static void addIconNodeHandler(String node, IconNodeHandler iconNodeHandler) {
		Preconditions.checkState(!iconNodeHandlers.containsKey(node), "Handler already exists for attribute " + node);
		iconNodeHandlers.put(node, iconNodeHandler);
	}

	public IconSettings(String menuName, String iconName) {
		this.menuName = menuName;
		this.iconName = iconName;
		this.applicableAttributes = new ArrayList<>();
	}

	public void loadFrom(ConfigSection config, ErrorCollector errorCollector) {
		for (String attributeKey : config.getKeys(false)) {
			AttributeErrorCollector attributeErrorCollector = new AttributeErrorCollector(errorCollector, this, attributeKey);

			try {
				IconNodeHandler nodeHandler = iconNodeHandlers.get(attributeKey);
				if (nodeHandler == null) {
					throw new ParseException("unknown attribute");
				}

				nodeHandler.handle(this, config, attributeKey, attributeErrorCollector);

			} catch (ParseException | ConfigValueException e) {
				attributeErrorCollector.addAttributeError(e);
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

		A create(V value, AttributeErrorCollector attributeErrorCollector) throws ParseException;

	}

	private interface AttributeCallback<A extends IconAttribute> {

		void apply(IconSettings iconSettings, A attribute);

	}

	private interface IconNodeHandler {

		void handle(IconSettings iconSettings, ConfigSection config, String node, AttributeErrorCollector attributeErrorCollector) throws ParseException, ConfigValueException;

	}


}

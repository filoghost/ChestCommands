/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.icon;

import java.util.HashMap;
import java.util.Map;

import me.filoghost.chestcommands.attribute.*;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.config.ConfigValue;
import me.filoghost.fcommons.config.ConfigValueType;
import me.filoghost.fcommons.config.exception.ConfigValueException;

public enum AttributeType {

    POSITION_X("POSITION-X", ConfigValueType.INTEGER, PositionAttribute::new),
    POSITION_Y("POSITION-Y", ConfigValueType.INTEGER, PositionAttribute::new),
    BLANK("BLANK", ConfigValueType.BOOLEAN, BlankAttribute::new),
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
    REQUIRED_ITEMS("REQUIRED-ITEMS", ConfigValueType.STRING_LIST, RequiredItemsAttribute::new),
    CUSTOM_ITEM("CUSTOM-ITEM", ConfigValueType.STRING, CustomItemAttribute::new),
    CUSTOM_MODEL_DATA("CUSTOM-MODEL-DATA", ConfigValueType.INTEGER, CustomModelDataAttribute::new)
    ;

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

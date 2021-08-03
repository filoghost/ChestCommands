/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.attribute.*;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.config.ConfigPath;
import me.filoghost.fcommons.config.ConfigType;
import me.filoghost.fcommons.config.ConfigValue;
import me.filoghost.fcommons.config.exception.ConfigValueException;

import java.util.HashMap;
import java.util.Map;

public enum AttributeType {

    POSITION_X("POSITION-X", ConfigType.INTEGER, PositionAttribute::new),
    POSITION_Y("POSITION-Y", ConfigType.INTEGER, PositionAttribute::new),
    BLANK("BLANK", ConfigType.BOOLEAN, BlankAttribute::new),
    MATERIAL("MATERIAL", ConfigType.STRING, MaterialAttribute::new),
    DURABILITY("DURABILITY", ConfigType.SHORT, DurabilityAttribute::new),
    AMOUNT("AMOUNT", ConfigType.INTEGER, AmountAttribute::new),
    NAME("NAME", ConfigType.STRING, NameAttribute::new),
    LORE("LORE", ConfigType.STRING_LIST, LoreAttribute::new),
    NBT_DATA("NBT-DATA", ConfigType.STRING, NBTDataAttribute::new),
    LEATHER_COLOR("COLOR", ConfigType.STRING, LeatherColorAttribute::new),
    SKULL_OWNER("SKULL-OWNER", ConfigType.STRING, SkullOwnerAttribute::new),
    BANNER_COLOR("BANNER-COLOR", ConfigType.STRING, BannerColorAttribute::new),
    BANNER_PATTERNS("BANNER-PATTERNS", ConfigType.STRING_LIST, BannerPatternsAttribute::new),
    PRICE("PRICE", ConfigType.DOUBLE, PriceAttribute::new),
    EXP_LEVELS("LEVELS", ConfigType.INTEGER, ExpLevelsAttribute::new),
    CLICK_PERMISSION("PERMISSION", ConfigType.STRING, ClickPermissionAttribute::new),
    CLICK_PERMISSION_MESSAGE("PERMISSION-MESSAGE", ConfigType.STRING, ClickPermissionMessageAttribute::new),
    VIEW_PERMISSION("VIEW-PERMISSION", ConfigType.STRING, ViewPermissionAttribute::new),
    KEEP_OPEN("KEEP-OPEN", ConfigType.BOOLEAN, KeepOpenAttribute::new),
    ACTIONS("ACTIONS", ConfigType.STRING_LIST, ActionsAttribute::new),
    ENCHANTMENTS("ENCHANTMENTS", ConfigType.STRING_LIST, EnchantmentsAttribute::new),
    REQUIRED_ITEMS("REQUIRED-ITEMS", ConfigType.STRING_LIST, RequiredItemsAttribute::new),
    CUSTOM_ITEM("CUSTOM-ITEM", ConfigType.STRING, CustomItemAttribute::new),
    CUSTOM_MODEL_DATA("CUSTOM-MODEL-DATA", ConfigType.INTEGER, CustomModelDataAttribute::new)
    ;

    private static final Map<ConfigPath, AttributeType> attributeTypeByConfigKey;
    
    static {
        attributeTypeByConfigKey = new HashMap<>();
        for (AttributeType attributeParser : values()) {
            attributeTypeByConfigKey.put(attributeParser.getConfigKey(), attributeParser);
        }
    }

    private final ConfigPath configKey;
    private final AttributeParser attributeParser;

    <V> AttributeType(String configKey, ConfigType<V> configType, AttributeFactory<V, ?> attributeFactory) {
        this.configKey = ConfigPath.literal(configKey);
        this.attributeParser = (ConfigValue configValue, AttributeErrorHandler errorHandler) -> {
            return attributeFactory.create(configValue.asRequired(configType), errorHandler);
        };
    }

    public ConfigPath getConfigKey() {
        return configKey;
    }

    public AttributeParser getParser() {
        return attributeParser;
    }

    public static AttributeType fromConfigKey(ConfigPath configKey) {
        return attributeTypeByConfigKey.get(configKey);
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

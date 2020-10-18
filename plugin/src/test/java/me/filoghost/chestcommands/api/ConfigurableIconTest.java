/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.api;

import me.filoghost.chestcommands.icon.BaseConfigurableIcon;
import me.filoghost.chestcommands.test.BukkitMocks;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ConfigurableIconTest {

    @BeforeAll
    static void beforeAll() {
        ChestCommandsAPI.registerPlaceholder(BukkitMocks.PLUGIN, "test", (player, argument) -> {
            if (argument != null) {
                return argument;
            } else {
                return "EMPTY";
            }
        });
    }

    @AfterAll
    static void afterAll() {
        ChestCommandsAPI.unregisterPlaceholder(BukkitMocks.PLUGIN, "test");
    }

    @Test
    void customPlaceholderReplacements() {
        BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("{test: start} abc {test} {MockPlugin/test} {test: 1} {mOckPLuGin/tEsT: 2} 123 {test: end}");
        assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("start abc EMPTY EMPTY 1 2 123 end");
    }

    @Test
    void placeholdersEnabled() {
        BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("abc {player} {test} 123");
        assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc " + BukkitMocks.PLAYER.getName() + " EMPTY 123");
    }

    @Test
    void placeholdersNotEnabled() {
        BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setName("abc {player} {test} 123");
        assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc {player} {test} 123");
    }

    @Test
    void dynamicPlaceholderRegistration() {
        BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("abc {temp} 123");

        try {
            ChestCommandsAPI.registerPlaceholder(BukkitMocks.PLUGIN, "temp", (player, argument) -> "value");

            assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc value 123");
        } finally {
            ChestCommandsAPI.unregisterPlaceholder(BukkitMocks.PLUGIN, "temp");
        }
    }

    @Test
    void placeholderUnregistration() {
        BaseConfigurableIcon icon = (BaseConfigurableIcon) ConfigurableIcon.create(Material.STONE);
        icon.setPlaceholdersEnabled(true);
        icon.setName("abc {temp} 123");

        try {
            ChestCommandsAPI.registerPlaceholder(BukkitMocks.PLUGIN, "temp", (player, argument) -> "value");
        } finally {
            ChestCommandsAPI.unregisterPlaceholder(BukkitMocks.PLUGIN, "temp");
        }

        assertThat(icon.renderName(BukkitMocks.PLAYER)).isEqualTo("abc {temp} 123");
    }

}

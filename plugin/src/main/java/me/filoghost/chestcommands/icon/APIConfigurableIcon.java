/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import me.filoghost.chestcommands.api.ClickHandler;
import me.filoghost.chestcommands.api.ConfigurableIcon;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

public class APIConfigurableIcon extends BaseConfigurableIcon implements ConfigurableIcon {

    private ClickHandler clickHandler;

    public APIConfigurableIcon(Material material) {
        super(material);
    }

    @Override
    public void setClickHandler(@Nullable ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public @Nullable ClickHandler getClickHandler() {
        return clickHandler;
    }

}

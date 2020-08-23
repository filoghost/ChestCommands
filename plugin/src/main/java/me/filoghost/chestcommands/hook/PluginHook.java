/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.hook;

import me.filoghost.fcommons.Preconditions;

public interface PluginHook {

    
    void setup();
    
    boolean isEnabled();
    
    default void checkEnabledState() {
        Preconditions.checkState(isEnabled(), "Plugin hook " + getClass().getSimpleName() + " is not enabled");
    }

}

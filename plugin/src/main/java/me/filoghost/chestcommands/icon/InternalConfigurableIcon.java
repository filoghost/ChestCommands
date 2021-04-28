/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.icon;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.action.Action;
import me.filoghost.chestcommands.action.OpenMenuAction;
import me.filoghost.chestcommands.api.MenuView;
import me.filoghost.chestcommands.config.Lang;
import me.filoghost.chestcommands.icon.requirement.RequiredExpLevel;
import me.filoghost.chestcommands.icon.requirement.RequiredMoney;
import me.filoghost.chestcommands.icon.requirement.Requirement;
import me.filoghost.chestcommands.icon.requirement.item.RequiredItem;
import me.filoghost.chestcommands.icon.requirement.item.RequiredItems;
import me.filoghost.fcommons.Preconditions;
import me.filoghost.fcommons.collection.CollectionUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InternalConfigurableIcon extends BaseConfigurableIcon implements RefreshableIcon {

    private IconPermission viewPermission;
    private IconPermission clickPermission;
    private String noClickPermissionMessage;

    private RequiredMoney requiredMoney;
    private RequiredExpLevel requiredExpLevel;
    private RequiredItems requiredItems;

    private ImmutableList<Action> clickActions;
    private ClickResult clickResult;

    public InternalConfigurableIcon(Material material) {
        super(material);
        setPlaceholdersEnabled(true);
        this.clickResult = ClickResult.CLOSE;
    }

    public boolean canViewIcon(Player player) {
        return IconPermission.hasPermission(player, viewPermission);
    }
    
    public boolean hasViewPermission() {
        return viewPermission != null && !viewPermission.isEmpty();
    }

    public void setClickPermission(String permission) {
        this.clickPermission = new IconPermission(permission);
    }
    
    public void setNoClickPermissionMessage(String noClickPermissionMessage) {
        this.noClickPermissionMessage = noClickPermissionMessage;
    }
        
    public void setViewPermission(String viewPermission) {
        this.viewPermission = new IconPermission(viewPermission);
    }

    public void setRequiredMoney(double requiredMoney) {
        if (requiredMoney > 0.0) {
            this.requiredMoney = new RequiredMoney(requiredMoney);
        } else {
            this.requiredMoney = null;
        }
    }

    public void setRequiredExpLevel(int requiredLevels) {
        if (requiredLevels > 0) {
            this.requiredExpLevel = new RequiredExpLevel(requiredLevels);
        } else {
            this.requiredExpLevel = null;
        }
    }

    public void setRequiredItems(List<RequiredItem> requiredItems) {
        if (requiredItems != null) {
            this.requiredItems = new RequiredItems(requiredItems);
        } else {
            this.requiredItems = null;
        }
    }

    public void setClickActions(List<Action> clickActions) {
        this.clickActions = CollectionUtils.newImmutableList(clickActions);
    }
    
    
    @Override
    public ItemStack render(@NotNull Player viewer) {
        if (canViewIcon(viewer)) {
            return super.render(viewer);
        } else {
            return null;
        }
    }

    @Override
    protected boolean shouldCacheRendering() {
        return super.shouldCacheRendering() && !hasViewPermission();
    }


    public void setClickResult(ClickResult clickResult) {
        Preconditions.notNull(clickResult, "clickResult");
        this.clickResult = clickResult;
    }

    @Override
    public void onClick(@NotNull MenuView menuView, @NotNull Player player) {
        ClickResult clickResult = onClickGetResult(menuView, player);
        if (clickResult == ClickResult.CLOSE) {
            menuView.close();
        }
    }

    private ClickResult onClickGetResult(@NotNull MenuView menuView, @NotNull Player player) {
        if (!IconPermission.hasPermission(player, viewPermission)) {
            return ClickResult.KEEP_OPEN;
        }

        if (!IconPermission.hasPermission(player, clickPermission)) {
            if (noClickPermissionMessage != null) {
                player.sendMessage(noClickPermissionMessage);
            } else {
                player.sendMessage(Lang.get().default_no_icon_permission);
            }
            return clickResult;
        }

        // Check all the requirements
        Requirement[] requirements = {requiredMoney, requiredExpLevel, requiredItems};
        boolean hasAllRequirements = Requirement.hasAllCosts(player, requirements);
        if (!hasAllRequirements) {
            return clickResult;
        }

        // If all requirements are satisfied, take their cost
        boolean takenAllCosts = Requirement.takeAllCosts(player, requirements);
        if (!takenAllCosts) {
            return clickResult;
        }

        boolean hasOpenMenuAction = false;

        if (clickActions != null) {
            for (Action action : clickActions) {
                action.execute(player);

                if (action instanceof OpenMenuAction) {
                    hasOpenMenuAction = true;
                }
            }
        }

        // Update the menu after taking requirement costs and executing all actions
        menuView.refresh();

        // Force menu to stay open if actions open another menu
        if (hasOpenMenuAction) {
            return ClickResult.KEEP_OPEN;
        } else {
            return clickResult;
        }
    }

    @Override
    public @Nullable ItemStack updateRendering(Player viewer, @Nullable ItemStack currentRendering) {
        if (currentRendering != null && shouldCacheRendering()) {
            // Internal icons do not change, no need to update if the item is already rendered
            return currentRendering;
        }

        if (!canViewIcon(viewer)) {
            // Hide the current item
            return null;
        }

        if (currentRendering == null) {
            // Render item normally
            return render(viewer);
        } else {
            // Internal icons are loaded and then never change, we can safely update only name and lore (for performance)
            ItemMeta meta = currentRendering.getItemMeta();
            meta.setDisplayName(renderName(viewer));
            meta.setLore(renderLore(viewer));
            currentRendering.setItemMeta(meta);
            return currentRendering;
        }
    }

}

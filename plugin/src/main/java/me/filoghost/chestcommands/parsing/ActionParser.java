/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.filoghost.chestcommands.action.*;

public class ActionParser {

    public static Action parse(String serializedAction) throws ParseException {
        for (ActionType actionType : ActionType.values()) {
            Matcher matcher = actionType.prefixPattern.matcher(serializedAction);
            if (matcher.find()) {
                // Remove the action prefix and trim the spaces
                serializedAction = matcher.replaceFirst("").trim();
                return actionType.actionFactory.create(serializedAction);
            }
        }

        return new PlayerCommandAction(serializedAction); // Default action, no match found
    }


    private enum ActionType {

        CONSOLE_COMMAND("console", ConsoleCommandAction::new),
        OP_COMMAND("op", OpCommandAction::new),
        OPEN("open", OpenMenuAction::new),
        CLOSE_THIS("close-this", CloseThisMenuAction::new),
        SERVER("server", ChangeServerAction::new), // The colon is optional
        TELL("tell", SendMessageAction::new),
        BROADCAST("broadcast", BroadcastAction::new),
        GIVE_ITEM("give", GiveItemAction::new),
        GIVE_MONEY("give-money", GiveMoneyAction::new),
        SOUND("sound", PlaySoundAction::new),
        BOSS_BAR("dragon-bar", DragonBarAction::new);


        private final Pattern prefixPattern;
        private final ActionFactory actionFactory;


        ActionType(String prefix, ActionFactory actionFactory) {
            // Non-default actions must match the format "{prefix}: {content}"
            this.prefixPattern = Pattern.compile("^" + Pattern.quote(prefix) + ":", Pattern.CASE_INSENSITIVE);
            this.actionFactory = actionFactory;
        }

        @FunctionalInterface
        private interface ActionFactory {

            Action create(String serializedAction) throws ParseException;

        }

    }
}

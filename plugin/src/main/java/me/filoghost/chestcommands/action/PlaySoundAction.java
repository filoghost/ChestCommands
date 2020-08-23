/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.chestcommands.action;

import java.util.Optional;
import me.filoghost.chestcommands.logging.Errors;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.fcommons.Strings;
import me.filoghost.fcommons.collection.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class PlaySoundAction implements Action {
    
    private static final Registry<Sound> SOUNDS_REGISTRY = Registry.fromEnumValues(Sound.class);

    private final Sound sound;
    private final float pitch;
    private final float volume;

    public PlaySoundAction(String serializedAction) throws ParseException {
        String[] split = Strings.trimmedSplit(serializedAction, ",", 3);

        Optional<Sound> sound = SOUNDS_REGISTRY.find(split[0]);
        if (!sound.isPresent()) {
            throw new ParseException(Errors.Parsing.unknownSound(split[0]));
        }
        this.sound = sound.get();

        if (split.length > 1) {
            try {
                pitch = NumberParser.getFloat(split[1]);
            } catch (ParseException e) {
                throw new ParseException(Errors.Parsing.invalidSoundPitch(split[1]), e);
            }
        } else {
            pitch = 1.0f;
        }

        if (split.length > 2) {
            try {
                volume = NumberParser.getFloat(split[2]);
            } catch (ParseException e) {
                throw new ParseException(Errors.Parsing.invalidSoundVolume(split[2]), e);
            }
        } else {
            volume = 1.0f;
        }
    }

    @Override
    public void execute(Player player) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}

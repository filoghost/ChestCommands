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
package me.filoghost.chestcommands.action;

import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.NumberParser;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.util.Strings;
import me.filoghost.chestcommands.util.collection.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlaySoundAction implements Action {
	
	private static final Registry<Sound> SOUNDS_REGISTRY = Registry.fromEnumValues(Sound.class);

	private final Sound sound;
	private final float pitch;
	private final float volume;

	public PlaySoundAction(String serializedAction) throws ParseException {
		String[] split = Strings.trimmedSplit(serializedAction, ",", 3);

		Optional<Sound> sound = SOUNDS_REGISTRY.find(split[0]);
		if (!sound.isPresent()) {
			throw new ParseException(ErrorMessages.Parsing.unknownSound(split[0]));
		}
		this.sound = sound.get();

		if (split.length > 1) {
			try {
				pitch = NumberParser.getFloat(split[1]);
			} catch (ParseException e) {
				throw new ParseException(ErrorMessages.Parsing.invalidSoundPitch(split[1]), e);
			}
		} else {
			pitch = 1.0f;
		}

		if (split.length > 2) {
			try {
				volume = NumberParser.getFloat(split[2]);
			} catch (ParseException e) {
				throw new ParseException(ErrorMessages.Parsing.invalidSoundVolume(split[2]), e);
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

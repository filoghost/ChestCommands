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
package me.filoghost.chestcommands.action;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.filoghost.chestcommands.util.BukkitUtils;

public class PlaySoundAction extends Action {

	private Sound sound;
	private float pitch;
	private float volume;
	private String errorMessage;

	public PlaySoundAction(String action) {
		super(action);
		if (!hasVariables) {
			parseSound(super.action);
		}
	}

	private void parseSound(String action) {
		errorMessage = null;
		pitch = 1.0f;
		volume = 1.0f;

		String[] split = action.split(",");

		sound = BukkitUtils.matchSound(split[0]);
		if (sound == null) {
			errorMessage = ChatColor.RED + "Invalid sound \"" + split[0].trim() + "\".";
			return;
		}

		if (split.length > 1) {
			try {
				pitch = Float.parseFloat(split[1].trim());
			} catch (NumberFormatException e) {
			}
		}

		if (split.length > 2) {
			try {
				volume = Float.parseFloat(split[2].trim());
			} catch (NumberFormatException e) {
			}
		}
	}

	@Override
	public void execute(Player player) {
		if (hasVariables) {
			parseSound(getParsedAction(player));
		}
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
			return;
		}

		player.playSound(player.getLocation(), sound, volume, pitch);
	}

}

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
package me.filoghost.chestcommands.legacy;

import com.google.common.collect.ImmutableSet;
import me.filoghost.chestcommands.config.yaml.PluginConfig;

import java.util.Set;

public class LegacySettingsConverter implements ConfigConverter {

	private static final Set<String> removedConfigNodes = ImmutableSet.of(
		"use-only-commands-without-args",
		"use-console-colors",
		"multiple-commands-separator"
	);

	public String getLegacyCommandSeparator(PluginConfig settingsConfig) {
		return settingsConfig.getString("multiple-commands-separator");

	}

	@Override
	public boolean convert(PluginConfig settingsConfig) {
		 boolean modified = false;

		for (String removedConfigNode : removedConfigNodes) {
			if (settingsConfig.isSet(removedConfigNode)) {
				settingsConfig.set(removedConfigNode, null);
				modified = true;
			}
		}

		return modified;
	}

}

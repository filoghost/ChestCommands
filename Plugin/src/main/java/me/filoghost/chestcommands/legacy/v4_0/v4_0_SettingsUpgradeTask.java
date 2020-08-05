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
package me.filoghost.chestcommands.legacy.v4_0;

import me.filoghost.chestcommands.config.ConfigManager;
import me.filoghost.chestcommands.config.framework.Config;
import me.filoghost.chestcommands.legacy.upgrade.YamlUpgradeTask;

public class v4_0_SettingsUpgradeTask extends YamlUpgradeTask {

	public v4_0_SettingsUpgradeTask(ConfigManager configManager) {
		super(configManager.getConfigLoader("config.yml"));
	}

	@Override
	public void computeYamlChanges(Config settingsConfig) {
		removeNode(settingsConfig, "use-only-commands-without-args");
		removeNode(settingsConfig, "use-console-colors");
		removeNode(settingsConfig, "multiple-commands-separator");
	}

}

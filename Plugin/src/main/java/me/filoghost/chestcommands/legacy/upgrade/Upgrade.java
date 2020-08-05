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
package me.filoghost.chestcommands.legacy.upgrade;

import me.filoghost.chestcommands.config.ConfigManager;

import java.util.List;

public class Upgrade {

	private final String id;
	private final MultiTaskSupplier upgradeTasksSupplier;

	public Upgrade(String id, MultiTaskSupplier taskSupplier) {
		this.id = id;
		this.upgradeTasksSupplier = taskSupplier;
	}

	public String getID() {
		return id;
	}

	public List<UpgradeTask> createUpgradeTasks(ConfigManager configManager) throws UpgradeTaskException {
		return upgradeTasksSupplier.getTasks(configManager);
	}

	@FunctionalInterface
	public interface SingleTaskSupplier {

		UpgradeTask getTask(ConfigManager configManager) throws UpgradeTaskException;

	}

	@FunctionalInterface
	public interface MultiTaskSupplier {

		List<UpgradeTask> getTasks(ConfigManager configManager) throws UpgradeTaskException;

	}

}

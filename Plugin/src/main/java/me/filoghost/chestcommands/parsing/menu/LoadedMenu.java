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
package me.filoghost.chestcommands.parsing.menu;

import com.google.common.collect.ImmutableList;
import me.filoghost.chestcommands.menu.InternalIconMenu;
import me.filoghost.chestcommands.util.collection.CollectionUtils;

import java.nio.file.Path;
import java.util.List;

public class LoadedMenu {

	private final InternalIconMenu menu;
	private final Path sourceFile;
	private final ImmutableList<String> openCommands;
	private final MenuOpenItem openItem;

	public LoadedMenu(InternalIconMenu menu, Path menuFile, List<String> openCommands, MenuOpenItem openItem) {
		this.menu = menu;
		this.sourceFile = menuFile;
		this.openCommands = CollectionUtils.immutableCopy(openCommands);
		this.openItem = openItem;
	}

	public InternalIconMenu getMenu() {
		return menu;
	}

	public Path getSourceFile() {
		return sourceFile;
	}

	public ImmutableList<String> getOpenCommands() {
		return openCommands;
	}

	public MenuOpenItem getOpenItem() {
		return openItem;
	}

}

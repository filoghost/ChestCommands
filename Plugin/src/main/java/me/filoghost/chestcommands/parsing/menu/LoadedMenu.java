package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.menu.InternalIconMenu;

import java.nio.file.Path;
import java.util.List;

public class LoadedMenu {

	private final InternalIconMenu menu;
	private final Path sourceFile;
	private final List<String> openCommands;
	private final MenuOpenItem openItem;

	public LoadedMenu(InternalIconMenu menu, Path menuFile, List<String> openCommands, MenuOpenItem openItem) {
		this.menu = menu;
		this.sourceFile = menuFile;
		this.openCommands = openCommands;
		this.openItem = openItem;
	}

	public InternalIconMenu getMenu() {
		return menu;
	}

	public Path getSourceFile() {
		return sourceFile;
	}

	public List<String> getOpenCommands() {
		return openCommands;
	}

	public MenuOpenItem getOpenItem() {
		return openItem;
	}

}

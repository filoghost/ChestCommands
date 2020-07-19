package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.menu.InternalIconMenu;

import java.nio.file.Path;
import java.util.List;

public class LoadedMenu {

	private final InternalIconMenu menu;
	private final Path sourceFile;
	private final List<String> triggerCommands;
	private final OpenTrigger openTrigger;

	public LoadedMenu(InternalIconMenu menu, Path menuFile, List<String> triggerCommands, OpenTrigger openTrigger) {
		this.menu = menu;
		this.sourceFile = menuFile;
		this.triggerCommands = triggerCommands;
		this.openTrigger = openTrigger;
	}

	public InternalIconMenu getMenu() {
		return menu;
	}

	public Path getSourceFile() {
		return sourceFile;
	}

	public List<String> getTriggerCommands() {
		return triggerCommands;
	}

	public OpenTrigger getOpenTrigger() {
		return openTrigger;
	}

}

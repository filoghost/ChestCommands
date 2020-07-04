package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.menu.InternalIconMenu;

import java.util.List;

public class LoadedMenu {

	private final InternalIconMenu menu;
	private final String fileName;
	private final List<String> triggerCommands;
	private final OpenTrigger openTrigger;

	public LoadedMenu(InternalIconMenu menu, String fileName, List<String> triggerCommands, OpenTrigger openTrigger) {
		this.menu = menu;
		this.fileName = fileName;
		this.triggerCommands = triggerCommands;
		this.openTrigger = openTrigger;
	}

	public InternalIconMenu getMenu() {
		return menu;
	}

	public String getFileName() {
		return fileName;
	}

	public List<String> getTriggerCommands() {
		return triggerCommands;
	}

	public OpenTrigger getOpenTrigger() {
		return openTrigger;
	}

}

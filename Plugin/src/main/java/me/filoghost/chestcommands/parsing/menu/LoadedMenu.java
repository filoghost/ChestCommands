package me.filoghost.chestcommands.parsing.menu;

import me.filoghost.chestcommands.menu.AdvancedIconMenu;

import java.util.List;

public class LoadedMenu {

	private final AdvancedIconMenu menu;
	private final String fileName;
	private final List<String> triggerCommands;
	private final OpenTrigger openTrigger;

	public LoadedMenu(AdvancedIconMenu menu, String fileName, List<String> triggerCommands, OpenTrigger openTrigger) {
		this.menu = menu;
		this.fileName = fileName;
		this.triggerCommands = triggerCommands;
		this.openTrigger = openTrigger;
	}

	public AdvancedIconMenu getMenu() {
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

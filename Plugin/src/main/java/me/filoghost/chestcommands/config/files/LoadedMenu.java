package me.filoghost.chestcommands.config.files;

import me.filoghost.chestcommands.menu.AdvancedIconMenu;
import me.filoghost.chestcommands.menu.settings.MenuSettings;

public class LoadedMenu {

	private final String fileName;
	private final MenuSettings settings;
	private final AdvancedIconMenu menu;

	public LoadedMenu(String fileName, MenuSettings settings, AdvancedIconMenu menu) {
		this.fileName = fileName;
		this.settings = settings;
		this.menu = menu;
	}

	public String getFileName() {
		return fileName;
	}

	public MenuSettings getSettings() {
		return settings;
	}

	public AdvancedIconMenu getMenu() {
		return menu;
	}

}

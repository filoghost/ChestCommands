package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.parsing.ParseException;
import me.filoghost.chestcommands.util.logging.ErrorCollector;

public class AttributeErrorCollector {

	private final ErrorCollector errorCollector;
	private final IconSettings iconSettings;
	private final String attributeName;

	public AttributeErrorCollector(ErrorCollector errorCollector, IconSettings iconSettings, String attributeName) {
		this.errorCollector = errorCollector;
		this.iconSettings = iconSettings;
		this.attributeName = attributeName;
	}

	public void addListElementError(String listElement, ParseException e) {
		errorCollector.add(ErrorMessages.Menu.invalidAttributeListElement(iconSettings, attributeName, listElement), e);
	}

}

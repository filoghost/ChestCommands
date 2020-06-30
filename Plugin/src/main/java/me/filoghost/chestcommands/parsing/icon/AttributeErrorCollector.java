package me.filoghost.chestcommands.parsing.icon;

import me.filoghost.chestcommands.parsing.ErrorFormat;
import me.filoghost.chestcommands.util.ErrorCollector;

public class AttributeErrorCollector {

	private final ErrorCollector errorCollector;
	private final IconSettings iconSettings;
	private final String attributeName;

	public AttributeErrorCollector(ErrorCollector errorCollector, IconSettings iconSettings, String attributeName) {
		this.errorCollector = errorCollector;
		this.iconSettings = iconSettings;
		this.attributeName = attributeName;
	}

	public void addAttributeError(Exception e) {
		errorCollector.addError(ErrorFormat.invalidAttribute(iconSettings, attributeName, e.getMessage()));
	}

	public void addListElementError(String listElement, Exception e) {
		errorCollector.addError(ErrorFormat.invalidListElement(iconSettings, attributeName, listElement, e.getMessage()));
	}

}

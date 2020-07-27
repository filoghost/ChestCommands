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
package me.filoghost.chestcommands.placeholder.scanner;

import me.filoghost.chestcommands.util.Strings;

import java.util.Objects;

public class PlaceholderMatch {

	private final String pluginNamespace;
	private final String identifier;
	private final String argument;

	private PlaceholderMatch(String pluginNamespace, String identifier, String argument) {
		this.pluginNamespace = pluginNamespace;
		this.identifier = identifier;
		this.argument = argument;
	}

	public String getPluginNamespace() {
		return pluginNamespace;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getArgument() {
		return argument;
	}

	/**
	 * Expected format: {pluginName/placeholder: argument}
	 */
	public static PlaceholderMatch parse(String placeholderContent) {
		String explicitPluginName = null;
		String identifier;
		String argument = null;

		if (placeholderContent.contains(":")) {
			String[] parts = Strings.trimmedSplit(placeholderContent, ":", 2);
			identifier = parts[0];
			argument = parts[1];
		} else {
			identifier = placeholderContent;
		}

		if (identifier.contains("/")) {
			String[] parts = Strings.trimmedSplit(identifier, "\\/", 2);
			identifier = parts[0];
			explicitPluginName = parts[1];
		}

		return new PlaceholderMatch(explicitPluginName, identifier, argument);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		PlaceholderMatch other = (PlaceholderMatch) obj;
		return Objects.equals(this.pluginNamespace, other.pluginNamespace) &&
				Objects.equals(this.identifier, other.identifier) &&
				Objects.equals(this.argument, other.argument);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pluginNamespace, identifier, argument);
	}

}

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

import me.filoghost.chestcommands.config.framework.exception.ConfigLoadException;
import me.filoghost.chestcommands.config.framework.exception.ConfigSaveException;
import me.filoghost.chestcommands.legacy.RegexReplacer;
import me.filoghost.chestcommands.logging.ErrorMessages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RegexUpgrade extends Upgrade {

	private final Path file;
	private final List<RegexReplacer> replacers;
	private List<String> newContents;

	public RegexUpgrade(Path file) {
		this.file = file;
		this.replacers = new ArrayList<>();
	}

	protected void addRegexReplacer(Pattern regex, Function<Matcher, String> replaceCallback) {
		replacers.add(new RegexReplacer(regex, replaceCallback));
	}

	@Override
	public Path getOriginalFile() {
		return file;
	}

	@Override
	public Path getUpgradedFile() {
		return file;
	}

	@Override
	protected void computeChanges() throws ConfigLoadException {
		if (!Files.isRegularFile(file)) {
			return;
		}

		List<String> lines;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			throw new ConfigLoadException(ErrorMessages.Config.readIOException, e);
		}

		Stream<String> linesStream = lines.stream();
		for (RegexReplacer replacer : replacers) {
			linesStream = linesStream.map(replacer);
		}

		newContents = linesStream.collect(Collectors.toList());

		if (!newContents.equals(lines)) {
			setModified();
		}
	}

	@Override
	protected void saveChanges() throws ConfigSaveException {
		try {
			Files.write(file, newContents);
		} catch (IOException e) {
			throw new ConfigSaveException(ErrorMessages.Config.writeDataIOException, e);
		}
	}

}

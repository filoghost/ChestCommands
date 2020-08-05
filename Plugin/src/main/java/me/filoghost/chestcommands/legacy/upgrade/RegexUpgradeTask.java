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
import me.filoghost.chestcommands.logging.ErrorMessages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RegexUpgradeTask extends UpgradeTask {

	private final Path file;
	private List<String> newContents;
	private Stream<String> linesStream;

	public RegexUpgradeTask(Path file) {
		this.file = file;
	}

	@Override
	public final Path getOriginalFile() {
		return file;
	}

	@Override
	public final Path getUpgradedFile() {
		return file;
	}

	@Override
	public final void computeChanges() throws ConfigLoadException {
		if (!Files.isRegularFile(file)) {
			return;
		}

		List<String> lines;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			throw new ConfigLoadException(ErrorMessages.Config.readIOException, e);
		}

		this.linesStream = lines.stream();
		computeRegexChanges();

		newContents = linesStream.collect(Collectors.toList());

		if (!newContents.equals(lines)) {
			setSaveRequired();
		}
	}

	@Override
	public final void saveChanges() throws ConfigSaveException {
		try {
			Files.write(file, newContents);
		} catch (IOException e) {
			throw new ConfigSaveException(ErrorMessages.Config.writeDataIOException, e);
		}
	}

	protected abstract void computeRegexChanges();

	protected void replaceString(String target, String replacement) {
		replaceRegex(
				Pattern.compile(Pattern.quote(target)),
				matcher -> replacement
		);
	}

	protected void replaceSubNode(String oldNode, String newNode) {
		replaceRegex(
				Pattern.compile("(^\\s+)" + Pattern.quote(oldNode) + "(:)"),
				matcher -> matcher.group(1) + newNode + matcher.group(2)
		);
	}

	protected void replaceRegex(Pattern regex, Function<Matcher, String> replaceCallback) {
		linesStream = linesStream.map(new RegexReplacer(regex, replaceCallback));
	}

}

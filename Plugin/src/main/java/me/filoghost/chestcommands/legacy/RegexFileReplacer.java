/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.filoghost.chestcommands.legacy;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexFileReplacer {

	private final Pattern regex;
	private final Function<Matcher, String> replaceCallback;

	public RegexFileReplacer(Pattern regex, Function<Matcher, String> replaceCallback) {
		this.regex = regex;
		this.replaceCallback = replaceCallback;
	}

	public List<String> replace(List<String> lines) {
		return lines.stream().map(this::replace).collect(Collectors.toList());
	}

	private String replace(String line) {
		Matcher matcher = regex.matcher(line);
		StringBuffer output = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(output, replaceCallback.apply(matcher));
		}
		matcher.appendTail(output);

		return output.toString();
	}

}

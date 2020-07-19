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
package me.filoghost.chestcommands.parsing;

import me.filoghost.chestcommands.logging.ErrorMessages;
import me.filoghost.chestcommands.util.Strings;
import me.filoghost.chestcommands.util.collection.Registry;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public final class ItemMetaParser {
	
	private static final Registry<DyeColor> DYE_COLORS_REGISTRY = Registry.fromEnumValues(DyeColor.class);
	private static final Registry<PatternType> PATTERN_TYPES_REGISTRY = Registry.fromEnumValues(PatternType.class);

	private ItemMetaParser() {}

	
	public static Color parseRGBColor(String input) throws ParseException {
		String[] split = Strings.trimmedSplit(input, ",");

		if (split.length != 3) {
			throw new ParseException(ErrorMessages.Parsing.invalidColorFormat);
		}

		int red = parseColor(split[0], "red");
		int green = parseColor(split[1], "green");
		int blue = parseColor(split[2], "blue");

		return Color.fromRGB(red, green, blue);
	}

	private static int parseColor(String valueString, String colorName) throws ParseException {
		int value;

		try {
			value = NumberParser.getInteger(valueString);
		} catch (ParseException e) {
			throw new ParseException(ErrorMessages.Parsing.invalidColorNumber(valueString, colorName), e);
		}

		if (value < 0 || value > 255) {
			throw new ParseException(ErrorMessages.Parsing.invalidColorRange(valueString, colorName));
		}

		return value;
	}

	public static DyeColor parseDyeColor(String input) throws ParseException {
		return DYE_COLORS_REGISTRY.find(input)
				.orElseThrow(() -> new ParseException(ErrorMessages.Parsing.unknownDyeColor(input)));
	}

	public static Pattern parseBannerPattern(String input) throws ParseException {
		String[] split = Strings.trimmedSplit(input, ":");
		if (split.length != 2) {
			throw new ParseException(ErrorMessages.Parsing.invalidPatternFormat);
		}

		PatternType patternType = PATTERN_TYPES_REGISTRY.find(split[0])
				.orElseThrow(() -> new ParseException(ErrorMessages.Parsing.unknownPatternType(split[0])));
		DyeColor patternColor = parseDyeColor(split[1]);

		return new Pattern(patternColor, patternType);
	}
}

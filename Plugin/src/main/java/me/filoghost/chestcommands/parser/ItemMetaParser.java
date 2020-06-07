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
package me.filoghost.chestcommands.parser;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import me.filoghost.chestcommands.exception.FormatException;
import me.filoghost.chestcommands.util.Registry;
import java.util.ArrayList;
import java.util.List;

public final class ItemMetaParser {
	
	private static Registry<DyeColor> DYE_COLORS_REGISTRY = Registry.fromEnumValues(DyeColor.class);
	private static Registry<PatternType> PATTERN_TYPES_REGISTRY = Registry.fromEnumValues(PatternType.class);

	private ItemMetaParser() {}

	
	public static Color parseColor(String input) throws FormatException {
		String[] split = input.replace(" ", "").split(",");

		if (split.length != 3) {
			throw new FormatException("it must be in the format \"red, green, blue\".");
		}

		int red, green, blue;

		try {
			red = Integer.parseInt(split[0]);
			green = Integer.parseInt(split[1]);
			blue = Integer.parseInt(split[2]);
		} catch (NumberFormatException ex) {
			throw new FormatException("it contains invalid numbers.");
		}

		if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
			throw new FormatException("it should only contain numbers between 0 and 255.");
		}

		return Color.fromRGB(red, green, blue);
	}

	public static DyeColor parseDyeColor(String input) throws FormatException {
		DyeColor color = DYE_COLORS_REGISTRY.find(input);
		
		if (color == null) {
			throw new FormatException("it must be a valid color.");
		}
		return color;
	}

	public static List<Pattern> parseBannerPatternList(List<String> input) throws FormatException {
		List<Pattern> patterns = new ArrayList<Pattern>();
		for (String str : input) {
			String[] split = str.split(":");
			if (split.length != 2) {
				throw new FormatException("it must be in the format \"pattern:color\".");
			}
			
			PatternType patternType = PATTERN_TYPES_REGISTRY.find(split[0]);
			DyeColor patternColor = parseDyeColor(split[1]);
			
			if (patternType == null) {
				throw new FormatException("it must be a valid pattern type.");
			}
			
			patterns.add(new Pattern(patternColor, patternType));
		}
		return patterns;
	}
}

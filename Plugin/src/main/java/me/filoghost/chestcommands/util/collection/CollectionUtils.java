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
package me.filoghost.chestcommands.util.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class CollectionUtils {

	private CollectionUtils() {}

	public static boolean isNullOrEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static <E> List<E> nullableCopy(List<E> list) {
		if (isNullOrEmpty(list)) {
			return null;
		} else {
			return new ArrayList<>(list);
		}
	}
	
	public static <K, V> Map<K, V> nullableCopy(Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return null;
		} else {
			return new HashMap<>(map);
		}
	}

	public static <A, B> List<B> transform(List<A> list, Function<A, B> transformFunction) {
		if (list == null) {
			return null;
		}
		List<B> result = new ArrayList<>(list.size());
		for (A element : list) {
			result.add(transformFunction.apply(element));
		}
		return result;
	}

}

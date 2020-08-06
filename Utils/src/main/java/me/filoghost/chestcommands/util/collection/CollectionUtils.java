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
package me.filoghost.chestcommands.util.collection;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class CollectionUtils {

	public static <E> List<E> copy(List<E> list) {
		if (list != null) {
			return new ArrayList<>(list);
		} else {
			return null;
		}
	}

	public static <K, V> Map<K, V> copy(Map<K, V> map) {
		if (map != null) {
			return new HashMap<>(map);
		} else {
			return null;
		}
	}

	public static <E> ImmutableList<E> immutableCopy(List<E> list) {
		if (list != null) {
			return ImmutableList.copyOf(list);
		} else {
			return null;
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

	public static <A, B> ImmutableList<B> transformImmutable(List<A> list, Function<A, B> transformFunction) {
		if (list == null) {
			return null;
		}
		ImmutableList.Builder<B> builder = ImmutableList.builder();
		for (A element : list) {
			builder.add(transformFunction.apply(element));
		}
		return builder.build();
	}

	public static <E> List<E> replaceNulls(List<E> list, E replacement) {
		return transform(list, element -> element != null ? element : replacement);
	}

}

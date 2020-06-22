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
package me.filoghost.chestcommands.util;

import java.util.HashMap;
import java.util.Map;

public class CaseInsensitiveMap<V> extends HashMap<String, V> {

	private static final long serialVersionUID = 4712822893326841081L;

	public static <K> CaseInsensitiveMap<K> create() {
		return new CaseInsensitiveMap<>();
	}

	@Override
	public V put(String key, V value) {
		return super.put(key.toLowerCase(), value);
	}

	@Override
	public V get(Object key) {
		return super.get(key.getClass() == String.class ? key.toString().toLowerCase() : key);
	}

	public String getKey(V value) {
		for (java.util.Map.Entry<String, V> entry : entrySet()) {
			if (entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}

		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key.getClass() == String.class ? key.toString().toLowerCase() : key);
	}

	@Override
	public V remove(Object key) {
		return super.remove(key.getClass() == String.class ? key.toString().toLowerCase() : key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> m) {
		throw new UnsupportedOperationException("putAll not supported");
	}

}

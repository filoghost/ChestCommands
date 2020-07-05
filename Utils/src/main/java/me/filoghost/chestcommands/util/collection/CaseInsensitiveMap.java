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

import me.filoghost.chestcommands.util.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMap<V> implements Map<String, V> {

	private final Map<String, V> delegate;

	public CaseInsensitiveMap() {
		this.delegate = new HashMap<>();
	}

	@Override
	public V put(String key, V value) {
		return delegate.put(getLowercaseKey(key), value);
	}

	@Override
	public V get(Object key) {
		return delegate.get(getLowercaseKey(key));
	}

	@Override
	public boolean containsKey(Object key) {
		return delegate.containsKey(getLowercaseKey(key));
	}

	@Override
	public V remove(Object key) {
		return delegate.remove(getLowercaseKey(key));
	}

	@Override
	public int size() {
		return delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	@Override
	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends V> map) {
		map.forEach(this::put);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public Set<String> keySet() {
		return Collections.unmodifiableSet(delegate.keySet());
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableCollection(delegate.values());
	}

	@Override
	public Set<Entry<String, V>> entrySet() {
		return Collections.unmodifiableSet(delegate.entrySet());
	}

	private String getLowercaseKey(Object key) {
		Preconditions.notNull(key, "key");
		Preconditions.checkArgument(key instanceof String, "key must be a string");
		return ((String) key).toLowerCase();
	}

}

package me.filoghost.chestcommands.placeholder;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import me.filoghost.chestcommands.util.collection.CollectionUtils;

public class RelativeStringList {

	private final ImmutableList<String> originalList;
	private final List<RelativeString> relativeList;
	private final boolean hasPlaceholders;
	
	public RelativeStringList(List<String> list) {
		if (list != null) {
			this.originalList = ImmutableList.copyOf(list);
			this.relativeList = CollectionUtils.transform(list, RelativeString::of);
			this.hasPlaceholders = this.relativeList.stream().anyMatch(RelativeString::hasPlaceholders);
		} else {
			this.originalList = null;
			this.relativeList = null;
			this.hasPlaceholders = false;
		}
	}
	
	public ImmutableList<String> getRawValue() {
		return originalList;
	}
	
	public List<String> getValue(Player player) {
		if (hasPlaceholders) {
			return CollectionUtils.transform(relativeList, element -> element.getValue(player));
		} else {
			return originalList;
		}
	}
	
	public boolean hasPlaceholders() {
		return hasPlaceholders;
	}

}

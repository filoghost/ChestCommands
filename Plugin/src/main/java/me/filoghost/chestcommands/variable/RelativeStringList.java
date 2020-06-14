package me.filoghost.chestcommands.variable;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import me.filoghost.chestcommands.util.Utils;

public class RelativeStringList {

	private final ImmutableList<String> originalList;
	private final List<RelativeString> relativeList;
	private final boolean hasVariables;
	
	public RelativeStringList(List<String> list) {
		if (list != null) {
			this.originalList = ImmutableList.copyOf(list);
			this.relativeList = Utils.transform(list, RelativeString::of);
			this.hasVariables = this.relativeList.stream().anyMatch(element -> element.hasVariables());
		} else {
			this.originalList = null;
			this.relativeList = null;
			this.hasVariables = false;
		}
	}
	
	public ImmutableList<String> getRawValue() {
		return originalList;
	}
	
	public List<String> getValue(Player player) {
		if (hasVariables) {
			return Utils.transform(relativeList, element -> element.getValue(player));
		} else {
			return originalList;
		}
	}
	
	public boolean hasVariables() {
		return hasVariables;
	}

}

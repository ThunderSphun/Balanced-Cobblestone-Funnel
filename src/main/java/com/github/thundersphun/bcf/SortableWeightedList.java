package com.github.thundersphun.bcf;

import net.minecraft.util.collection.WeightedList;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public class SortableWeightedList<U> extends WeightedList<U> {
	public SortableWeightedList() {
		super();
	}

	public List<Entry<U>> sort() {
		return entries.stream().sorted(Comparator.comparingInt(Entry::getWeight)).toList();
	}

	public U getRandom() {
		return shuffle().stream().toList().get(0);
	}

	public void foreach(BiConsumer<? super U, Integer> action) {
		entries.forEach(e -> action.accept(e.getElement(), e.getWeight()));
	}
}

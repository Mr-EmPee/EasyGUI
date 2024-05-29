package io.github.empee.easygui.model.inventories.containers;

import io.github.empee.easygui.guis.inventories.InventoryGUI;
import io.github.empee.easygui.model.inventories.Item;
import io.github.empee.easygui.model.inventories.Slot;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ScrollableContainer {

  private final InventoryGUI gui;

  private final Slot.Generator slotProvider;
  private final BiFunction<Integer, Integer, List<Item>> itemsProvider;
  private final Supplier<Integer> totalProvider;

  private List<Item> loadedItems = new ArrayList<>();
  private List<Slot> loadedSlots = new ArrayList<>();

  @Getter
  private int offset = 0;
  private Integer totalItems;

  public ScrollableContainer(
      InventoryGUI gui, Slot.Generator slotProvider,
      BiFunction<Integer, Integer, List<Item>> itemsProvider, Supplier<Integer> totalProvider
  ) {
    this.gui = gui;
    this.slotProvider = slotProvider;
    this.itemsProvider = itemsProvider;
    this.totalProvider = totalProvider;

    update();
  }

  public static ScrollableContainer fromCollection(InventoryGUI gui, List<Slot> slots, Collection<Item> content) {
    return fromCollection(gui, Slot.Generator.of(slots), content);
  }

  public static ScrollableContainer fromCollection(InventoryGUI gui, Slot.Generator generator, Collection<Item> content) {
    var items = new ArrayList<>(content);
    items.sort(Comparator.reverseOrder());

    Supplier<Integer> totalProvider = () -> {
      return (int) items.stream()
          .filter(Item::isVisible)
          .count();
    };

    BiFunction<Integer, Integer, List<Item>> itemsProvider = (offset, limit) -> {
      var visibleContent = items.stream()
          .filter(Item::isVisible)
          .toList();

      if (visibleContent.isEmpty()) {
        return Collections.emptyList();
      }

      int end = offset + limit;
      if (end > visibleContent.size()) {
        end = visibleContent.size();
      }

      return visibleContent.subList(offset, end);
    };

    return new ScrollableContainer(gui, generator, itemsProvider, totalProvider);
  }

  public boolean hasNext() {
    return offset + loadedSlots.size() < getTotalItems();
  }

  public boolean hasPrevious() {
    return offset - loadedSlots.size() >= 0;
  }

  public void next() {
    setOffset(offset + loadedSlots.size());
  }

  public void previous() {
    setOffset(offset - loadedSlots.size());
  }

  public void setOffset(int offset) {
    if (offset < 0 || offset >= getTotalItems()) {
      throw new IllegalArgumentException("Offset is out of bounds");
    }

    this.offset = offset;

    update();
    gui.updateInventory();
  }

  private void update() {
    totalItems = null;

    gui.remove(loadedItems);

    loadedSlots = slotProvider.generate(offset);
    loadedItems = itemsProvider.apply(offset, loadedSlots.size());

    for (int i=0; i<loadedItems.size(); i++) {
      loadedItems.get(i).slot(loadedSlots.get(i));
    }

    gui.inserts(loadedItems);
  }

  public int getTotalItems() {
    if (totalItems == null) {
      totalItems = totalProvider.get();
    }

    return totalItems;
  }

}

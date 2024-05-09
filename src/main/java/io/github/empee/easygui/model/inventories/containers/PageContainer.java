package io.github.empee.easygui.model.inventories.containers;

import io.github.empee.easygui.model.inventories.Item;
import io.github.empee.easygui.model.inventories.Slot;
import io.github.empee.easygui.guis.inventories.InventoryGUI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class PageContainer implements InventoryContainer {

  private final InventoryGUI gui;
  private final List<Slot> view;

  private final List<Item> content = new ArrayList<>();

  @Setter
  private int page = 0;

  public static PageContainer of(InventoryGUI gui, List<Slot> view, List<Item> items) {
    var container = new PageContainer(gui, view);
    container.addContent(items);
    return container;
  }

  public void addContent(List<Item> items) {
    items.forEach(Item::removeSlot);
    content.addAll(items);
    gui.addItems(items);
  }

  public void update() {
    content.forEach(Item::removeSlot);

    int offset = view.size() * page;
    for (int i = 0; i< view.size(); i++) {
      Item item = null;

      //Get first visible item
      while (item == null && i + offset < content.size()) {
        item = content.get(i + offset);
        if (item != null && item.isHidden()) {
          item = null;
        }

        if (item == null) {
          offset += 1;
        }
      }

      if (item == null) {
        break;
      }

      item.slot(view.get(i));
    }

    gui.updateInventory();
  }

  public int getTotalPages() {
    return (int) Math.ceil((double) content.size() / view.size());
  }

  public void next() {
    page += 1;
  }

  public void previous() {
    page -= 1;
  }

  public boolean hasNext() {
    return (page + 1) * view.size() < content.size();
  }

  public boolean hasPrevious() {
    return page > 0;
  }

}

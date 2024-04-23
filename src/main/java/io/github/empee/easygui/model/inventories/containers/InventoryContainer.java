package io.github.empee.easygui.model.inventories.containers;

import io.github.empee.easygui.model.inventories.Item;

import java.util.List;

public interface InventoryContainer {

  void update();

  void addContent(List<Item> items);

}

package io.github.empee.easygui.guis.inventories;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor(staticName = "of")
public class ChestGUI extends InventoryGUI {

  @Getter
  private final int height;

  public int getLength() {
    return 9;
  }

  protected Inventory createInventory() {
    return Bukkit.createInventory(this, 9 * height, title);
  }
}

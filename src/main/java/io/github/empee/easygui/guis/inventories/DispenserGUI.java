package io.github.empee.easygui.guis.inventories;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor(staticName = "of")
public class DispenserGUI extends InventoryGUI {

  public int getLength() {
    return 3;
  }

  @Override
  public int getHeight() {
    return 3;
  }

  protected Inventory createInventory() {
    return Bukkit.createInventory(this, InventoryType.DISPENSER, title);
  }
}

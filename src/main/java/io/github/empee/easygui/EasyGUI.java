package io.github.empee.easygui;

import io.github.empee.easygui.guis.inventories.InventoryGUI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EasyGUI implements Listener {

  public static void init(JavaPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(new EasyGUI(), plugin);
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClick(InventoryClickEvent event) {
    var holder = event.getInventory().getHolder();
    if (holder instanceof InventoryGUI) {
      ((InventoryGUI) holder).onClick(event);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryDrag(InventoryDragEvent event) {
    var holder = event.getInventory().getHolder();
    if (holder instanceof InventoryGUI) {
      ((InventoryGUI) holder).onDrag(event);
    }
  }

  @EventHandler(ignoreCancelled = true)
  public void onInventoryClose(InventoryCloseEvent event) {
    var holder = event.getInventory().getHolder();
    if (holder instanceof InventoryGUI) {
      ((InventoryGUI) holder).onClose(event);
    }
  }

}

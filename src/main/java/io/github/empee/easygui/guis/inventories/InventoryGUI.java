package io.github.empee.easygui.guis.inventories;

import io.github.empee.easygui.guis.GUI;
import io.github.empee.easygui.model.inventories.Item;
import io.github.empee.easygui.model.inventories.Slot;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor(staticName = "of")
public class InventoryGUI implements GUI, InventoryHolder {

  private String name = " ";
  private boolean denyInteract = true;

  private final Player player;
  private final int height;
  protected int length = 9;

  @Setter(AccessLevel.NONE)
  private Inventory inventory;

  @Getter(AccessLevel.NONE)
  private final List<Item> content = new ArrayList<>();

  public final void addItem(Item item) {
    content.add(item);
    content.sort(Comparator.reverseOrder());
  }

  public final void addItems(List<Item> items) {
    content.addAll(items);
    content.sort(Comparator.reverseOrder());
  }

  public final Item getItem(int row, int col) {
    var slot = Slot.of(row, col);

    for (var item : content) {
      if (slot.equals(item.getSlot())) {
        return item;
      }
    }

    return Item.DEFAULT;
  }

  public final List<Item> getItems(int row, int col) {
    var slot = Slot.of(row, col);

    return content.stream()
        .filter(s -> slot.equals(s.getSlot()))
        .toList();
  }

  private void onClick(InventoryClickEvent event) {
    if (denyInteract) {
      event.setCancelled(true);
    }

    boolean clickedCustomInventory = inventory.equals(event.getClickedInventory());
    if (!clickedCustomInventory) {
      return;
    }

    var row = event.getSlot() / length;
    var col = event.getSlot() % length;

    var item = getItem(row, col);
    if (item.isHidden()) {
      item = Item.DEFAULT;
    }

    item.onClick(event);
  }

  private void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }

  public final void updateInventory() {
    ItemStack[] itemStacks = new ItemStack[inventory.getSize()];
    for (int i=0; i<itemStacks.length; i++) {
      var row = i / length;
      var col = i % length;

      var item = getItem(row, col);
      if (item.isHidden()) {
        item = Item.DEFAULT;
      }

      itemStacks[i] = item.getItemstack();
    }

    inventory.setContents(itemStacks);
  }

  public final Inventory getInventory() {
    if (inventory == null) {
      this.inventory = Bukkit.createInventory(this, length * height, this.name);

      updateInventory();
    }

    return inventory;
  }

  public void open() {
    player.openInventory(getInventory());
  }

  public static class Listeners implements Listener {
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
  }
}

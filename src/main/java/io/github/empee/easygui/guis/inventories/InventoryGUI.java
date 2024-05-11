package io.github.empee.easygui.guis.inventories;

import io.github.empee.easygui.model.inventories.Item;
import io.github.empee.easygui.model.inventories.Slot;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

@Accessors(fluent = true, chain = true)
public abstract class InventoryGUI implements InventoryHolder {

  private final List<Item> content = new ArrayList<>();
  private Inventory inventory;

  @Setter @Getter
  protected String title = "";

  @Setter @Getter
  protected Consumer<InventoryCloseEvent> closeHandler;
  @Setter @Getter
  protected Consumer<InventoryClickEvent> clickHandler;
  @Setter @Getter
  protected Consumer<InventoryDragEvent> dragHandler;

  public abstract int getLength();
  public abstract int getHeight();

  protected abstract Inventory createInventory();

  public void inserts(Item... item) {
    inserts(List.of(item));
  }

  public void inserts(List<Item> items) {
    content.addAll(items);
    content.sort(Comparator.reverseOrder());
  }

  public Item getItem(int row, int col) {
    var slot = Slot.of(row, col);

    for (var item : content) {
      if (slot.equals(item.getSlot())) {
        return item;
      }
    }

    return Item.DEFAULT;
  }

  public final void updateInventory() {
    ItemStack[] itemStacks = new ItemStack[inventory.getSize()];
    for (int i=0; i<itemStacks.length; i++) {
      var row = i / getLength();
      var col = i % getLength();

      var item = getItem(row, col);
      if (item.isHidden()) {
        item = Item.DEFAULT;
      }

      itemStacks[i] = item.getItemstack();
    }

    inventory.setContents(itemStacks);
  }

  public Inventory getInventory() {
    if (inventory == null) {
      inventory = createInventory();
      updateInventory();
    }

    return inventory;
  }

  public void onClose(InventoryCloseEvent event) {
    if (closeHandler != null) {
      closeHandler.accept(event);
    }
  }

  public void onClick(InventoryClickEvent event) {
    event.setCancelled(true);
    if (clickHandler != null) {
      clickHandler.accept(event);
    }

    if (inventory != event.getClickedInventory()) {
      return;
    }

    var row = event.getSlot() / getLength();
    var col = event.getSlot() % getLength();

    var item = getItem(row, col);
    if (item.isHidden()) {
      item = Item.DEFAULT;
    }

    item.onClick(event);
  }

  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
    if (dragHandler != null) {
      dragHandler.accept(event);
    }
  }

  public void open(Player player) {
    player.openInventory(getInventory());
  }

}

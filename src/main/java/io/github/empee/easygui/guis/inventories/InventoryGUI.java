package io.github.empee.easygui.guis.inventories;

import io.github.empee.easygui.model.inventories.Item;
import io.github.empee.easygui.model.inventories.Slot;
import io.github.empee.easygui.utils.InventoryUtils;
import io.github.empee.easygui.utils.ServerVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Accessors(fluent = true, chain = true)
public abstract class InventoryGUI implements InventoryHolder {

  private Inventory inventory;

  private final List<Item> content = new ArrayList<>();

  @Getter
  protected Supplier<String> title = () -> "";

  @Setter
  @Getter
  protected Consumer<InventoryCloseEvent> closeHandler;
  @Setter
  @Getter
  protected Consumer<InventoryClickEvent> clickHandler;
  @Setter
  @Getter
  protected Consumer<InventoryDragEvent> dragHandler;

  public abstract int getLength();

  public abstract int getHeight();

  protected abstract Inventory createInventory();

  public void title(String title) {
    title(() -> title);
  }

  public void title(Supplier<String> title) {
    this.title = title;
  }

  public void inserts(Item... items) {
    inserts(List.of(items));
  }

  public void inserts(List<Item> items) {
    content.addAll(items);
  }

  public void remove(List<Item> items) {
    content.removeAll(items);
  }

  public List<Item> getItems(@Nullable Slot slot) {
    return content.stream()
        .filter(item -> Objects.equals(slot, item.getSlot()))
        .collect(Collectors.toList());
  }

  public Item getItem(Slot slot) {
    return getItems(slot).stream()
        .filter(Item::isVisible)
        .max(Comparator.naturalOrder())
        .orElse(Item.DEFAULT);
  }

  public final void update() {
    if (ServerVersion.isGreaterThan(1, 20)) {
      updateTitle();
    }

    updateContent();
  }

  private void updateTitle() {
    inventory.getViewers().forEach(player -> {
      var view = player.getOpenInventory();
      if (view.getTitle().equals(title.get())) {
        return;
      }

      InventoryUtils.setTitle(view, title.get());
    });
  }

  private void updateContent() {
    ItemStack[] content = new ItemStack[inventory.getSize()];
    for (int i = 0; i < content.length; i++) {
      var row = i / getLength();
      var col = i % getLength();

      var item = getItem(Slot.of(row, col));

      content[i] = item.getItemstack();
    }

    inventory.setContents(content);
  }

  public Inventory getInventory() {
    if (inventory == null) {
      inventory = createInventory();
      update();
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

    var item = getItem(Slot.of(row, col));

    item.onClick(event);
  }

  public void onDrag(InventoryDragEvent event) {
    event.setCancelled(true);
    if (dragHandler != null) {
      dragHandler.accept(event);
    }
  }

  public void open(HumanEntity player) {
    player.openInventory(getInventory());
  }

}

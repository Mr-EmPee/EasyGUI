package io.github.empee.easygui.model.inventories;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor(staticName = "of")
public class Item implements Comparable<Item> {

  public static final Item DEFAULT = Item.of(new ItemStack(Material.AIR)).setPriority(0);

  private final Supplier<ItemStack> itemstack;
  private Supplier<Integer> priority;
  private Consumer<InventoryClickEvent> clickHandler;
  private Supplier<Slot> slot;

  public static Item of(ItemStack item) {
    return of(() -> item);
  }

  public static Item copy(Item item) {
    var copy = new Item(item.itemstack);

    copy.priority = item.priority;
    copy.slot = item.slot;
    copy.clickHandler = item.clickHandler;

    return copy;
  }

  @Contract("_ -> this")
  public Item setClickHandler(Consumer<InventoryClickEvent> clickHandler) {
    this.clickHandler = clickHandler;
    return this;
  }

  public void onClick(InventoryClickEvent event) {
    if (clickHandler == null) {
      return;
    }

    clickHandler.accept(event);
  }

  public ItemStack getItemstack() {
    return itemstack.get();
  }

  @Contract("_ -> this")
  public Item setPriority(Integer priority) {
    return setPriority(() -> priority);
  }

  @Contract("_ -> this")
  public Item setPriority(Supplier<Integer> priority) {
    this.priority = priority;
    return this;
  }

  public int getPriority() {
    if (priority == null) {
      return 1;
    }

    return priority.get();
  }

  public boolean isHidden() {
    return getPriority() < 0 || slot == null || slot.get() == null;
  }

  public void removeSlot() {
    this.slot = null;
  }

  @Contract("_,_ -> this")
  public Item setSlot(int row, int col) {
    return setSlot(Slot.of(row, col));
  }

  @Contract("_ -> this")
  public Item setSlot(Slot slot) {
    return setSlot(() -> slot);
  }

  @Contract("_ -> this")
  public Item setSlot(Supplier<Slot> slot) {
    this.slot = slot;
    return this;
  }

  public Slot getSlot() {
    return slot.get();
  }

  @Override
  public int compareTo(@NotNull Item item) {
    return Integer.compare(getPriority(), item.getPriority());
  }
}

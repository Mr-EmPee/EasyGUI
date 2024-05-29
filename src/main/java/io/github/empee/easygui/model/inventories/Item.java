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

  public static final Item DEFAULT = Item.of(new ItemStack(Material.AIR)).priority(0);

  private final Supplier<ItemStack> itemstack;
  private Supplier<Integer> priority;
  private Consumer<InventoryClickEvent> clickHandler;
  private Supplier<Slot> slot;

  public static Item of(ItemStack item) {
    return of(() -> item);
  }

  @Contract("_ -> this")
  public Item clickHandler(Consumer<InventoryClickEvent> clickHandler) {
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
  public Item priority(Integer priority) {
    return priority(() -> priority);
  }

  @Contract("_ -> this")
  public Item priority(Supplier<Integer> priority) {
    this.priority = priority;
    return this;
  }

  public int getPriority() {
    if (priority == null) {
      return 1;
    }

    return priority.get();
  }

  public boolean isVisible() {
    return getPriority() >= 0;
  }

  @Contract("_,_ -> this")
  public Item slot(int row, int col) {
    return slot(Slot.of(row, col));
  }

  @Contract("_ -> this")
  public Item slot(Slot slot) {
    return slot(() -> slot);
  }

  @Contract("_ -> this")
  public Item slot(Supplier<Slot> slot) {
    this.slot = slot;
    return this;
  }

  public Slot getSlot() {
    if (slot == null) {
      return null;
    }

    return slot.get();
  }

  @Override
  public int compareTo(@NotNull Item item) {
    return Integer.compare(getPriority(), item.getPriority());
  }
}

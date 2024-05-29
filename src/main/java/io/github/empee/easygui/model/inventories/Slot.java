package io.github.empee.easygui.model.inventories;

import io.github.empee.easygui.guis.inventories.InventoryGUI;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class Slot {

  private final int row;
  private final int col;

  public static List<Slot> col(InventoryGUI gui, int col) {
    return col(gui, 0, col);
  }

  public static List<Slot> row(InventoryGUI gui, int row) {
    return row(gui, 0, row);
  }

  public static List<Slot> col(InventoryGUI gui, int margin, int col) {
    return col(margin, gui.getHeight() - 1 - margin, col);
  }

  public static List<Slot> row(InventoryGUI gui, int margin, int row) {
    return row(margin, gui.getLength() - 1 - margin, row);
  }

  public static List<Slot> col(int start, int end, int col) {
    var result = new ArrayList<Slot>();

    for (int row = start; row <= end; row++) {
      result.add(Slot.of(row, col));
    }

    return result;
  }

  public static List<Slot> row(int start, int end, int row) {
    var result = new ArrayList<Slot>();

    for (int col = start; col < end; col++) {
      result.add(Slot.of(row, col));
    }

    return result;
  }

  public static List<Slot> cols(InventoryGUI gui, int... cols) {
    return cols(0, gui, cols);
  }

  public static List<Slot> rows(InventoryGUI gui, int... rows) {
    return rows(0, gui, rows);
  }

  public static List<Slot> cols(int margin, InventoryGUI gui, int... cols) {
    return cols(margin, gui.getHeight()-1-margin, cols);
  }

  public static List<Slot> rows(int margin, InventoryGUI gui, int... rows) {
    return rows(margin, gui.getLength()-1-margin, rows);
  }

  public static List<Slot> cols(int start, int end, int... cols) {
    var result = new ArrayList<Slot>();

    for (int col : cols) {
      result.addAll(col(start, end, col));
    }

    return result;
  }

  public static List<Slot> rows(int start, int end, int... rows) {
    var result = new ArrayList<Slot>();

    for (int row : rows) {
      result.addAll(row(start, end, row));
    }

    return result;
  }

  public static List<Slot> pane(Slot start, Slot end) {
    var result = new ArrayList<Slot>();

    for (int row = start.getRow(); row <= end.getRow(); row++) {
      for (int col = start.getCol(); col <= end.getCol(); col++) {
        result.add(Slot.of(row, col));
      }
    }

    return result;
  }

  public static List<Slot> pane(InventoryGUI gui, int margin) {
    return pane(Slot.of(margin, margin), Slot.of(gui.getHeight()-1-margin, gui.getLength()-1-margin));
  }

  public static List<Slot> pane(InventoryGUI gui) {
    return pane(gui, 0);
  }

  public static List<Slot> borders(Slot start, Slot end) {
    var result = new ArrayList<Slot>();

    result.addAll(rows(start.getCol(), end.getCol(), start.getRow(), end.getRow()));
    result.addAll(cols(start.getRow(), end.getRow(), start.getCol(), end.getCol()));

    return result;
  }

  public static List<Slot> borders(InventoryGUI gui, int margin) {
    return borders(Slot.of(margin, margin), Slot.of(gui.getHeight()-1-margin, gui.getLength()-1-margin));
  }

  public static List<Slot> borders(InventoryGUI gui) {
    return borders(gui, 0);
  }

  public static int toRaw(int row, int col, int totalCols) {
    return (row * totalCols) + col;
  }

  public int toRaw(int totalCols) {
    return toRaw(row, col, totalCols);
  }

  public interface Generator {
    static Generator of(List<Slot> slots) {
      return offset -> Collections.unmodifiableList(slots);
    }

    List<Slot> generate(int offset);
  }
}

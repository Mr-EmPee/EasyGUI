package io.github.empee.easygui;

import io.github.empee.easygui.guis.inventories.InventoryGUI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EasyGUI {

  public static void init(JavaPlugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(new InventoryGUI.Listeners(), plugin);
  }

}

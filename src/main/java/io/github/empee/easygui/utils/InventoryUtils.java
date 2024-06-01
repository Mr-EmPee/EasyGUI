package io.github.empee.easygui.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class InventoryUtils {

  private static final Map<Class<?>, Method> CACHE = new HashMap<>();

  @SneakyThrows
  private Method getMethod(Class<?> clazz, String name, Class<?>... params) {
    return clazz.getMethod(name, params);
  }

  @SneakyThrows
  public void setTitle(InventoryView view, String title) {
    if (ServerVersion.isLowerThan(1, 20)) {
      throw new IllegalArgumentException("Can't update GUI title server < 1.20");
    }

    var setTitle = CACHE.computeIfAbsent(view.getClass(), k -> getMethod(k,"setTitle"));
    setTitle.invoke(view, title);
  }

}

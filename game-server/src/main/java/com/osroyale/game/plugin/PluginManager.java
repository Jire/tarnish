package com.osroyale.game.plugin;

import com.osroyale.game.event.bus.PlayerDataBus;
import io.github.classgraph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

/**
 * This class handles how plugins are loaded/unloaded and accessed.
 *
 * @author nshusa
 */
public final class PluginManager {

    private static final Logger logger = LogManager.getLogger();
    private static final Set<String> plugins = new HashSet<>();
    private static final PlayerDataBus dataBus = PlayerDataBus.getInstance();

    private PluginManager() {
        throw new UnsupportedOperationException();
    }

    public static void load(final String pkg) {
        try (final ScanResult scanResult =
                     new ClassGraph()
                             .enableClassInfo()
                             .enableMethodInfo()
                             .acceptPackages(pkg)
                             .scan()) {
            for (final ClassInfo classInfo : scanResult.getSubclasses(PluginContext.class)) {
                try {
                    if (!classInfo.isAbstract()) {
                        final MethodInfoList constructorInfos = classInfo.getDeclaredConstructorInfo();
                        final MethodInfo constructorInfo = constructorInfos.get(0);
                        final Constructor<?> constructor = constructorInfo.loadClassAndGetConstructor();

                        final Object instance = constructor.newInstance();
                        final PluginContext listener = (PluginContext) instance;
                        listener.onInit();
                        dataBus.subscribe(listener);
                        plugins.add(classInfo.getName());
                    }
                } catch (Exception ex) {
                    logger.error(String.format("Error loading plugin=%s", classInfo.getSimpleName()), ex);
                }
            }
        }
        logger.info(String.format("Loaded: %d plugins.", PluginManager.getPluginCount()));
    }

    public static int getPluginCount() {
        return plugins.size();
    }

    public static PlayerDataBus getDataBus() {
        return dataBus;
    }

}

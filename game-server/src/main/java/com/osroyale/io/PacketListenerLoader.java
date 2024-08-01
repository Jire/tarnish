package com.osroyale.io;

import com.osroyale.net.packet.PacketListener;
import com.osroyale.net.packet.PacketListenerMeta;
import com.osroyale.net.packet.PacketRepository;
import io.github.classgraph.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * The class that loads all packet listeners.
 *
 * @author nshusa
 */
public final class PacketListenerLoader implements Runnable {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void run() {
        try (final ScanResult scanResult =
                     new ClassGraph()
                             .enableClassInfo()
                             .enableMethodInfo()
                             .enableAnnotationInfo()
                             .acceptPackages("com.osroyale.net.packet.in")
                             .scan()) {
            for (final ClassInfo classInfo : scanResult.getClassesWithAnnotation(PacketListenerMeta.class)) {
                try {
                    final MethodInfoList constructorInfos = classInfo.getDeclaredConstructorInfo();
                    final MethodInfo constructorInfo = constructorInfos.get(0);
                    final Constructor<?> constructor = constructorInfo.loadClassAndGetConstructor();

                    final Object instance = constructor.newInstance();
                    final PacketListener listener = (PacketListener) instance;

                    final AnnotationInfo annotationInfo = classInfo.getAnnotationInfo(PacketListenerMeta.class);
                    final Annotation annotation = annotationInfo.loadClassAndInstantiate();
                    final PacketListenerMeta meta = (PacketListenerMeta) annotation;

                    Arrays.stream(meta.value())
                            .forEach(it -> PacketRepository.registerListener(it, listener));
                } catch (final Exception ex) {
                    logger.error(String.format("Error loading packet listeners=%s", classInfo.getSimpleName()), ex);
                }
            }
        }
    }

}

package com.osroyale.net.packet;

import java.lang.annotation.*;

/**
 * An annotation that describes what client -> server packets a {@link PacketListener} can listen to.
 *
 * @author nshusa
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface PacketListenerMeta {

    /**
     * The client-server packet identifiers that this annotated listener can listen to.
     */
    int[] value() default {};
}
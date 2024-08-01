package org.jire.tarnishps

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import de.mkammerer.argon2.Argon2Factory.Argon2Types

/**
 * @author Jire
 */
object Argon2 {

    @JvmField
    val DEFAULT_TYPE: Argon2Types = Argon2Types.ARGON2id

    const val DEFAULT_ITERATIONS = 2
    const val DEFAULT_MEMORY = 65536
    @JvmField
    val DEFAULT_PARALLELISM = Runtime.getRuntime().availableProcessors()

    private val threadLocal2i = ThreadLocal.withInitial {
        Argon2Factory.create(Argon2Types.ARGON2i)
    }

    private val threadLocal2d = ThreadLocal.withInitial {
        Argon2Factory.create(Argon2Types.ARGON2d)
    }

    private val threadLocal2id = ThreadLocal.withInitial {
        Argon2Factory.create(Argon2Types.ARGON2id)
    }

    @JvmStatic
    fun get2i(): Argon2 = threadLocal2i.get()

    @JvmStatic
    fun get2d(): Argon2 = threadLocal2d.get()

    @JvmStatic
    fun get2id(): Argon2 = threadLocal2id.get()

    @JvmStatic
    fun forType(argon2Types: Argon2Types) = when (argon2Types) {
        Argon2Types.ARGON2d -> get2d()
        Argon2Types.ARGON2i -> get2i()
        Argon2Types.ARGON2id -> get2id()
        else -> throw UnsupportedOperationException()
    }

    @JvmStatic
    fun getDefault(): Argon2 = forType(DEFAULT_TYPE)

    @JvmStatic
    fun argon2Type(password: String): Argon2Types? = when {
        password.startsWith("\$argon2id\$") -> Argon2Types.ARGON2id
        password.startsWith("\$argon2i\$") -> Argon2Types.ARGON2i
        password.startsWith("\$argon2d\$") -> Argon2Types.ARGON2d
        else -> null
    }

    @JvmStatic
    fun forHash(hash: String) = forType(
        argon2Type(hash) ?: throw UnsupportedOperationException("Could not determine type for hash \"$hash\"")
    )

}
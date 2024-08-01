package org.jire.tarnishps.objectexamines

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.osroyale.Config
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.nio.file.Path
import kotlin.io.path.bufferedReader

/**
 * @author Jire
 */
object ObjectExamines {

    private val defaultFilePath: Path = Path.of(
            "data",
            "def",
            "object_examines.json"
    )

    @JvmField
    val map: Int2ObjectMap<String> = Int2ObjectOpenHashMap()

    @JvmStatic
    @JvmOverloads
    fun loadObjectExamines(
            filePath: Path = defaultFilePath,
            gson: Gson = GsonBuilder()
                    .setPrettyPrinting()
                    .create()
    ) {
        val examines: Array<ObjectExamine> =
                gson.fromJson(filePath.bufferedReader(), Array<ObjectExamine>::class.java)
        for (examine in examines) {
            map.put(examine.id, examine.examine.replace("%SERVER_NAME%", Config.SERVER_NAME))
        }
    }

}
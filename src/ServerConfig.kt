import com.google.gson.Gson
import com.github.salomonbrys.kotson.*
import java.io.File

val gson = Gson()

data class ServerConfig(
        val database: DatabaseConfig
)

inline fun <reified T : Any> loadJson(path: String): T {
    val content = File(path).readText(charset = Charsets.UTF_8)
    return gson.fromJson<T>(content)
}

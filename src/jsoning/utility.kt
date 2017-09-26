package jsoning

import com.google.gson.Gson
import com.github.salomonbrys.kotson.*
import java.io.File

val gson = Gson()

inline fun <reified T : Any> jsonify(data: String): T {
    return gson.fromJson<T>(data)
}

inline fun <reified T : Any> loadJson(path: String): T {
    val content = File(path).readText(charset = Charsets.UTF_8)
    return jsonify<T>(content)
}

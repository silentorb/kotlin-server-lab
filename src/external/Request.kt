package external

import jsoning.jsonify
import java.io.InputStreamReader
import java.io.BufferedReader
import java.net.URL
import java.io.DataOutputStream
import java.net.HttpURLConnection


fun postHttpString(method: String, url: String, urlParameters: String = ""):String? {
    var connection: HttpURLConnection? = null
    val response = StringBuffer()

    try {
        //Create connection
        val urlObject = URL(url)
        connection = urlObject.openConnection() as HttpURLConnection
        with(connection) {
            setRequestMethod(method)
//            setRequestProperty("Content-Type", "application/json")
//            setRequestProperty("Content-Length", Integer.toString(urlParameters.toByteArray().size))
            setUseCaches(false)
//            setDoOutput(true)
        }

        connection.useCaches = false
//        val webRequest = DataOutputStream(connection.getOutputStream())
//        webRequest.writeBytes(urlParameters)
//        webRequest.close()

        val status = connection.getResponseCode()

        val stream = connection.getInputStream()
        val buffer = BufferedReader(InputStreamReader(stream))
        while(true) {
            val line = buffer.readLine() ?: break
            response.append(line)
        }

        buffer.close()
        return response.toString()
    } catch (e: Exception) {
        return null
    } finally {
        if (connection != null) {
            connection.disconnect()
        }
    }
}

fun getHttpString(method: String, url: String, urlParameters: String = ""):String {
    var connection: HttpURLConnection? = null
    val response = StringBuffer()

    try {
        val urlObject = URL(url)
        connection = urlObject.openConnection() as HttpURLConnection
        with(connection) {
            setRequestMethod(method)
//            setRequestProperty("Content-Type", "application/json")
//            setRequestProperty("Content-Length", Integer.toString(urlParameters.toByteArray().size))
            setUseCaches(false)
        }

        val status = connection.getResponseCode()

        val stream = connection.getInputStream()
        val buffer = BufferedReader(InputStreamReader(stream))
        while(true) {
            val line = buffer.readLine() ?: break
            response.append(line)
        }

        buffer.close()
        return response.toString()
    } catch (e: Exception) {
        throw e
    } finally {
        if (connection != null) {
            connection.disconnect()
        }
    }
}

inline fun <reified T : Any>getHttpObject(method: String, url: String, urlParameters: String = ""):T {
    val body = getHttpString(method, url, urlParameters)
    return jsonify<T>(body)
}
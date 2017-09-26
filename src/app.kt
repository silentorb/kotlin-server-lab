import external.getHttpObject
import external.getHttpString
import ground.Ground

class User {
    var id: String = ""
    var username: String = ""
}

class BitfinixBitcoinResponse {
    var mid: String = ""
    var bid: String = ""
    var ask: String = ""
    var last_price: String = ""
    var low: String = ""
    var high: String = ""
    var volume: String = ""
    var timestamp: String = ""
}

fun main(args: Array<String>) {
    println("Hello World!")
    val serverConfig = loadJson<ServerConfig>("config/config.json")
    val data = getHttpObject<BitfinixBitcoinResponse>("GET", "https://api.bitfinex.com/v1/pubticker/btcusd")
//    val body = requestString("GET", "http://silentorb.com")

    val ground = Ground(serverConfig.database)
    ground.query<User>("SELECT * FROM users")

}
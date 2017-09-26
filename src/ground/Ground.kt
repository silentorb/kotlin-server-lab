package ground

import org.apache.commons.dbcp2.BasicDataSource
import java.sql.Connection

data class DatabaseConfig(
        val host: String,
        val database: String,
        val username: String,
        val password: String,
        val dialect: String,
        val devMode: Boolean,
        val logging: Boolean
)

class Ground(config: DatabaseConfig) {
    val config = config
    var pool: BasicDataSource = BasicDataSource()

    fun init() {
        pool.setDriverClassName("org.postgresql.Driver")
        pool.username = config.username
        pool.password = config.password
        pool.url = "jdbc:postgresql://" + config.host + ":5432/" + config.database
    }

    fun getConnection(): Connection {
        return pool.getConnection()
    }
}
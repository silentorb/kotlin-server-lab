package ground

import org.apache.commons.dbcp2.BasicDataSource
import java.sql.Connection
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

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

    init {
        pool.setDriverClassName("org.postgresql.Driver")
        pool.username = config.username
        pool.password = config.password
        pool.url = "jdbc:postgresql://" + config.host + ":5432/" + config.database
    }

    fun getConnection(): Connection {
        return pool.getConnection()
    }

    inline fun <reified T: Any> createSeed() : T {
        T::class.constructors.forEach { con ->
            if (con.parameters.size == 0) {
                return con.call()
            }
        }

        throw Exception("Class " + T::class.simpleName + " must have a constructor with zero parameters.")
    }

    inline fun <reified T : Any> query(sql: String): Stream<T> {
        val connection = getConnection()
        val statement = connection.createStatement()
        val result = statement.executeQuery(sql)
        val collection: MutableList<T> = mutableListOf()
        while (result.next()) {
            val seed = createSeed<T>()

            for (property in T::class.memberProperties) {
                println(property.typeParameters.count())
                if (property is KMutableProperty<*>) {
                    val currentValue = property.get(seed)
                    if (currentValue is String)
                        property.setter.call(seed, result.getString(property.name))
                    else
                        throw Exception("Unsupported property type.")
                }
            }
            collection.add(seed)
        }

        return StreamSupport.stream(collection.spliterator(), false)
    }
}
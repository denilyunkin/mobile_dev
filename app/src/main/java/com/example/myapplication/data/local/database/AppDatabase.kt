package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

//adb shell pm clear com.example.myapplication
@Database(
    entities = [User::class, Furni::class, CartItem::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun furniDao(): FurniDao
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao

    companion object {
        const val DATABASE_NAME = "furni_shop_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback(context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    class DatabaseCallback(
        private val context: Context
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)


            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `users` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `username` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `password` TEXT NOT NULL
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `furnis` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `name` TEXT NOT NULL,
                    `manufacturer` TEXT NOT NULL,
                    `price` REAL NOT NULL,
                    `imageUrl` TEXT NOT NULL,
                    `weight` INTEGER NOT NULL,
                    `material` TEXT NOT NULL,
                    `width` INTEGER NOT NULL,
                    `high` INTEGER NOT NULL,
                    `depth` INTEGER NOT NULL,
                    `description` TEXT NOT NULL
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `cart_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `userId` INTEGER NOT NULL,
                    `furniId` INTEGER NOT NULL,
                    `quantity` INTEGER NOT NULL,
                    FOREIGN KEY(`userId`) REFERENCES `users`(`id`) ON DELETE CASCADE,
                    FOREIGN KEY(`furniId`) REFERENCES `furnis`(`id`) ON DELETE CASCADE
                )
                """.trimIndent()
            )


            db.execSQL("INSERT INTO users (username, email, password) VALUES ('admin', 'admin@example.com', 'admin123')")
            db.execSQL("INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'password1')")
            db.execSQL("INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'password2')")


            val furnises = listOf(
                Furni(
                    name = "Мягкое кресло \"Агата\"",
                    manufacturer = "SCANDICA",
                    price = 6599.00,
                    imageUrl = "agatha",
                    weight = 16,
                    material = "Хлопок",
                    width = 295,
                    high = 610,
                    depth = 240,
                    description = "Мягкое кресло"
                ),
                Furni(
                    name = "Набор журнальных столов",
                    manufacturer = "Vega",
                    price = 10999.00,
                    imageUrl = "vega",
                    weight = 12,
                    material = "Стекло",
                    width = 395,
                    high = 550,
                    depth =  240,
                    description = "Набор журнальных столов из новой коллекции"
                ),
                Furni(
                    name = "Кресло \"Wood Base\"",
                    manufacturer = "Swoon Lounge",
                    price = 20699.00,
                    imageUrl = "swoon_lounge",
                    weight = 15,
                    material = "Хлопок",
                    width = 520,
                    high = 365,
                    depth = 288,
                    description = "Кресло \"Swoon Lounge - Wood Base\""
                ),
                Furni(
                    name = "Журнальный стол \"Трансформер\"",
                    manufacturer = "SCANDICA",
                    price = 9990.00,
                    imageUrl = "transformer",
                    weight = 32,
                    material = "Дерево",
                    width = 1626,
                    high = 491,
                    depth = 392,
                    description = "Журнальный стол \"Трансформер\" из новой коллекции"
                ),
                Furni(
                    name = "Диван \"Искра\"",
                    manufacturer = "РУСМЕБЕЛЬ",
                    price = 25590.00,
                    imageUrl = "spark",
                    weight = 29,
                    material = "Хлопок",
                    width = 200,
                    high = 525,
                    depth = 120,
                    description = "Новый диван от РУСМЕБЕЛЬ"
                ),
                Furni(
                    name = "Мягкий диван \"Тафтинг\"",
                    manufacturer = "SCANDICA",
                    price = 29599.00,
                    imageUrl = "tafting",
                    weight = 60,
                    material = "Хлопок",
                    width = 310,
                    high = 565,
                    depth = 352,
                    description = "Мягкий диван"
                ),
                Furni(
                    name = "Фасад \"Меренго\" для комода",
                    manufacturer = "MEBELCO",
                    price = 2799.00,
                    imageUrl = "marengo",
                    weight = 26,
                    material = "Дерево",
                    width = 720,
                    high = 299,
                    depth = 248,
                    description = "Фасад - старая коллекция"
                ),
                Furni(
                    name = "Кресло-кровать DREAMART \"Палермо\"",
                    manufacturer = "DREAMART",
                    price = 37999.00,
                    imageUrl = "palermo_yellow",
                    weight = 36,
                    material = "Хлопок",
                    width = 200,
                    high = 200,
                    depth = 196,
                    description = "Кресло-кровать DREAMART \"Палермо\" - новая коллекция"
                )
            )

            val furniStmt = db.compileStatement(
                "INSERT INTO furnis (name, manufacturer, price, imageUrl, weight, material, width, high, depth, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            )

            furnises.forEach { furni ->
                furniStmt.bindString(1, furni.name)
                furniStmt.bindString(2, furni.manufacturer)
                furniStmt.bindDouble(3, furni.price)
                furniStmt.bindString(4, furni.imageUrl)
                furniStmt.bindLong(5, furni.weight.toLong())
                furniStmt.bindString(6, furni.material)
                furniStmt.bindLong(7, furni.width.toLong())
                furniStmt.bindLong(8, furni.high.toLong())
                furniStmt.bindLong(9, furni.depth.toLong())
                furniStmt.bindString(10, furni.description)
                furniStmt.executeInsert()
                furniStmt.clearBindings()
            }

            val cartStmt = db.compileStatement(
                "INSERT INTO cart_items (userId, furniId, quantity) VALUES (?, ?, ?)"
            )

            cartStmt.bindLong(1, 1)
            cartStmt.bindLong(2, 1)
            cartStmt.bindLong(3, 1)
            cartStmt.executeInsert()
            cartStmt.clearBindings()

            cartStmt.bindLong(1, 1)
            cartStmt.bindLong(2, 2)
            cartStmt.bindLong(3, 2)
            cartStmt.executeInsert()
            cartStmt.clearBindings()

            cartStmt.bindLong(1, 2)
            cartStmt.bindLong(2, 3)
            cartStmt.bindLong(3, 1)
            cartStmt.executeInsert()
            cartStmt.clearBindings()
        }
    }

}
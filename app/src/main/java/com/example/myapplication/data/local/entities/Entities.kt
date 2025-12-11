package com.example.myapplication



import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String
) {
    companion object {
        fun empty() = User(
            username = "",
            email = "",
            password = ""
        )
    }
}

@Entity(tableName = "furnis")
data class Furni(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val manufacturer: String,
    val price: Double,
    val imageUrl: String,
    val weight: Int, // memorySize - weight
    val material: String, // memoryType - material
    val width: Int, // coreClock - width
    val high: Int, // boostClock - high
    val depth: Int, // cudaCores - depth
    val description: String
) {
    companion object {
        fun empty() = Furni(
            name = "",
            manufacturer = "",
            price = 0.0,
            imageUrl = "",
            weight = 0,
            material = "",
            width = 0,
            high = 0,
            depth = 0,
            description = ""
        )
    }
}

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Furni::class,
            parentColumns = ["id"],
            childColumns = ["furniId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val furniId: Int,
    var quantity: Int = 1
)
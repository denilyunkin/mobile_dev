package com.example.myapplication
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShopRepository @Inject constructor(
    private val furniDao: FurniDao,
    private val userDao: UserDao,
    private val cartDao: CartDao
) {
    // Furni operations
    fun getAllFurnis(): Flow<List<Furni>> = furniDao.getAllFurnis()
    fun getFurniById(furniId: Int): Flow<Furni> = furniDao.getFurniById(furniId)
    suspend fun insertFurni(furni: Furni) = furniDao.insertFurni(furni)

    // User operations
    suspend fun getUser(email: String, password: String) = userDao.getUser(email, password)
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    fun getUserById(userId: Int): Flow<User> = userDao.getUserById(userId)

    // Cart operations
    fun getCartItems(userId: Int): Flow<List<CartItem>> = cartDao.getCartItems(userId)
    suspend fun getCartItem(userId: Int, furniId: Int) = cartDao.getCartItem(userId, furniId)
    suspend fun addToCart(userId: Int, furniId: Int) {
        val existingItem = cartDao.getCartItem(userId, furniId)
        if (existingItem != null) {
            existingItem.quantity += 1
            cartDao.updateCartItem(existingItem)
        } else {
            cartDao.insertCartItem(CartItem(userId = userId, furniId = furniId))
        }
    }
    suspend fun removeFromCart(userId: Int, furniId: Int) {
        val existingItem = cartDao.getCartItem(userId, furniId)
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity -= 1
                cartDao.updateCartItem(existingItem)
            } else {
                cartDao.deleteCartItem(existingItem)
            }
        }
    }
    suspend fun deleteCartItem(cartItem: CartItem) = cartDao.deleteCartItem(cartItem)
    suspend fun clearCart(userId: Int) = cartDao.clearCart(userId)
    fun getCartItemCount(userId: Int): Flow<Int> = cartDao.getCartItemCount(userId)
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
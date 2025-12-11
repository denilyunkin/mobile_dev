
package com.example.myapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun MainScreen(
    navController: NavController,
    viewModel: ShopViewModel
) {
    val furnis by viewModel.allFurnis.collectAsState(initial = emptyList())
    val cartItems by viewModel.cartItems.collectAsState(initial = emptyList())
    val cartFurniIds by remember(cartItems) {
        derivedStateOf {
            cartItems.map { it.furniId }.toSet()
        }
    }
    val currentUserId by viewModel.currentUserId.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, currentUserId, currentRoute)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(furnis, key = { it.id }) { furni ->
                val isInCart = furni.id in cartFurniIds
                val cartItem = cartItems.find { it.furniId == furni.id }
                FurniCard(
                    furni = furni,
                    isInCart = isInCart,
                    onAddToCart = { viewModel.addToCart(furni.id) },
                    onRemoveFromCart = { cartItem?.let { viewModel.deleteCartItem(it) }},
                    onItemClick = { navController.navigate(Screen.Item.createRoute(furni.id)) }
                )
            }
        }
    }
}
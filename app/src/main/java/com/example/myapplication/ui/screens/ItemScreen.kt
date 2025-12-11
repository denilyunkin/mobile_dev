package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun ItemScreen(
    navController: NavController,
    viewModel: ShopViewModel,
    furniId: Int
) {
    val furni by viewModel.furniById(furniId).collectAsState(initial = Furni.empty())
    val cartItems by viewModel.cartItems.collectAsState(initial = emptyList())
    val currentUserId by viewModel.currentUserId.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isInCart by viewModel.isInCart(furni.id).collectAsState(false)
    val context = LocalContext.current
    val imageName = if (furni.imageUrl.isNullOrBlank()) "no_image" else furni.imageUrl

    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        bottomBar = {
            BottomNavigationBar(navController, currentUserId, currentRoute)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = furni.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = furni.name,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "%.2f ₽".format(furni.price),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isInCart) {
                        val cartItem = cartItems.find { it.furniId == furni.id }
                        cartItem?.let { viewModel.deleteCartItem(it) }
                    } else {

                        viewModel.addToCart(furniId)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInCart) Color.Red else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isInCart) "Удалить из корзины" else "Добавить в корзину")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Характеристики",
                style = MaterialTheme.typography.headlineSmall
            )

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            PropertyRow("Производитель", furni.manufacturer)
            PropertyRow("Вес", "${furni.weight} Кг")
            PropertyRow("Основной материал", furni.material)
            PropertyRow("Ширина", "${furni.width} См")
            PropertyRow("Высота", "${furni.high} См")
            PropertyRow("В наличии", furni.depth.toString()) // "${furni.high} См"

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Описание",
                style = MaterialTheme.typography.headlineSmall
            )

            Divider()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = furni.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun PropertyRow(name: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
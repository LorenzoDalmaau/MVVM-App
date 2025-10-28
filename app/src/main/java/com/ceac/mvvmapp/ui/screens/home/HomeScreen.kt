package com.ceac.mvvmapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ceac.mvvmapp.domain.model.Product
import com.ceac.mvvmapp.ui.theme.MVVMAppTheme

@Composable
fun HomeScreen(
    state: HomeUiState,
    onRetry: () -> Unit
) {
    val s = MVVMAppTheme.spacing

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(s.md)) {
                Text(state.error, color = MaterialTheme.colorScheme.error)
                OutlinedButton(onClick = onRetry) { Text("Reintentar") }
            }
        }
        else -> LazyColumn(
            contentPadding = PaddingValues(vertical = s.lg, horizontal = s.lg),
            verticalArrangement = Arrangement.spacedBy(s.lg)
        ) {
            items(state.products, key = { it.id }) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product) {
    val s = MVVMAppTheme.spacing

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(s.lg)) {
                Text(product.name, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(s.xs))
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(s.sm))
                Text(
                    text = "€${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



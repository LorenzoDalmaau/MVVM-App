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

/**
 * Paso 13: UI declarativa de la pantalla Home (stateless).
 *
 * Explicación:
 * Esta función composable representa la UI pura de Home. No conoce ViewModel ni lógica
 * de negocio; recibe un estado inmutable [HomeUiState] y callbacks para notificar acciones.
 *
 * Renderiza tres escenarios mutuamente excluyentes:
 * 1) Cargando: indicador centrado.
 * 2) Error: mensaje y botón de "Reintentar".
 * 3) Lista: un feed de productos en `LazyColumn`.
 *
 * Diseño:
 * - Aplica el `contentPadding` del contenedor superior (Scaffold/NavHost).
 * - Usa spacing centralizado desde el tema (`MVVMAppTheme.spacing`) para consistencia.
 * - Mantiene la UI predecible y testeable al ser completamente "stateless".
 *
 * @param state Estado actual de la pantalla (loading/error/items/paginación).
 * @param onRetry Acción a ejecutar cuando el usuario decide reintentar la carga tras un error.
 * @param contentPadding Padding proveniente del contenedor padre (Scaffold).
 *
 * Paso siguiente:
 * - Añadir soporte de paginación (detectar fin de lista y solicitar `vm.load(page+1)`).
 * - Exponer un callback `onItemClick(Product)` o `onItemClick(id: String)` para navegares
 *   desde la lista a un detalle de producto.
 */
@Composable
fun HomeScreen(
    state: HomeUiState,
    onRetry: () -> Unit,
    contentPadding: PaddingValues
) {
    // Spacing centralizado definido en tu tema (márgenes, separaciones, etc.)
    val s = MVVMAppTheme.spacing

    when {
        // 1) Estado de carga: spinner centrado
        state.isLoading -> Box(
            Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        // 2) Estado de error: mensaje + acción de reintento
        state.error != null -> Box(
            Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(s.md)
            ) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error
                )
                OutlinedButton(onClick = onRetry) {
                    Text("Reintentar")
                }
            }
        }

        // 3) Estado de éxito: lista de productos
        else -> {
            // Aplica el padding del Scaffold/NavHost en el contenedor raíz
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = s.lg, horizontal = s.lg),
                    verticalArrangement = Arrangement.spacedBy(s.lg),
                    modifier = Modifier.fillMaxSize()
                ) {
                    /**
                     * items(list, key) → renderiza cada producto de forma eficiente.
                     * - key = { it.id } mejora la estabilidad de la lista en recomposiciones.
                     */
                    items(state.items, key = { it.id }) { product ->
                        ProductCard(product = product)
                    }
                }
            }
        }
    }
}

/**
 * Subcomponente visual (stateless) que representa una tarjeta de producto.
 *
 * Explicación:
 * - Muestra imagen, nombre, descripción y precio del producto.
 * - Usa `AsyncImage` (Coil) con `ImageRequest` para carga eficiente y `crossfade`.
 * - `ContentScale.Crop` asegura una presentación consistente de la imagen.
 *
 * @param product Entidad de dominio a renderizar.
 *
 * Paso siguiente:
 * - Añadir parámetro `onClick: (Product) -> Unit` (o `onClick: (String) -> Unit`)
 *   si deseas soportar navegación a detalle desde la card.
 */
@Composable
private fun ProductCard(product: Product) {
    val s = MVVMAppTheme.spacing

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Imagen del producto (Coil + AsyncImage)
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

            // Texto del producto
            Column(Modifier.padding(s.lg)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(s.xs))
                Text(
                    text = product.description,
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

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
 * ----------------------------------------------------------------------------
 * HomeScreen.kt
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción general:
 * Pantalla principal de la aplicación, responsable de **mostrar la lista de productos**.
 *
 * Esta función Composable representa la **UI pura**, sin lógica de negocio:
 * simplemente reacciona al estado (`HomeUiState`) y muestra el contenido adecuado
 * según los tres posibles escenarios:
 *
 * 1️⃣ **Cargando** → Spinner central (`CircularProgressIndicator`).
 * 2️⃣ **Error** → Mensaje + botón de “Reintentar”.
 * 3️⃣ **Éxito** → Lista de productos (`LazyColumn`).
 *
 * ----------------------------------------------------------------------------
 * 🔹 Principios aplicados:
 * ----------------------------------------------------------------------------
 * ✅ **Declaratividad:** la UI refleja exactamente el estado actual.
 * ✅ **Inmutabilidad:** no modifica nada, solo recibe datos y callbacks.
 * ✅ **Reactividad:** Compose se recompone automáticamente si el `state` cambia.
 * ✅ **Desacoplamiento:** no conoce ViewModels, ni navegación, ni lógica de datos.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Parámetros:
 * ----------------------------------------------------------------------------
 * @param state Estado actual de la pantalla (`HomeUiState`).
 * @param onRetry Callback ejecutado cuando el usuario pulsa "Reintentar" tras un error.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Diseño y estructura:
 * ----------------------------------------------------------------------------
 * - Usa `when` para renderizar un estado a la vez (loading, error o lista).
 * - Usa `MVVMAppTheme.spacing` para mantener consistencia visual (márgenes y padding).
 * - Renderiza los productos en una `LazyColumn` para rendimiento óptimo.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Ejemplo de uso:
 * ----------------------------------------------------------------------------
 * ```
 * HomeScreen(
 *     state = uiState,
 *     onRetry = { viewModel.load() }
 * )
 * ```
 * ----------------------------------------------------------------------------
 */
@Composable
fun HomeScreen(
    state: HomeUiState,
    onRetry: () -> Unit
) {
    val s = MVVMAppTheme.spacing

    when {
        // 🌀 Estado de carga
        state.isLoading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        // ⚠️ Estado de error
        state.error != null -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(s.md)
            ) {
                Text(
                    state.error,
                    color = MaterialTheme.colorScheme.error
                )
                OutlinedButton(onClick = onRetry) { Text("Reintentar") }
            }
        }

        // ✅ Estado de éxito: lista de productos
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

/**
 * ----------------------------------------------------------------------------
 * ProductCard.kt (subcomponente privado dentro de HomeScreen)
 * ----------------------------------------------------------------------------
 *
 * 🔹 Descripción:
 * Componente visual que representa un producto individual dentro de la lista.
 * Muestra su imagen, nombre, descripción y precio, siguiendo el estilo Material3.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Por qué está separado:
 * - Mejora la legibilidad del código de la pantalla principal.
 * - Facilita la reutilización y los tests visuales (previews).
 * - Se puede convertir fácilmente en un composable público si se reutiliza en otros módulos.
 *
 * ----------------------------------------------------------------------------
 * 🔹 Detalles técnicos:
 * - Usa `Card` con esquinas grandes (`MaterialTheme.shapes.large`).
 * - `AsyncImage` de la librería **Coil** para carga eficiente de imágenes.
 * - `ContentScale.Crop` recorta la imagen para mantener proporciones.
 * - Usa `TextOverflow.Ellipsis` para evitar textos desbordados.
 * ----------------------------------------------------------------------------
 */
@Composable
private fun ProductCard(product: Product) {
    val s = MVVMAppTheme.spacing

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Imagen del producto
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
                    product.name,
                    style = MaterialTheme.typography.titleLarge
                )
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

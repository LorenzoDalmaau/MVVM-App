package com.ceac.mvvmapp.ui.screens.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Paso 16: UI declarativa del login (stateless).
 *
 * Explicación:
 * Esta pantalla no conoce ViewModel ni lógica de negocio. Recibe un estado inmutable
 * y callbacks para notificar acciones del usuario. Con este enfoque:
 * - La UI es predecible y fácilmente testeable.
 * - El estado es la única fuente de verdad (se representa tal cual).
 * - Las acciones se comunican hacia arriba mediante funciones.
 *
 * Representación de estados:
 * - `state.isLoading` activa el indicador de progreso en el botón.
 * - `state.error` muestra un mensaje de error bajo los campos.
 *
 * Accesibilidad y UX:
 * - `KeyboardOptions` ajusta teclado y acción IME.
 * - Botón deshabilitado durante la operación para evitar pulsaciones repetidas.
 *
 * @param state Estado actual del formulario (email, password, loading, error).
 * @param onEmailChange Callback invocado al modificar el email.
 * @param onPasswordChange Callback invocado al modificar la contraseña.
 * @param onLoginClick Callback invocado al pulsar el botón de “Entrar”.
 * @param onRegisterClick Callback para navegar a la pantalla de registro.
 * @param onRecoverClick Callback para navegar a recuperación de contraseña.
 * @param contentPadding Padding externo heredado del Scaffold superior.
 */
@Composable
fun LoginScreen(
    state: LoginViewModel.UiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
    contentPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        // Campo de correo
        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de contraseña
        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Mensaje de error si existe
        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // CTA principal
        Button(
            onClick = onLoginClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Entrar")
            }
        }

        // Acciones secundarias
        TextButton(onClick = onRegisterClick) { Text("Crear cuenta") }
        TextButton(onClick = onRecoverClick) { Text("¿Olvidaste tu contraseña?") }
    }
}

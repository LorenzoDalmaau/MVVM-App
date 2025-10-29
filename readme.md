# 🧱 Arquitectura MVVM en Android con Jetpack Compose, Hilt y Clean Architecture

Este proyecto demuestra **cómo estructurar una app moderna en Android (2025)** siguiendo la arquitectura **MVVM (Model-View-ViewModel)** junto con buenas prácticas de **Clean Architecture** y **Dependency Injection con Hilt**.

El objetivo no es solo que funcione, sino que **permanezca**: que el código sea **limpio, mantenible y escalable**.  
Ideal para aprender cómo se organiza una app profesional desde cero.

---

## 🚀 ¿Qué es MVVM?

**MVVM** significa **Model - View - ViewModel**, y es un patrón que separa la lógica de negocio de la interfaz de usuario.

📱 En palabras simples:
> La **UI (View)** muestra datos y escucha clics del usuario.  
> La **ViewModel** se encarga de la lógica y maneja el estado.  
> Los **Models (Domain + Data)** contienen los datos reales.

Así evitamos mezclar “código de botones” con “lógica de negocio”.

---

## 🧩 Objetivo del patrón

✅ Evitar duplicación de código  
✅ Separar responsabilidades  
✅ Mejorar testabilidad  
✅ Facilitar mantenimiento  
✅ Reutilizar la lógica del dominio

---

## 📦 Estructura de carpetas

```
com.ceac.mvvmapp/
├── data/                # Implementaciones concretas de datos
│   ├── repository/      # Repositorios falsos o reales (API, DB...)
│   └── model/           # DTOs y mapeadores
│
├── di/                  # Configuración de Hilt (inyección de dependencias)
│   └── AppModule.kt
│
├── domain/              # Lógica de negocio pura
│   ├── model/           # Modelos del dominio
│   ├── repository/      # Interfaces de los repositorios
│   └── usecase/         # Casos de uso (acciones)
│
├── navigation/          # Sistema de rutas y navegación
│   ├── navGraph/        # Subgrafos de pantallas
│   ├── Route.kt         # Rutas tipadas
│   ├── UiEvent.kt       # Eventos desde el ViewModel
│   └── HandleNavigationEvents.kt
│
├── ui/                  # Capa de presentación
│   ├── screens/         # Pantallas por módulos
│   │   ├── auth/        # Login, Register, Recover Password
│   │   └── home/        # Pantalla principal
│   └── theme/           # Colores, tipografía, espaciado, etc.
│
└── MainActivity.kt      # Punto de entrada principal (Compose + NavHost)
```

---

## 🧠 Diagrama conceptual (MVVM en esta app)

```
🟩 UI (Compose)
   ↓ ↑
🟦 ViewModel
   ↓ ↑
🟧 Domain (UseCases, Repos, Modelos)
   ↓ ↑
🟥 Data (APIs, DB, FakeRepos)
```

Cada capa **solo depende de la capa inferior**.

---

## 🟩 UI Layer — Presentación

La **UI** es lo que el usuario ve y toca.  
Usa **Jetpack Compose** y se divide en:

| Tipo | Ejemplo | Qué hace |
|------|----------|----------|
| `Screen` | `RegisterScreen.kt` | Dibuja la interfaz visual pura. No tiene lógica. |
| `Entry` | `RegisterEntry.kt` | Conecta el `ViewModel` con la `Screen` y maneja los eventos. |
| `ViewModel` | `RegisterViewModel.kt` | Contiene la lógica y el estado de la pantalla. |
| `UiState` | `RegisterUiState.kt` | Estructura inmutable del estado actual. |

---

### 🧱 Ejemplo visual: flujo del registro

1. El usuario escribe su email → `onEmailChange()`  
2. `RegisterViewModel` valida y actualiza el estado (`UiState`)  
3. El usuario pulsa “Crear cuenta”  
4. El ViewModel llama al caso de uso `RegisterUseCase`  
5. El caso de uso usa el `AuthRepository`  
6. El repositorio (Fake) devuelve éxito o error  
7. El ViewModel envía un `UiEvent.ShowSnackbar`  
8. La UI muestra el mensaje o navega

---

### 🖼️ `RegisterScreen.kt` (UI pura)

```kotlin
@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column {
        OutlinedTextField(value = state.email, onValueChange = onEmailChange, label = { Text("Email") })
        OutlinedTextField(value = state.password, onValueChange = onPasswordChange, label = { Text("Contraseña") })
        Button(onClick = onRegisterClick, enabled = state.isValid) { Text("Crear cuenta") }
        TextButton(onClick = onBackClick) { Text("Volver") }
    }
}
```

👉 Solo dibuja la interfaz. No valida ni navega.

---

### ⚙️ `RegisterViewModel.kt` (lógica y estado)

```kotlin
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onRegisterClick() = viewModelScope.launch {
        val s = _state.value
        val result = registerUseCase(s.email, s.password)
        result.onSuccess {
            _events.send(UiEvent.ShowSnackbar("Cuenta creada. Inicia sesión"))
            _events.send(UiEvent.NavigateBack)
        }.onFailure {
            _state.value = s.copy(submitError = it.message ?: "Error desconocido")
        }
    }
}
```

👉 Valida, llama al caso de uso, y comunica eventos a la UI.  
**No sabe nada de Compose ni de NavController.**

---

## 🟧 DOMAIN Layer — Lógica de negocio pura

Aquí definimos *qué hace la app*, sin importar *cómo*.

### Ejemplo de **modelo del dominio**
```kotlin
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double
)
```

### Ejemplo de **repositorio (interfaz)**
```kotlin
interface ProductRepository {
    suspend fun getProducts(): List<Product>
}
```

### Ejemplo de **caso de uso**
```kotlin
class GetProductsUseCase @Inject constructor(
    private val repo: ProductRepository
) {
    suspend operator fun invoke(): List<Product> = repo.getProducts()
}
```

👉 El dominio no sabe nada de Compose, ni de Retrofit, ni de bases de datos.  
Solo define las reglas del juego.

---

## 🟥 DATA Layer — Implementaciones reales o falsas

Aquí es donde “sucede” la obtención de datos: API, base de datos, mocks…

```kotlin
@Singleton
class FakeProductRepository @Inject constructor() : ProductRepository {
    override suspend fun getProducts(): List<Product> {
        delay(600)
        return (1..10).map { i ->
            Product(
                id = i.toString(),
                name = "Producto $i",
                description = "Descripción breve",
                price = (10..99).random() + 0.99,
                imageUrl = "https://picsum.photos/seed/$i/600/400"
            )
        }
    }
}
```

👉 En el futuro podrías reemplazar `FakeProductRepository` por una versión real usando Retrofit o Room, **sin tocar el ViewModel ni el Domain**.

---

## ⚙️ DI Layer — Inyección de dependencias (Hilt)

Hilt se encarga de conectar las piezas.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds @Singleton
    abstract fun bindAuthRepository(impl: FakeAuthRepository): AuthRepository

    @Binds @Singleton
    abstract fun bindProductRepository(impl: FakeProductRepository): ProductRepository
}
```

Y luego simplemente usas:

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel()
```

✅ Sin `new`, sin singletons manuales, sin pasar dependencias a mano.

---

## 🧭 Navegación desacoplada (Navigation + UiEvents)

Cada pantalla emite **eventos** desde su ViewModel,  
y la UI se encarga de interpretarlos:

```kotlin
sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data object NavigateBack : UiEvent()
    data class ShowSnackbar(val message: String) : UiEvent()
}
```

Así la **ViewModel no tiene acceso directo** al `NavController` ni al `Snackbar`.

---

## 🏠 `MainActivity.kt`

Punto de entrada principal:

```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val nav = rememberNavController()
            val snack = remember { SnackbarHostState() }
            MVVMAppTheme {
                Scaffold(snackbarHost = { SnackbarHost(snack) }) { padding ->
                    AppNav(navController = nav, snackbarHostState = snack, contentPadding = padding)
                }
            }
        }
    }
}
```

---

## 🧾 Flujo de desarrollo recomendado

1️⃣ Crear la ruta en `Route.kt`  
2️⃣ Crear el **UiState**  
3️⃣ Crear el **ViewModel**  
4️⃣ Crear la **Screen (UI pura)**  
5️⃣ Crear el **Entry**  
6️⃣ Añadir la pantalla al **Graph**  
7️⃣ Probar estados (loading, error, success)  
8️⃣ Añadir a Hilt si es un nuevo repositorio o caso de uso  

---

## 💬 Conclusión

> El objetivo de este proyecto no es solo enseñar Compose,  
> sino enseñar **cómo se construye una app real que escala.**

Con esta estructura:
- Puedes cambiar una API sin tocar la UI  
- Puedes probar los ViewModels fácilmente  
- Y el código es **autoexplicativo y profesional**

---

### 🧑‍💻 Autor
**Lorenzo**
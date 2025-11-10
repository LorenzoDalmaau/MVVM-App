package com.ceac.mvvmapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ceac.mvvmapp.data.local.datastore.TokenStore
import com.ceac.mvvmapp.data.remote.api.ProductApi
import com.ceac.mvvmapp.data.remote.api.auth.AuthApi
import com.ceac.mvvmapp.data.remote.interceptor.AuthInterceptor
import com.ceac.mvvmapp.data.remote.interceptor.TokenAuthenticator
import com.ceac.mvvmapp.data.repository.AuthRepositoryImpl
import com.ceac.mvvmapp.data.repository.ProductRepositoryImpl
import com.ceac.mvvmapp.domain.repository.ProductRepository
import com.ceac.mvvmapp.domain.repository.auth.AuthRepository
import com.ceac.mvvmapp.domain.usecase.GetProductsUseCase
import com.ceac.mvvmapp.domain.usecase.auth.LoginUseCase
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Paso 5: Módulo de configuración de red e inyección de dependencias (Hilt).
 *
 * Explicación:
 * Este módulo centraliza la creación y configuración de todos los servicios
 * necesarios para la comunicación con el backend:
 *
 * - DataStore / TokenStore → persistencia de tokens seguros.
 * - Moshi → serialización y deserialización JSON.
 * - OkHttp + Interceptores → gestión de cabeceras y refresco de tokens.
 * - Retrofit → cliente REST con MoshiConverterFactory.
 * - Casos de uso (UseCases) y repositorios.
 *
 * Todos los proveedores están anotados con @Singleton para mantener una
 * única instancia en el grafo de dependencias de Hilt.
 *
 * Paso siguiente:
 * Implementar los interceptores (AuthInterceptor, TokenAuthenticator) y los
 * repositorios (AuthRepositoryImpl, ProductRepositoryImpl) que usarán estas dependencias.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // -------------------------------------------------------------------------
    // DataStore / TokenStore
    // -------------------------------------------------------------------------

    /** Base URL del backend remoto.
     *
     * Cambiar a la URL del entorno real:
     * - Para emulador Android local: "http://10.0.2.2:8080/"
     * - Para dispositivo físico: IP del PC en la misma red.
     */
    private const val BASE_URL = "http://10.0.2.2:8080/"

    /**
     * Crea un DataStore<Preferences> para persistir datos clave-valor de forma segura.
     *
     * @param context Contexto de aplicación inyectado por Hilt.
     * @return Instancia singleton de DataStore<Preferences>.
     */
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("app_prefs") }
        )

    /**
     * Crea un TokenStore que utiliza DataStore internamente.
     *
     * @param ds DataStore<Preferences> inyectado.
     * @return TokenStore para lectura/escritura de tokens.
     */
    @Provides
    @Singleton
    fun provideTokenStore(ds: DataStore<Preferences>): TokenStore = TokenStore(ds)

    // -------------------------------------------------------------------------
    // Moshi (JSON Parser)
    // -------------------------------------------------------------------------

    /**
     * Crea una instancia de Moshi para la conversión JSON.
     *
     * @return Instancia singleton de Moshi.
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    // -------------------------------------------------------------------------
    // OkHttp + Retrofit (Auth) → llamadas sin autenticación
    // -------------------------------------------------------------------------

    /**
     * Cliente OkHttp simple para endpoints públicos (login, refresh...).
     *
     * Incluye interceptor de logging para depurar las peticiones y respuestas.
     *
     * @return OkHttpClient configurado para autenticación.
     */
    @Provides
    @Singleton
    @Named("authClient")
    fun provideAuthOkHttp(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    /**
     * Retrofit asociado al cliente auth (sin cabeceras de autenticación).
     *
     * @param ok Cliente OkHttp inyectado.
     * @param moshi Instancia de Moshi inyectada.
     * @return Retrofit configurado con MoshiConverterFactory.
     */
    @Provides
    @Singleton
    @Named("authRetrofit")
    fun provideAuthRetrofit(
        @Named("authClient") ok: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(ok)
            .build()

    /**
     * Crea la API de autenticación (AuthApi).
     *
     * @param rt Retrofit inyectado configurado sin token.
     * @return Instancia de AuthApi.
     */
    @Provides
    @Singleton
    fun provideAuthApi(
        @Named("authRetrofit") rt: Retrofit
    ): AuthApi = rt.create(AuthApi::class.java)

    // -------------------------------------------------------------------------
    // OkHttp + Retrofit (Authed) → llamadas con autenticación Bearer
    // -------------------------------------------------------------------------

    /**
     * Cliente OkHttp con autenticación:
     * - AuthInterceptor → añade el token de acceso a las peticiones.
     * - TokenAuthenticator → gestiona el refresh automático en 401.
     *
     * @param tokenStore Almacén de tokens.
     * @param authApi API de autenticación (para refrescar tokens).
     * @return OkHttpClient configurado con autenticación.
     */
    @Provides
    @Singleton
    @Named("authedClient")
    fun provideAuthedOkHttp(
        tokenStore: TokenStore,
        authApi: AuthApi
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenStore))
            .authenticator(TokenAuthenticator(authApi, tokenStore))
            .addInterceptor(logging)
            .build()
    }

    /**
     * Retrofit asociado al cliente autenticado (con token).
     *
     * @param ok Cliente OkHttp con autenticador.
     * @param moshi Instancia de Moshi.
     * @return Retrofit con MoshiConverterFactory y cabeceras Bearer automáticas.
     */
    @Provides
    @Singleton
    @Named("authedRetrofit")
    fun provideAuthedRetrofit(
        @Named("authedClient") ok: OkHttpClient,
        moshi: Moshi
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(ok)
            .build()

    /**
     * Crea la API de productos (ProductApi) asociada al cliente autenticado.
     *
     * @param rt Retrofit autenticado inyectado.
     * @return Instancia de ProductApi.
     */
    @Provides
    @Singleton
    fun provideProductApi(
        @Named("authedRetrofit") rt: Retrofit
    ): ProductApi = rt.create(ProductApi::class.java)

    // -------------------------------------------------------------------------
    // Casos de uso (Use Cases)
    // -------------------------------------------------------------------------

    /**
     * Proveedor del caso de uso de login.
     *
     * @param repo Repositorio de autenticación.
     * @return LoginUseCase.
     */
    @Provides
    @Singleton
    fun provideLoginUseCase(repo: AuthRepository): LoginUseCase = LoginUseCase(repo)

    /**
     * Proveedor del caso de uso de obtención de productos.
     *
     * @param repo Repositorio de productos.
     * @return GetProductsUseCase.
     */
    @Provides
    @Singleton
    fun provideGetProductsUseCase(repo: ProductRepository): GetProductsUseCase =
        GetProductsUseCase(repo)
}

/**
 * Paso 5.1: Módulo de bindings (interfaces → implementaciones).
 *
 * Explicación:
 * Usa @Binds para declarar enlaces entre interfaces del dominio y sus
 * implementaciones de la capa de datos. Es más eficiente que @Provides
 * porque no requiere instanciación manual.
 *
 * Paso siguiente:
 * Implementar los repositorios concretos (AuthRepositoryImpl, ProductRepositoryImpl)
 * y las clases auxiliares (interceptores, safeApiCall, NetworkResult).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class BindingsModule {

    /**
     * Vincula la interfaz AuthRepository con su implementación.
     *
     * @param impl Implementación concreta de AuthRepository.
     * @return AuthRepository vinculado al grafo de Hilt.
     */
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    /**
     * Vincula la interfaz ProductRepository con su implementación.
     *
     * @param impl Implementación concreta de ProductRepository.
     * @return ProductRepository vinculado al grafo de Hilt.
     */
    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository
}

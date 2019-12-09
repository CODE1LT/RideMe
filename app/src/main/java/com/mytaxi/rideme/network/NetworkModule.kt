package com.mytaxi.rideme.network

import dagger.Module
import dagger.Provides
import com.mytaxi.rideme.BuildConfig
import com.mytaxi.rideme.app.ApplicationScope
import com.mytaxi.rideme.endpointsprovider.EndpointsProvider
import com.mytaxi.rideme.endpointsprovider.EndpointsProviderModule
import com.mytaxi.rideme.network.services.api.ApiServicesModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module(includes = [ApiServicesModule::class])
class NetworkModule {

    companion object {
        const val RETROFIT_API_NO_AUTH = "RETROFIT_API_NO_AUTH"
        const val HTTP_LOGGING_INTERCEPTOR = "HTTP_LOGGING_INTERCEPTOR"
        const val OK_HTTP_CLIENT_BUILDER_BASE = "OK_HTTP_CLIENT_BUILDER_BASE"
        const val OK_HTTP_CLIENT_BASE = "OK_HTTP_CLIENT_BASE"
        const val OK_HTTP_CLIENT_BUILDER_BASE_WITH_LOGGING =
            "OK_HTTP_CLIENT_BUILDER_BASE_WITH_LOGGING"
        private const val READ_TIMEOUT = 300L
        private const val CONNECT_TIMEOUT = 300L
    }

    @Provides
    @Named(HTTP_LOGGING_INTERCEPTOR)
    @ApplicationScope
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
    }

    @Provides
    @Named(OK_HTTP_CLIENT_BUILDER_BASE)
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        return okHttpClient
    }

    @Provides
    @Named(OK_HTTP_CLIENT_BUILDER_BASE_WITH_LOGGING)
    @ApplicationScope
    fun provideBasicAuthOkHttpClientBuilderWithLogging(
        @Named(OK_HTTP_CLIENT_BUILDER_BASE) okHttpClientBuilder: OkHttpClient.Builder,
        @Named(HTTP_LOGGING_INTERCEPTOR) httpLoggingInterceptor: Interceptor
    ): OkHttpClient.Builder {
        val loggingInterceptor = httpLoggingInterceptor as HttpLoggingInterceptor
        if (BuildConfig.DEBUG_MODE) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        okHttpClientBuilder.interceptors().add(loggingInterceptor)
        return okHttpClientBuilder
    }

    @Provides
    @Named(OK_HTTP_CLIENT_BASE)
    fun provideOkHttpClient(@Named(OK_HTTP_CLIENT_BUILDER_BASE_WITH_LOGGING) okHttpClientBuilder: OkHttpClient.Builder): OkHttpClient {
        return okHttpClientBuilder.build()
    }

    @Provides
    @ApplicationScope
    fun provideRetrofitBuilder(
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
    }

    @Provides
    @ApplicationScope
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @ApplicationScope
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Named(RETROFIT_API_NO_AUTH)
    @ApplicationScope
    fun provideRetrofitAuth(
        @Named(EndpointsProviderModule.ENDPOINTS_PROVIDER) endpointsProvider: EndpointsProvider,
        @Named(OK_HTTP_CLIENT_BASE) okHttpClient: OkHttpClient,
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(endpointsProvider.getBaseUrl())
            .client(okHttpClient)
            .build()
    }
}
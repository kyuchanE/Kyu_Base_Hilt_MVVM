package kiu.dev.kyuhiltmvvm.di

import android.annotation.SuppressLint
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kiu.dev.kyuhiltmvvm.utils.Config
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieManager
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@SuppressLint("StaticFieldLeak")
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val PRINT_LOG = true // 로그 출력여부

    private const val CONNECT_TIMEOUT = 3000L // 커넥션 타임
    private const val WRITE_TIMEOUT = 3000L // 쓰기 타임
    private const val READ_TIMEOUT = 3000L // 읽기 타임

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LotteryModule

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RandomUserModule

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): LoggingInterceptor =
        LoggingInterceptor.Builder() // 전송로그
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .tag("log")
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: LoggingInterceptor): OkHttpClient =
        if (PRINT_LOG) {
            OkHttpClient.Builder()
                //            .connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS)) // https 관련 보안 옵션
                .cookieJar(JavaNetCookieJar(CookieManager()))       // 쿠키 매니저 연결
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)  // 쓰기 타임아웃 시간 설정
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)      // 읽기 타임아웃 시간 설정
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)        // 연결 타임아웃 시간 설정
                .cache(null)                                 // 캐시사용 안함
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
//                        .header("User-Agent", "")
//                        .header("devicemodel", Build.MODEL)
//                                    .header("key", "value")
                            .build()
                    )
                }
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(OkHttpProfilerInterceptor())
                .addNetworkInterceptor(StethoInterceptor()) // Stetho 로그
                .build()

        } else {
            OkHttpClient.Builder()
                //            .connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS)) // https 관련 보안 옵션
                .cookieJar(JavaNetCookieJar(CookieManager()))       // 쿠키 매니저 연결
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)  // 쓰기 타임아웃 시간 설정
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)      // 읽기 타임아웃 시간 설정
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)        // 연결 타임아웃 시간 설정
                .cache(null)                                 // 캐시사용 안함
                .addInterceptor { chain ->
                    chain.proceed(
                        chain.request()
                            .newBuilder()
//                        .header("User-Agent", "")
//                        .header("devicemodel", Build.MODEL)
//                                    .header("key", "value")
                            .build()
                    )
                }
                .build()

        }

    /**
     * 기본 설정하여 Retrofit을 반환
     *
     * @return 설정이 반영된 Retrofit
     */
    @Provides
    @Singleton
    @LotteryModule
    fun provideLotteryRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.LotteryService.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx를 사용할 수 있도록 아답터 적용
            .addConverterFactory(ScalarsConverterFactory.create())      // ScalarConverter 적용
            .addConverterFactory(GsonConverterFactory.create())         // GsonConverter 적용
            .build()

    @Provides
    @Singleton
    @RandomUserModule
    fun provideRandomUserRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.RandomUserService.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // Rx를 사용할 수 있도록 아답터 적용
            .addConverterFactory(ScalarsConverterFactory.create())      // ScalarConverter 적용
            .addConverterFactory(GsonConverterFactory.create())         // GsonConverter 적용
            .build()
}
package kiu.dev.kyuhiltmvvm.base

import android.app.Application
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // init Stetho
        Stetho.initializeWithDefaults(this)
    }
}
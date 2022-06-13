package com.vendor.mastergarage.di

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.vendor.mastergarage.datastore.ModelPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass : Application(), CameraXConfig.Provider {

    override fun onCreate() {
        super.onCreate()
        ModelPreferencesManager.with(this)
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

}
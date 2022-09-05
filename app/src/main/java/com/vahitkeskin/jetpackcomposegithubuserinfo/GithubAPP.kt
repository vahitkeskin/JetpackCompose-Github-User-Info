package com.vahitkeskin.jetpackcomposegithubuserinfo

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.core.FlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.soloader.SoLoader
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.vahitkeskin.jetpackcomposegithubuserinfo.utils.FlipperNetworkObject
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GithubAPP : Application() {

    override fun onCreate() {
        super.onCreate()
        //Facebook Flipper
        SoLoader.init(this, false) //TODO Without this it will never work
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val clint: FlipperClient = AndroidFlipperClient.getInstance(this)
            val networkFlipperPlugin = NetworkFlipperPlugin()
            FlipperNetworkObject.networkFlipperPlugin = networkFlipperPlugin
            clint.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            clint.addPlugin(networkFlipperPlugin)
            clint.start()
        }

        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(1)
            .methodOffset(5)
            .tag("")
            .build()

        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))


        Timber.plant(object : Timber.DebugTree() {
            override fun log(
                priority: Int, tag: String?, message: String, t: Throwable?
            ) {
                Logger.log(priority, "-$tag", message, t)
            }
        })

        // Usage
        Timber.d("onCreate: Inside Application!")


    }
}
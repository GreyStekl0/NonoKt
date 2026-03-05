package dev.stekl0.nonokt

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import androidx.compose.runtime.Composer
import androidx.compose.runtime.tooling.ComposeStackTraceMode
import dev.stekl0.nonokt.feature.levels.impl.di.levelsFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.os.StrictMode.ThreadPolicy.Builder as ThreadPolicyBuilder
import android.os.StrictMode.VmPolicy.Builder as VmPolicyBuilder

public class NonoktApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NonoktApplication)
            modules(levelsFeatureModule)
        }

        Composer.setDiagnosticStackTraceMode(ComposeStackTraceMode.Auto)

        setStrictModePolicy()
    }

    /**
     * Return true if the application is debuggable.
     */
    private fun isDebuggable(): Boolean = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (!isDebuggable()) return

        StrictMode.setThreadPolicy(buildThreadPolicy())
        StrictMode.setVmPolicy(buildVmPolicy())
    }

    private fun buildThreadPolicy(): StrictMode.ThreadPolicy =
        ThreadPolicyBuilder()
            .detectAll()
            .penaltyLog()
            .penaltyFlashScreen()
            .build()

    private fun buildVmPolicy(): StrictMode.VmPolicy =
        VmPolicyBuilder()
            .detectAll()
            .penaltyLog()
            .build()
}

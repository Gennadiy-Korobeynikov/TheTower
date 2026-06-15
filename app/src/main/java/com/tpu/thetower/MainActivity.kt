package com.tpu.thetower

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tpu.thetower.managers.FileSaveManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var musicManager: MusicManager

    @Inject
    lateinit var soundManager: SoundManager

    @Inject
    lateinit var saveRepo: SaveRepository

    @Inject
    lateinit var loadManager: LoadManager

    @Inject
    lateinit var fileSaveManager: FileSaveManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Запускаем музыку только при первом старте приложения, а не при пересоздании Activity.
        if (savedInstanceState == null) {
            musicManager.playMusic(R.raw.soundtrack_1)
        }

        val prefs = AppPreferences(this)
        //TEST: режим разработчика (макс. уровень доступа, кнопка пропуска пазлов)
        prefs.isDevMode = false
        prefs.isMaxAccessLvl = false

        setManagers()

        setupUiVisibilityByDestination()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_bg) as? NavHostFragment
                val currentFragment = navHostFragment?.childFragmentManager?.fragments?.lastOrNull()
                if (currentFragment is Fragment) {
                    UiVisibilityController.show(this@MainActivity, UiVisibilityController.UiContainer.MENU)
                }
            }
        })
    }


    private fun setupUiVisibilityByDestination() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv_bg) as? NavHostFragment
            ?: return
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.titleScreenFragment,
                R.id.settingsFragment -> {
                    UiVisibilityController.hide(this, UiVisibilityController.UiContainer.TOPBAR_UI,
                        UiVisibilityController.UiContainer.GO_BACK_ARROW)
                }

                else -> {
                    // По умолчанию для игровых экранов HUD виден.
                    UiVisibilityController.show(this, UiVisibilityController.UiContainer.TOPBAR_UI,
                        UiVisibilityController.UiContainer.GO_BACK_ARROW)
                }
            }
        }
    }

    private fun setManagers() {
        fileSaveManager.ensureSaveExists()

        loadManager.invalidateCache()
        loadManager.loadSettings()

        soundManager.init()
        soundManager.loadSounds(
            SoundEffect.entries
        )
    }

    override fun onStart() {
        super.onStart()
        musicManager.resumeMusic()
    }

    override fun onStop() {
        super.onStop()
        musicManager.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        musicManager.stopMusic()
        soundManager.release()
    }
}

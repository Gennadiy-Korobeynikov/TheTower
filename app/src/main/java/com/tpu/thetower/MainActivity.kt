package com.tpu.thetower

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.tpu.thetower.managers.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import com.tpu.thetower.managers.FileSaveManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import com.tpu.thetower.utils.SoundEffect

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

        val prefs = AppPreferences(this)
        //TEST: режим разработчика (макс. уровень доступа, кнопка пропуска пазлов)
        prefs.isDevMode = true
        prefs.isMaxAccessLvl = true

        setManagers()

        setupUiVisibilityByDestination()

        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
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
                    UiVisibilityController.hide(this, UiVisibilityController.UiContainer.HUD,
                        UiVisibilityController.UiContainer.GO_BACK_ARROW)
                }

                else -> {
                    // По умолчанию для игровых экранов HUD виден.
                    UiVisibilityController.show(this, UiVisibilityController.UiContainer.HUD,
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

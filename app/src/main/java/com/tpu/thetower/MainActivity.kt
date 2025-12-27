package com.tpu.thetower

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import java.io.File

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        copyJsonFromAssets(this, "save_file.json")
        loadManager.loadProgress()

        setManagers()
        saveRepo.savePuzzleUsedHintsCount(0, "flashlight", 0) // TEST
        saveRepo.savePuzzleUsedHintsCount(0, "lock", 0) // TEST

        loadManager.loadSettings()

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

    private fun setManagers() {
        soundManager.init()
        soundManager.loadSound(
            listOf(
                R.raw.sound_of_guard_snoring
            )
        )
    }

    fun copyJsonFromAssets(context: Context, fileName: String) {
        val file = File(context.filesDir, fileName)

        if (!file.exists()) { // Копируем, только если файла нет
            context.assets.open(fileName).use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
    }
}

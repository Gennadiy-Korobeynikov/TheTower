package com.tpu.thetower

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.managers.UiVisibilityController
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var musicManager: MusicManager
    protected lateinit var soundManager: SoundManager
    private val saveRepo: SaveRepository = SaveRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        copyJsonFromAssets(this, "save_file.json")
        LoadManager.loadProgress(this)

        setManagers()
        saveRepo.savePuzzleUsedHintsCount(this,0, "flashlight",0)// TEST
        saveRepo.savePuzzleUsedHintsCount(this,0, "lock",0)// TEST

        // Когда появится кнопка сброса прогресса
        //LoadManager.loadProgress()

        LoadManager.loadSettings(this)

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
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            this, listOf(
                R.raw.sound_of_guard_snoring
            )
        )
        // SaveRepository создаётся лениво; отдельной инициализации не требуется.
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

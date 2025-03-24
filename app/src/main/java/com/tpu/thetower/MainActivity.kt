package com.tpu.thetower

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LoadManager.setGameData(this)
        setContentView(R.layout.activity_main)

        copyJsonFromAssets(this, "save_file.json")
        setManagers()
        saveManager.savePuzzleUsedHintsCount(this,0, 0,0)// TEST
        saveManager.savePuzzleUsedHintsCount(this,0, 1,0)// TEST

        // Когда появится кнопка сброса прогресса
        //LoadManager.loadProgress()

        LoadManager.loadSettings(this)



//        deleteJsonFile(this, "save_file.json")

        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

    }


    private fun setManagers() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        saveManager = SaveManager.getInstance()
    }

//    private fun loadSettings() {
//
//
//        saveManager.saveAccessLevel(this , 0) // Fot TEst
//
//
//        val gameData = saveManager.readData(this)
//        val savedMusicVolume = gameData?.gameSettings?.musicVolume ?: 0.5f
//        val savedSoundVolume = gameData?.gameSettings?.soundVolume ?: 0.5f
//
//        musicManager.setVolume(savedMusicVolume)
//        soundManager.setVolume(savedSoundVolume)
//    }

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
//
//    fun deleteJsonFile(context: Context, fileName: String) {
//        val file = File(context.filesDir, fileName)
//            file.delete()
//    }

}

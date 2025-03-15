package com.tpu.thetower

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var musicManager: MusicManager
    private lateinit var soundManager: SoundManager
    private lateinit var saveManager: SaveManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        DialogManager.loadCharacters()
        DialogManager.loadDialogs(this)


        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        saveManager = SaveManager.getInstance()


        val gameData = saveManager.readData(this)
        val savedMusicVolume = gameData?.gameSettings?.musicVolume ?: 0.5f
        val savedSoundVolume = gameData?.gameSettings?.soundVolume ?: 0.5f

        musicManager.setVolume(savedMusicVolume)
        soundManager.setVolume(savedSoundVolume)
        loadSettings()

//        deleteJsonFile(this, "save_file.json")
//        copyJsonFromAssets(this, "save_file.json")




        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }

    }

    fun loadSettings() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        saveManager = SaveManager.getInstance()


        val gameData = saveManager.readData(this)
        val savedMusicVolume = gameData?.gameSettings?.musicVolume ?: 0.5f
        val savedSoundVolume = gameData?.gameSettings?.soundVolume ?: 0.5f

        musicManager.setVolume(savedMusicVolume)
        soundManager.setVolume(savedSoundVolume)
    }






//    fun copyJsonFromAssets(context: Context, fileName: String) {
//        val file = File(context.filesDir, fileName)
//
//        if (!file.exists()) { // Копируем, только если файла нет
//            context.assets.open(fileName).use { inputStream ->
//                file.outputStream().use { outputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//            }
//        }
//    }
//
//    fun deleteJsonFile(context: Context, fileName: String) {
//        val file = File(context.filesDir, fileName)
//            file.delete()
//    }

}

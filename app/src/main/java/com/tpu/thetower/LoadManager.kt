package com.tpu.thetower

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class LoadManager private constructor(activity: MainActivity) {

    private var activityRef: WeakReference<MainActivity> = WeakReference(activity)

    companion object {
        private var instance: LoadManager? = null

        @Synchronized
        fun getInstance(activity: MainActivity): LoadManager {
            return instance?.apply { activityRef = WeakReference(activity) }
                ?: synchronized(this) {
                    instance ?: LoadManager(activity).also { instance = it }
                }
        }
    }

    private var activity: MainActivity = activityRef.get()!!
    private val saveManager = SaveManager.getInstance()
    private val gameData = saveManager.readData(activity)
    private val musicManager = MusicManager.getInstance()
    private val soundManager = SoundManager.getInstance()
    private val currFragment = activity.supportFragmentManager.findFragmentById(R.id.fcv_bg)!!


    private val levels = listOf(
        R.id.action_elevatorFragment_to_lvlTestFragment,
        R.id.action_elevatorFragment_to_lvl0Fragment,
        R.id.action_elevatorFragment_to_lvl1Fragment
    )


    fun loadProgress() {
        DialogManager.loadCharacters()
        DialogManager.loadDialogs(activity)

        LevelAccessManager.currentAccessLvl = gameData?.playerInfo?.accessLevel ?: 0
        LevelAccessManager.unlockModules(currFragment)

    }


    fun startSavedLevel() {
        val savedLevel = gameData?.playerInfo?.currentLevel ?: 0
        val bundle = Bundle().apply {
            putString("saved_level", levels[savedLevel + 1].toString())
        }
        FragmentManager.changeBG(
            currFragment,
            R.id.action_titleScreenFragment_to_elevatorFragment,
            bundle
        )
    }


    fun loadSettings() {
        // Временно здесь
        DialogManager.loadCharacters()
        DialogManager.loadDialogs(activity)

        saveManager.saveAccessLevel(activity, 0) // Fot TEst

        val savedMusicVolume = gameData?.gameSettings?.musicVolume ?: 0.5f
        val savedSoundVolume = gameData?.gameSettings?.soundVolume ?: 0.5f

        musicManager.setVolume(savedMusicVolume)
        soundManager.setVolume(savedSoundVolume)
    }
}
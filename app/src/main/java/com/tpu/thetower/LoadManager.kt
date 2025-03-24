package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

class LoadManager {
    companion object {

        private val saveManager = SaveManager.getInstance()
        private lateinit var gameData: SaveManager.SaveData
        private val musicManager = MusicManager.getInstance()
        private val soundManager = SoundManager.getInstance()


        private val levels = listOf(
            R.id.action_elevatorFragment_to_lvlTestFragment,
            R.id.action_elevatorFragment_to_lvl0Fragment,
            R.id.action_elevatorFragment_to_lvl1Fragment
        )

        fun setGameData(activity: Activity) {
            gameData = saveManager.readData(activity)!!
        }

        fun loadProgress(activity: Activity) {
            setGameData(activity)
            DialogManager.loadCharacters()
            DialogManager.loadDialogs(activity)

            LevelAccessManager.unlockModules(getCurrFragment(activity))
        }

        private fun getCurrFragment(activity: Activity): Fragment {
            return (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.fcv_bg)!!
        }

        fun startSavedLevel(activity: Activity) {
            setGameData(activity)
            val savedLevel = gameData.playerInfo.currentLevel ?: 0
            val bundle = Bundle().apply {
                // Потом убрать + 1, потому что пропадёт тестовый уровень
                putString("saved_level", levels[savedLevel + 1].toString())
            }
            FragmentManager.changeBG(
                getCurrFragment(activity),
                R.id.action_titleScreenFragment_to_elevatorFragment,
                bundle
            )
        }

        fun loadSettings(activity: Activity) {
            setGameData(activity)
            // Временно здесь
            DialogManager.loadCharacters()
            DialogManager.loadDialogs(activity)


            val savedMusicVolume = gameData.gameSettings.musicVolume
            val savedSoundVolume = gameData.gameSettings.soundVolume

            musicManager.setVolume(savedMusicVolume)
            soundManager.setVolume(savedSoundVolume)
        }

//        fun getPuzzleStatus(level: Int, puzzle: Int): String {
//            return gameData.levels[level].puzzles[puzzle].status
//        }

        fun getPuzzleUsedHintsCount(activity: Activity, level: Int, puzzle: Int): Int {
            setGameData(activity)
            return gameData.levels[level].puzzles[puzzle].hintsUsed //Пока так
        }

        fun getLevelProgress(activity: Activity, level: Int): Int {
            setGameData(activity)
            return gameData.levels[level].puzzles.count {it.status == "completed"}
        }

        fun getAccessLevel(activity: Activity) : Int {
            setGameData(activity)

            return gameData.playerInfo.accessLevel ?: 0
        }
    }
}
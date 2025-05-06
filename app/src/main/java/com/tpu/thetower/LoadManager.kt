package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class LoadManager {
    companion object {


        public var isASKII = false // TODO Исправить!!!!! длолжно быть через сохранения

        private val saveManager = SaveManager.getInstance()
        private lateinit var gameData: SaveManager.SaveData
        private val musicManager = MusicManager.getInstance()
        private val soundManager = SoundManager.getInstance()


        private val levels = listOf(
            R.id.action_elevatorFragment_to_lvlTestFragment,
            R.id.action_elevatorFragment_to_lvl0Fragment,
            R.id.action_elevatorFragment_to_lvl1Fragment,
            R.id.action_elevatorFragment_to_lvl2Fragment,
            R.id.action_elevatorFragment_to_lvl3Fragment
        )

        fun setGameData(activity: Activity) {
            gameData = saveManager.readData(activity)!!
        }

        fun loadProgress(activity: Activity) {
            setGameData(activity)
            DialogManager.loadCharacters()
            DialogManager.loadDialogs(activity)

            LevelAccessManager.currentAccessLvl = gameData.playerInfo.accessLevel
            LevelAccessManager.unlockModules(getCurrFragment(activity))

        }

        private fun getCurrFragment(activity: Activity): Fragment {
            return (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.fcv_bg)!!
        }

        fun startSavedLevel(activity: Activity) {
            setGameData(activity)
            val savedLevel = gameData.playerInfo.currentLevel
            val bundle = Bundle().apply {
                // Потом убрать + 1, потому что пропадёт тестовый уровень
                putString("saved_level", levels[savedLevel + 1].toString())
            }
            FragmentManager.changeBG(
                getCurrFragment(activity),
                R.id.action_global_elevatorFragment,
                bundle
            )
        }


        fun loadSettings(activity: Activity) {
            setGameData(activity)
            // Временно здесь
            DialogManager.loadCharacters()
            DialogManager.loadDialogs(activity)

            val savedMusicVolume = gameData.gameSettings.musicVolume ?: 0.5f
            val savedSoundVolume = gameData.gameSettings.soundVolume ?: 0.5f

            musicManager.setVolume(savedMusicVolume)
            soundManager.setVolume(savedSoundVolume)
        }

//        fun getPuzzleStatus(level: Int, puzzle: Int): String {
//            return gameData.levels[level].puzzles[puzzle].status
//        }

        fun getPuzzleUsedHintsCount(activity: Activity, level: Int, puzzle: String): Int {
            setGameData(activity)
            return gameData.levels.find { it.id == level }?.puzzles?.find { it.name == puzzle }?.hintsUsed ?: 0 //Пока так
        }

        fun getLevelProgress(activity: Activity, level: Int): Int {
            setGameData(activity)
            return gameData.levels.find { it.id == level }?.puzzles?.count { it.status == "completed" } ?: 0
        }

        fun isPuzzleCompleted(activity: Activity, level: Int, puzzle: String) : Boolean {
            setGameData(activity)
            return gameData.levels.find { it.id == level }?.puzzles?.find { it.name == puzzle }?.status == "completed"
        }

        fun isLevelCompleted(activity: Activity, level: Int): Boolean {
            setGameData(activity)

            return getLevelProgress(activity, level) == gameData.levels.find { it.id == level }?.puzzles?.size
        }

        fun getBlockProgress(activity: Activity, borders: Pair<Int, Int>): Int {
            var progressStatus = 0

            for (level: Int in borders.first..borders.second) {
                if (isLevelCompleted(activity, level)) {
                    progressStatus += 20
                }
            }

            return progressStatus
        }

        fun getAccessLevel(activity: Activity): Int {
            setGameData(activity)

            return gameData.playerInfo.accessLevel
        }

        fun getCurrentDialog(activity: Activity, level: Int, npc: Int): Int {
            setGameData(activity)

            return gameData.levels.find { it.id == level }?.npcDialogs?.find { it.id == npc }?.currentDialogIndex ?: 0
        }

        fun getPuzzleStatus(activity: Activity, level: Int, puzzle: String): String {
            setGameData(activity)
            return gameData.levels.find { it.id == level }?.puzzles?.find { it.name == puzzle }?.status ?: "locked"
        }
    }
}
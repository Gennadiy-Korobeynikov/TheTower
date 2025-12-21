package com.tpu.thetower.managers

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tpu.thetower.MainActivity
import com.tpu.thetower.R

class LoadManager {
    companion object {
        public var isASKII = false // TODO Исправить!!!!! длолжно быть через сохранения

        private val repo = SaveRepository.getInstance()

        private val musicManager = MusicManager.getInstance()
        private val soundManager = SoundManager.getInstance()


        private val levels = listOf(
            R.id.action_elevatorFragment_to_lvl0Fragment,
            R.id.action_elevatorFragment_to_lvl1Fragment,
            R.id.action_elevatorFragment_to_lvl2Fragment,
            R.id.action_elevatorFragment_to_lvl3Fragment,
            R.id.action_elevatorFragment_to_lvl4Fragment,
            R.id.action_elevatorFragment_to_lvl5Fragment,
            R.id.action_elevatorFragment_to_lvl6Fragment
        )

        fun refreshCache(activity: Activity) {
            repo.refreshCache(activity)
        }

        fun invalidateCache() {
            repo.invalidateCache()
        }

        fun loadProgress(activity: Activity) {
            val data = repo.get(activity)
            LevelAccessManager.currentAccessLvl = data.playerInfo.accessLevel
        }

        fun startSavedLevel(activity: Activity) {
            val data = repo.get(activity)
            val savedLevel = data.playerInfo.currentLevel
            val bundle = Bundle().apply {
                putString("saved_level", levels[savedLevel].toString())
            }
            FragmentNavigation.changeBG(
                getCurrFragment(activity),
                R.id.action_global_elevatorFragment,
                bundle
            )
        }

        fun loadSettings(activity: Activity) {
            val data = repo.get(activity)
            musicManager.setVolume(data.gameSettings.musicVolume)
            soundManager.setVolume(data.gameSettings.soundVolume)
        }

        fun getCurrentLevel(activity: Activity): Int =
            repo.get(activity).playerInfo.currentLevel

        fun getPuzzleUsedHintsCount(activity: Activity, level: Int, puzzle: String): Int {
            val data = repo.get(activity)
            return data.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.hintsUsed ?: 0
        }

        fun getLevelProgress(activity: Activity, level: Int): Pair<Int, Int> {
            val data = repo.get(activity)
            val lvl = data.levels.find { it.id == level }
            return Pair(
                lvl?.puzzles?.count { it.status == "completed" } ?: 0,
                lvl?.puzzles?.size ?: 0
            )
        }

        fun isPuzzleCompleted(activity: Activity, level: Int, puzzle: String): Boolean {
            val data = repo.get(activity)
            return data.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.status == "completed"
        }

        fun getLevelStatus(activity: Activity, level: Int): Boolean {
            val data = repo.get(activity)
            return data.levels.find { it.id == level }?.isCompleted ?: false
        }

        fun getAccessLevel(activity: Activity): Int =
            repo.get(activity).playerInfo.accessLevel

        fun getCurrentDialog(activity: Activity, level: Int, npc: Int): Int {
            val data = repo.get(activity)
            return data.levels.find { it.id == level }
                ?.npcDialogs?.find { it.id == npc }
                ?.currentDialogIndex ?: 0
        }

        fun getPuzzleStatus(activity: Activity, level: Int, puzzle: String): String {
            val data = repo.get(activity)
            return data.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.status ?: "locked"
        }

        fun isLevelCompleted(activity: Activity, level: Int): Boolean {
            val data = repo.get(activity)
            val lvl = data.levels.find { it.id == level } ?: return false
            return lvl.puzzles.isNotEmpty() && lvl.puzzles.all { it.status == "completed" }
        }

        private fun getCurrFragment(activity: Activity): Fragment {
            return (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.fcv_bg)!!
        }

    }
}
package com.tpu.thetower.managers

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tpu.thetower.MainActivity
import com.tpu.thetower.R
import com.tpu.thetower.models.PuzzleStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadManager @Inject constructor(
    private val repo: SaveRepository,
    private val musicManager: MusicManager,
    private val soundManager: SoundManager
) {
    companion object {
        var isASKII = false // TODO Исправить!!!!! длолжно быть через сохранения

        private val levels = listOf(
            R.id.action_elevatorFragment_to_lvl0Fragment,
            R.id.action_elevatorFragment_to_lvl1Fragment,
            R.id.action_elevatorFragment_to_lvl2Fragment,
            R.id.action_elevatorFragment_to_lvl3Fragment,
            R.id.action_elevatorFragment_to_lvl4Fragment,
            R.id.action_elevatorFragment_to_lvl5Fragment,
            R.id.action_elevatorFragment_to_lvl6Fragment
        )
    }

    fun refreshCache() {
        repo.refreshCache()
    }

    fun invalidateCache() {
        repo.invalidateCache()
    }

    fun loadSettings() {
        val data = repo.get()
        musicManager.setVolume(data.gameSettings.musicVolume)
        soundManager.setVolume(data.gameSettings.soundVolume)
    }

    fun startSavedLevel(activity: Activity) {
        val data = repo.get()
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

    fun getAccessLevel(): Int =
        repo.get().playerInfo.accessLevel

    fun getCurrentAccessCardNumber(): Int =
        repo.get().playerInfo.accessCardNumber

    fun getCurrentDialog(level: Int, npc: Int): Int {
        val data = repo.get()
        return data.levels.find { it.id == level }
            ?.npcDialogs?.find { it.id == npc }
            ?.currentDialogIndex ?: 0
    }

    fun getCurrentLevel(): Int =
        repo.get().playerInfo.currentLevel

    fun getLevelProgress(level: Int): Pair<Int, Int> {
        val data = repo.get()
        val lvl = data.levels.find { it.id == level }
        return Pair(
            lvl?.puzzles?.count { it.status == PuzzleStatus.COMPLETED.value } ?: 0,
            lvl?.puzzles?.size ?: 0
        )
    }

    fun isLevelCompleted(level: Int): Boolean {
        val data = repo.get()
        return data.levels.find { it.id == level }
            ?.isCompleted ?: false
    }

    fun getPuzzleUsedHintsCount(level: Int, puzzle: String): Int {
        val data = repo.get()
        return data.levels.find { it.id == level }
            ?.puzzles?.find { it.name == puzzle }
            ?.hintsUsed ?: 0
    }

    fun getPuzzleStatus(level: Int, puzzle: String): String {
        val data = repo.get()
        return data.levels.find { it.id == level }
            ?.puzzles?.find { it.name == puzzle }
            ?.status ?: PuzzleStatus.LOCKED.value
    }

    private fun getCurrFragment(activity: Activity): Fragment {
        return (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.fcv_bg)!!
    }
}
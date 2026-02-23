package com.tpu.thetower.managers

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
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
        private val levels = listOf(
            R.id.action_elevatorFragment_to_lvl0Fragment,
            R.id.action_elevatorFragment_to_lvl1Fragment,
            R.id.action_elevatorFragment_to_lvl2Fragment,
            R.id.action_elevatorFragment_to_lvl3Fragment,
            R.id.action_elevatorFragment_to_lvl4Fragment,
            R.id.action_elevatorFragment_to_lvl5Fragment,
            R.id.action_elevatorFragment_to_lvl6Fragment
        )

        private val cardImageIds: List<Int> = listOf(
            R.drawable.access_card_1,
            R.drawable.access_card_2,
            R.drawable.access_card_3,
            R.drawable.access_card_4,
            R.drawable.access_card_5,
            R.drawable.access_card_6
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

    fun getCurrentDialogIndex(level: Int, key: String): Int {
        val data = repo.get()
        return data.levels.find { it.id == level }
            ?.dialogs?.find { it.dialogKey == key }
            ?.currentDialogIndex ?: 0
    }

    fun getCurrentLevel(): Int =
        repo.get().playerInfo.currentLevel

    fun getCurrentLevelFragmentId(): Int {
        val currentLevel = getCurrentLevel()
        return levels[currentLevel]
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

    fun getLevelExtraState(level: Int, key: String): JsonElement? {
        val data = repo.get()
        return data.levels.find { it.id == level }
            ?.extraState
            ?.get(key)
    }

    fun getLevelExtraStateString(level: Int, key: String, default: String? = null): String? =
        (getLevelExtraState(level, key) as? JsonPrimitive)?.asString ?: default

    fun getLevelExtraStateInt(level: Int, key: String, default: Int = 0): Int =
        (getLevelExtraState(level, key) as? JsonPrimitive)?.asInt ?: default

    fun getLevelExtraStateBoolean(level: Int, key: String, default: Boolean = false): Boolean =
        (getLevelExtraState(level, key) as? JsonPrimitive)?.asBoolean ?: default

    fun getLevelExtraStateFloat(level: Int, key: String, default: Float = 0f): Float =
        (getLevelExtraState(level, key) as? JsonPrimitive)?.asFloat ?: default

    fun getCardImage(cardNumber : Int): Int {
        return cardImageIds[cardNumber-1] //
    }

    fun changeAccessCardNumber(newCardNumber: Int) {
        repo.saveAccessCardNumber(newCardNumber)
    }

    fun updateAccessLvl(newAccessLvl: Int) : Int {
        repo.saveAccessLevel(newAccessLvl)
        return newAccessLvl
    }

    private fun getCurrFragment(activity: Activity): Fragment {
        return (activity as MainActivity).supportFragmentManager.findFragmentById(R.id.fcv_bg)!!
    }


}
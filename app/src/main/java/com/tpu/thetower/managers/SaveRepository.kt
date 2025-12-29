package com.tpu.thetower.managers

import com.tpu.thetower.models.SaveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveRepository @Inject constructor(
    private val fileSaveManager: FileSaveManager
) {
    @Volatile
    private var cache: SaveData? = null

    fun invalidateCache() {
        cache = null
    }

    fun refreshCache(): SaveData {
        val loaded = fileSaveManager.readData()
            ?: throw IllegalStateException("SaveData is missing/unreadable")
        cache = loaded
        return loaded
    }

    fun get(): SaveData {
        val cached = cache
        if (cached != null) return cached
        return refreshCache()
    }

    private fun updateCache(updated: SaveData?) {
        if (updated != null) cache = updated else invalidateCache()
    }

    fun saveMusicVolume(volume: Float) {
        updateCache(fileSaveManager.update {
            it.copy(gameSettings = it.gameSettings.copy(musicVolume = volume))
        })
    }

    fun saveSoundVolume(volume: Float) {
        updateCache(fileSaveManager.update {
            it.copy(gameSettings = it.gameSettings.copy(soundVolume = volume))
        })
    }

    fun saveCurrentLevel(level: Int) {
        updateCache(fileSaveManager.update {
            it.copy(playerInfo = it.playerInfo.copy(currentLevel = level))
        })
    }

    fun saveAccessLevel(currAccessLevel: Int) {
        updateCache(fileSaveManager.update {
            it.copy(playerInfo = it.playerInfo.copy(accessLevel = currAccessLevel))
        })
    }

    fun saveAccessCardNumber(currCardLvl: Int) {
        updateCache(fileSaveManager.update {
            it.copy(playerInfo = it.playerInfo.copy(accessCardNumber = currCardLvl))
        })
    }

    fun savePuzzleUsedHintsCount(level: Int, puzzle: String, hintUsed: Int) {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.hintsUsed = hintUsed
            updated
        })
    }

    fun saveLevelCompletedStatus(level: Int) {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }?.isCompleted = true
            updated
        })
    }

    fun saveCurrentDialog(level: Int, npc: Int, dialogIndex: Int) {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.npcDialogs?.find { it.id == npc }
                ?.currentDialogIndex = dialogIndex
            updated
        })
    }

    fun savePuzzleData(level: Int, puzzle: String, status: String = "completed") {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.status = status
            updated
        })
    }
}

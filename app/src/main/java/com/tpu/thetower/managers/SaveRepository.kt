package com.tpu.thetower.managers

import android.app.Activity
import com.tpu.thetower.models.SaveData

class SaveRepository private constructor(
    private val fileSaveManager: FileSaveManager = FileSaveManager.getInstance()
) {
    @Volatile
    private var cache: SaveData? = null

    companion object {
        @Volatile
        private var instance: SaveRepository? = null

        fun getInstance(): SaveRepository {
            val existing = instance
            if (existing != null) return existing
            return synchronized(this) {
                val again = instance
                if (again != null) again else SaveRepository().also { instance = it }
            }
        }
    }

    fun invalidateCache() {
        cache = null
    }

    fun refreshCache(activity: Activity): SaveData {
        val loaded = fileSaveManager.readData(activity)
            ?: throw IllegalStateException("SaveData is missing/unreadable")
        cache = loaded
        return loaded
    }

    fun get(activity: Activity): SaveData {
        val cached = cache
        if (cached != null) return cached
        return refreshCache(activity)
    }

    private fun updateCache(updated: SaveData?) {
        if (updated != null) cache = updated else invalidateCache()
    }

    fun saveMusicVolume(activity: Activity, volume: Float) {
        updateCache(fileSaveManager.update(activity) {
            it.copy(gameSettings = it.gameSettings.copy(musicVolume = volume))
        })
    }

    fun saveSoundVolume(activity: Activity, volume: Float) {
        updateCache(fileSaveManager.update(activity) {
            it.copy(gameSettings = it.gameSettings.copy(soundVolume = volume))
        })
    }

    fun saveCurrentLevel(activity: Activity, level: Int) {
        updateCache(fileSaveManager.update(activity) {
            it.copy(playerInfo = it.playerInfo.copy(currentLevel = level))
        })
    }

    fun saveAccessLevel(activity: Activity, currAccessLevel: Int) {
        updateCache(fileSaveManager.update(activity) {
            it.copy(playerInfo = it.playerInfo.copy(accessLevel = currAccessLevel))
        })
    }

    fun savePuzzleUsedHintsCount(activity: Activity, level: Int, puzzle: String, hintUsed: Int) {
        updateCache(fileSaveManager.update(activity) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.hintsUsed = hintUsed
            updated
        })
    }

    fun saveLevelStatus(activity: Activity, level: Int) {
        updateCache(fileSaveManager.update(activity) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }?.isCompleted = true
            updated
        })
    }

    fun saveCurrentDialog(activity: Activity, level: Int, npc: Int, dialogIndex: Int) {
        updateCache(fileSaveManager.update(activity) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.npcDialogs?.find { it.id == npc }
                ?.currentDialogIndex = dialogIndex
            updated
        })
    }

    /**
     * Аналог старого SaveManager.savePuzzleData: меняет статус головоломки внутри уровня.
     * Если status не передан, считаем, что пазл завершён.
     */
    fun savePuzzleData(activity: Activity, level: Int, puzzle: String, status: String = "completed") {
        updateCache(fileSaveManager.update(activity) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.status = status
            updated
        })
    }
}

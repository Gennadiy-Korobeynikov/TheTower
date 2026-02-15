package com.tpu.thetower.managers

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.models.SaveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveRepository @Inject constructor(
    private val fileSaveManager: FileSaveManager
) {
    @Volatile
    private var cache: SaveData? = null

    private val gson = Gson()

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

    fun saveCurrentDialogIndex(level: Int, key: String, dialogIndex: Int) {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.dialogs?.find { it.dialogKey == key }
                ?.currentDialogIndex = dialogIndex
            updated
        })
    }

    fun savePuzzleStatus(level: Int, puzzle: String, status: String = PuzzleStatus.COMPLETED.value) {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.status = status
            updated
        })
    }

    fun resetFileData() {
        fileSaveManager.resetData()
    }

    /**
     * Универсальное сохранение произвольного состояния уровня.
     * value поддерживает: String/Number/Boolean/Char, JsonElement, Map/List, любой POJO (через Gson).
     * null => ключ будет записан как JsonNull.
     */
    fun saveLevelExtraState(level: Int, key: String, value: Any?) {
        updateCache(fileSaveManager.update { gameData ->
            val updated = gameData.copy()
            val lvl = updated.levels.find { it.id == level } ?: return@update updated

            val state: JsonObject = (lvl.extraState ?: JsonObject()).also { lvl.extraState = it }
            state.add(key, value.toJsonElement(gson))

            updated
        })
    }

    private fun Any?.toJsonElement(gson: Gson): JsonElement = when (this) {
        null -> JsonNull.INSTANCE
        is JsonElement -> this
        is String -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        is Char -> JsonPrimitive(this)
        else -> gson.toJsonTree(this)
    }
}

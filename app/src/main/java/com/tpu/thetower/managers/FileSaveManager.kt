package com.tpu.thetower.managers

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.tpu.thetower.models.SaveData
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class FileSaveManager private constructor() {

    private val gson = Gson()
    private val ioLock = Any()

    companion object {
        private var instance: FileSaveManager? = null
        private const val SAVE_FILE_NAME = "save_file.json"

        @Synchronized
        fun getInstance(): FileSaveManager {
            if (instance == null) instance = FileSaveManager()
            return instance!!
        }
    }

    private fun getSaveFile(context: Context) =
        File(context.filesDir, SAVE_FILE_NAME)

    fun readData(context: Context): SaveData? =
        synchronized(ioLock) { readDataLocked(context) }

    fun ensureSaveExists(context: Context) =
        synchronized(ioLock) { ensureSaveExistsLocked(context) }

    fun saveData(context: Context, saveData: SaveData) =
        synchronized(ioLock) { saveDataLocked(context, saveData) }

    fun update(context: Context, transform: (SaveData) -> SaveData): SaveData? {
        synchronized(ioLock) {
            val current = readDataLocked(context) ?: return null
            val updated = transform(current)
            saveDataLocked(context, updated)
            return updated
        }
    }

    fun resetData(context: Context) {
        synchronized(ioLock) {
            val previous = readDataLocked(context) ?: return

            val file = getSaveFile(context)
            if (file.exists()) file.delete()

            ensureSaveExistsLocked(context)

            val currentData = readDataLocked(context)
            val updatedData = currentData?.copy(gameSettings = previous.gameSettings)
            if (updatedData != null) saveDataLocked(context, updatedData)
        }
    }

    fun saveMusicVolume(context: Context, volume: Float) {
        update(context) { gameData ->
            gameData.copy(gameSettings = gameData.gameSettings.copy(musicVolume = volume))
        }
    }

    fun saveSoundVolume(context: Context, volume: Float) {
        update(context) { gameData ->
            gameData.copy(gameSettings = gameData.gameSettings.copy(soundVolume = volume))
        }
    }

    fun saveCurrentLevel(context: Context, level: Int) {
        update(context) { gameData ->
            gameData.copy(playerInfo = gameData.playerInfo.copy(currentLevel = level))
        }
    }

    fun saveAccessLevel(context: Context, currAccessLevel: Int) {
        update(context) { gameData ->
            gameData.copy(playerInfo = gameData.playerInfo.copy(accessLevel = currAccessLevel))
        }
    }

    fun savePuzzleUsedHintsCount(context: Context, level: Int, puzzle: String, hintUsed: Int) {
        update(context) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.puzzles?.find { it.name == puzzle }
                ?.hintsUsed = hintUsed
            updated
        }
    }

    fun saveLevelStatus(context: Context, level: Int) {
        update(context) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.isCompleted = true
            updated
        }
    }

    fun saveCurrentDialog(activity: Activity, level: Int, npc: Int, dialogIndex: Int) {
        update(activity) { gameData ->
            val updated = gameData.copy()
            updated.levels.find { it.id == level }
                ?.npcDialogs?.find { it.id == npc }
                ?.currentDialogIndex = dialogIndex
            updated
        }
    }

    private fun ensureSaveExistsLocked(context: Context) {
        val file = getSaveFile(context)
        if (file.exists()) return

        context.assets.open(SAVE_FILE_NAME).use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
    }

    private fun readDataLocked(context: Context): SaveData? {
        ensureSaveExistsLocked(context)
        val file = getSaveFile(context)
        if (!file.exists()) return null

        FileReader(file).use { reader ->
            return gson.fromJson(reader, SaveData::class.java)
        }
    }

    private fun saveDataLocked(context: Context, saveData: SaveData) {
        val file = getSaveFile(context)
        val tmp = File(context.filesDir, "$SAVE_FILE_NAME.tmp")

        FileWriter(tmp).use { writer -> gson.toJson(saveData, writer) }

        if (file.exists() && !file.delete()) {
            throw IllegalStateException("Cannot replace existing save file: ${file.absolutePath}")
        }
        if (!tmp.renameTo(file)) {
            throw IllegalStateException("Cannot move temp save file into place: ${tmp.absolutePath}")
        }
    }
}
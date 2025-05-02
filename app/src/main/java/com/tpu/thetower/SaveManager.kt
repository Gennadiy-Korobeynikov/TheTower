package com.tpu.thetower

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.tpu.thetower.models.GameSettings
import com.tpu.thetower.models.LevelData
import com.tpu.thetower.models.PlayerInfo
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class SaveManager private constructor() {

    private val gson = Gson()

    companion object {
        private var instance: SaveManager? = null

        @Synchronized
        fun getInstance(): SaveManager {
            if (instance == null) {
                instance = SaveManager()
            }
            return instance!!
        }
    }


    fun readData(context: Context): SaveData? {
        val file = File(context.filesDir, "save_file.json")
        if (file.exists()) {
            val fileReader = FileReader(file)
            val saveData = gson.fromJson(fileReader, SaveData::class.java)
            fileReader.close()
            return saveData
        }
        return null
    }

    fun saveData(context: Context, saveData: SaveData) {
        val file = File(context.filesDir, "save_file.json")
        val fileWriter = FileWriter(file)
        gson.toJson(saveData, fileWriter)
        fileWriter.close()
    }

    fun resetData(context: Context) {
        val saveData = readData(context) ?: return

        val file = File(context.filesDir, "save_file.json")
        file.delete()
        context.assets.open("save_file.json").use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        val currentData = readData(context)
        val updatedData = currentData?.copy(
            gameSettings = saveData.gameSettings
        )

        if (updatedData != null) {
            saveData(context, updatedData)
        }
    }

    fun saveMusicVolume(context: Context, volume: Float) {
        val gameData = readData(context)
        val updatedGameData = gameData?.copy(
            gameSettings = gameData.gameSettings.copy(musicVolume = volume)
        )

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }
    }

    fun saveSoundVolume(context: Context, volume: Float) {
        val gameData = readData(context)
        val updatedGameData = gameData?.copy(
            gameSettings = gameData.gameSettings.copy(soundVolume = volume)
        )

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }
    }

    fun saveCurrentLevel(context: Context, level: Int) {
        val gameData = readData(context)
        val updatedGameData = gameData?.copy(
            playerInfo = gameData.playerInfo.copy(currentLevel = level)
        )

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }
    }

    fun saveAccessLevel(context: Context, currAccessLevel: Int) {
        val gameData = readData(context)
        val updatedGameData = gameData?.copy(
            playerInfo = gameData.playerInfo.copy(accessLevel = currAccessLevel)
        )

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }
    }

    fun savePuzzleUsedHintsCount(context: Context, level: Int, puzzle: String, hintUsed: Int) {
        val gameData = readData(context)
        val updatedGameData = gameData?.copy()
        updatedGameData?.levels?.find { it.id == level }?.puzzles?.find { it.name == puzzle }?.hintsUsed = hintUsed

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }
    }

    fun savePuzzleData(
        context: Context,
        level: Int,
        puzzle: String,
        status: String = "completed",
        attempts: Int = 1,
        timeSpent: Int = 1,
        hintsUsed: Int = 1
    ) {
        val gameData = readData(context)

        val updatedPuzzle = gameData?.levels?.find { it.id == level }?.puzzles?.find { it.name == puzzle }?.copy(
                status = status,
                attempts = attempts,
                timeSpent = timeSpent,
                hintsUsed = hintsUsed
            )

        val puzzleId =
            gameData?.levels?.find { it.id == level }?.puzzles?.indexOf(gameData.levels.find { it.id == level }?.puzzles?.find { it.name == puzzle })

        val updatedPuzzles = gameData?.levels?.find { it.id == level }?.puzzles?.toMutableList().apply {
            if (updatedPuzzle != null && puzzleId != null) {
                this?.set(puzzleId, updatedPuzzle)
            }
        }

        val updatedLevel = updatedPuzzles?.let {
            gameData?.levels?.find { it.id == level }?.copy(
                puzzles = it
            )
        }

        val levelId = gameData?.levels?.indexOf(gameData.levels.find { it.id == level })

        val updatedLevels = gameData?.levels?.toMutableList().apply {
            if (updatedLevel != null && levelId != null) {
                this?.set(levelId, updatedLevel)
            }
        }

        val updatedGameData = updatedLevels?.let {
            gameData?.copy(
                levels = it
            )
        }

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }

    }

    fun saveLevelProgress(activity: Activity, level: Int) {
        val gameData = readData(activity)
        val updatedGameData = gameData?.copy()
        if (updatedGameData?.levels?.find { it.id == level }?.puzzles?.size == LoadManager.getLevelProgress(
                activity,
                0
            )
        ) {
            updatedGameData.levels.find { it.id == level }?.isCompleted = true
        }

        if (updatedGameData != null) {
            saveData(activity, updatedGameData)
        }
    }

    fun saveCurrentDialog(activity: Activity, level: Int, npc: Int, dialogIndex: Int) {
        val gameData = readData(activity)
        val updatedGameData = gameData?.copy()
        if (updatedGameData != null) {
            updatedGameData.levels.find { it.id == level }?.npcDialogs?.find { it.id == npc }?.currentDialogIndex = dialogIndex
            saveData(activity, updatedGameData)
        }
    }

    data class SaveData(
        val playerInfo: PlayerInfo,
        val levels: List<LevelData>,
        val gameSettings: GameSettings
    )

}
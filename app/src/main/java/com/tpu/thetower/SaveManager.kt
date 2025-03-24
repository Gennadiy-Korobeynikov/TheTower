package com.tpu.thetower

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

    fun savePuzzleUsedHintsCount(context: Context, level : Int, puzzle : Int, hintUsed : Int) {
        val gameData = readData(context)
        val updatedGameData = gameData?.copy()
        updatedGameData?.levels?.get(level)?.puzzles?.get(puzzle)?.hintsUsed = hintUsed

        if (updatedGameData != null) {
            saveData(context, updatedGameData)
        }
    }

    data class SaveData(
        val playerInfo: PlayerInfo,
        val levels: List<LevelData>,
        val gameSettings: GameSettings
    )

}
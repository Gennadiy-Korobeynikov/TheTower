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

    data class SaveData(
        val playerInfo: PlayerInfo,
        val levels: List<LevelData>,
        val gameSettings: GameSettings
    )

}
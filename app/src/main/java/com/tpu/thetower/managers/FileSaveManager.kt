package com.tpu.thetower.managers

import android.content.Context
import com.google.gson.Gson
import com.tpu.thetower.models.SaveData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileSaveManager @Inject constructor(
    @ApplicationContext private val appContext: Context
) {
    private val gson = Gson()
    private val ioLock = Any()

    private companion object {
        private const val SAVE_FILE_NAME = "save_file.json"
    }

    private fun getSaveFile() = File(appContext.filesDir, SAVE_FILE_NAME)

    fun readData(): SaveData? = synchronized(ioLock) { readDataLocked() }

    fun ensureSaveExists() = synchronized(ioLock) { ensureSaveExistsLocked() }

    fun update(transform: (SaveData) -> SaveData): SaveData? {
        synchronized(ioLock) {
            val current = readDataLocked() ?: return null
            val updated = transform(current)
            saveDataLocked(updated)
            return updated
        }
    }

    fun resetData() {
        synchronized(ioLock) {
            val previous = readDataLocked() ?: return

            val file = getSaveFile()
            if (file.exists()) file.delete()

            ensureSaveExistsLocked()

            val currentData = readDataLocked()
            val updatedData = currentData?.copy(gameSettings = previous.gameSettings)
            if (updatedData != null) saveDataLocked(updatedData)
        }
    }


    private fun ensureSaveExistsLocked() {
        val file = getSaveFile()
        if (file.exists()) return

        appContext.assets.open(SAVE_FILE_NAME).use { input ->
            file.outputStream().use { output -> input.copyTo(output) }
        }
    }

    private fun readDataLocked(): SaveData? {
        ensureSaveExistsLocked()
        val file = getSaveFile()
        if (!file.exists()) return null

        FileReader(file).use { reader ->
            return gson.fromJson(reader, SaveData::class.java)
        }
    }

    private fun saveDataLocked(saveData: SaveData) {
        val file = getSaveFile()
        val tmp = File(appContext.filesDir, "$SAVE_FILE_NAME.tmp")

        FileWriter(tmp).use { writer -> gson.toJson(saveData, writer) }

        if (file.exists() && !file.delete()) {
            throw IllegalStateException("Cannot replace existing save file: ${file.absolutePath}")
        }
        if (!tmp.renameTo(file)) {
            throw IllegalStateException("Cannot move temp save file into place: ${tmp.absolutePath}")
        }
    }
}
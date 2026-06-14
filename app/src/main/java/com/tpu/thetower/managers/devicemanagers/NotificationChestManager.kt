package com.tpu.thetower.managers.devicemanagers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput
import com.tpu.thetower.fragments.lvl5.Lvl5PuzzleChestFragment
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.puzzles.Lv5PuzzleChest
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

class ChestCodeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val chestManager = EntryPointAccessors.fromApplication(
            context.applicationContext,
            ChestManagerEntryPoint::class.java
        ).chestManager()

        val code = RemoteInput
            .getResultsFromIntent(intent)
            ?.getCharSequence(Lvl5PuzzleChestFragment.KEY_CHEST_CODE)
            ?.toString() ?: return

        chestManager.tryOpen(code)
    }
}

@Singleton
class ChestManager @Inject constructor(
    private val saveRepo: SaveRepository,
    private val soundManager: SoundManager
) {
    private val puzzle = Lv5PuzzleChest(5, "chest")
    private val _chestState = MutableStateFlow(false)
    val chestState: StateFlow<Boolean> = _chestState.asStateFlow()


    fun resetForNewGame() {
        _chestState.value = false
    }

    fun tryOpen(code: String) {
        if (puzzle.checkSolution(saveRepo = saveRepo, solution = code)) {
            _chestState.value = true
        }
        else {
            soundManager.playSound(SoundEffect.VACUUM_BUMPING)
        }
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ChestManagerEntryPoint {
    fun chestManager(): ChestManager
}
package com.tpu.thetower.managers

import android.app.Activity
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.tpu.thetower.AppPreferences
import com.tpu.thetower.utils.SoundEffect
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HintManager @AssistedInject constructor(
    @Assisted private val hints: List<String>,
    @Assisted private val level: Int,
    @Assisted private val puzzle: String,
    private val saveRepo: SaveRepository,
    private val loadManager: LoadManager,
    private val dialogManager: DialogManager,
    private val soundManager: SoundManager
) {
    companion object {
        private var isNewHintAvailable = true
        private var timer: CountDownTimer? = null
        private var lastPuzzleName = ""

        private fun startHintRecovery(activity: Activity, hintManager: HintManager) {
            timer?.cancel()
            isNewHintAvailable = false

            val totalTimeToRecover =  if (AppPreferences(activity).isDevMode) 1_000L else 20_000L
            val updateInterval = totalTimeToRecover / 5

            timer = object : CountDownTimer(totalTimeToRecover, updateInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    // Добавлено + 1, чтобы первый шаг анимации начинался сразу с 1, а не с 0.
                    val step = ((totalTimeToRecover - millisUntilFinished) / updateInterval).toInt() + 1

                    (activity as? AppCompatActivity)?.supportFragmentManager
                        ?.setFragmentResult("hintImgUpdating", bundleOf("step" to step))
                }

                override fun onFinish() {

                    (activity as? AppCompatActivity)?.supportFragmentManager
                        ?.setFragmentResult("hintImgUpdating", bundleOf("step" to 0))

                    isNewHintAvailable = true
                    hintManager.usedHintsCountIncrease()
                }
            }.start()
        }

        // Для пропуска ожидания с помощью потенциальной рекламы
        fun cancelRecovery() {
            timer?.cancel()
            isNewHintAvailable = true
        }
    }

    fun useHint(activity: Activity) {

        val usedHintsCount = loadManager.getPuzzleUsedHintsCount(level, puzzle)
        soundManager.playSound(SoundEffect.HINT_CLICK)

        // Если в предыдущий раз подсказка была вызвана на этом фрагменте или это не первая подсказка
        if (lastPuzzleName == puzzle || usedHintsCount>0) {

            // Последняя подсказка - только показываем
            if (usedHintsCount == hints.count()) {
                dialogManager.startDialog(activity, hints[usedHintsCount - 1])
                return
            }

            // Показываем текущую подсказку, если она новая или ещё не восстановилась старая на этом же фрагменте
            if (lastPuzzleName == puzzle || isNewHintAvailable) {
                dialogManager.startDialog(activity, hints[usedHintsCount])
            }

            // Показываем предыдущую подсказку
            else
                dialogManager.startDialog(activity, hints[usedHintsCount - 1])

            if (isNewHintAvailable) {
                soundManager.playSound(SoundEffect.HINT_RESTORED)
                startHintRecovery(activity, this)
                lastPuzzleName = puzzle
            }
        } else if (isNewHintAvailable) {
            soundManager.playSound(SoundEffect.HINT_RESTORED)
            dialogManager.startDialog(activity, hints[usedHintsCount])
            startHintRecovery(activity, this)
            lastPuzzleName = puzzle
        }

    }

    private fun usedHintsCountIncrease() {
        val usedHintsCount = loadManager.getPuzzleUsedHintsCount(level, puzzle)
        if (usedHintsCount < hints.count()) {
            saveRepo.savePuzzleUsedHintsCount(level, puzzle, usedHintsCount + 1)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            hints: List<String>,
            level: Int,
            puzzle: String
        ): HintManager
    }
}
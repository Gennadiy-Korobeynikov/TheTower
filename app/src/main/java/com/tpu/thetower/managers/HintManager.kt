package com.tpu.thetower.managers

import android.app.Activity
import android.os.CountDownTimer
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HintManager @AssistedInject constructor(
    @Assisted private val hints: List<String>,
    @Assisted private val level: Int,
    @Assisted private val puzzle: String,
    private val saveRepo: SaveRepository,
    private val loadManager: LoadManager,
    private val dialogManager: DialogManager
) {
    companion object {
        private var isNewHintAvailable = true
        private var timer: CountDownTimer? = null
        private var lastPuzzleName = ""

        private fun startHintRecovery(activity: Activity, hintManager: HintManager, usedHintsCount: Int) {
            timer?.cancel()
            isNewHintAvailable = false

            val totalTimeToRecover =  if (AppPreferences(activity).isDevMode) 1_000L else 20_000L
            val updateInterval = totalTimeToRecover / 6

                timer = object : CountDownTimer(totalTimeToRecover, updateInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    ImageUpdateDispatcher.updateHintStateImg(
                        activity,
                        ((totalTimeToRecover - millisUntilFinished) / updateInterval).toInt()
                    )
                }

                override fun onFinish() {
                    ImageUpdateDispatcher.updateHintStateImg(activity, 0)
                    isNewHintAvailable = true
                    if (usedHintsCount < hintManager.hints.count())
                        hintManager.usedHintsCountIncrease(activity)
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

        // Если в предыдущий раз подсказка была вызвана на этом фрагменте или это не первая подсказка
        if (lastPuzzleName == puzzle || usedHintsCount>0) {

            // Последняя подсказка - только показываем
            if (usedHintsCount == hints.count()) {
                dialogManager.startDialog(activity, hints[usedHintsCount - 1])
                return
            }

            // Показываем текущую подсказку, если она новая или ещё не восстановилась старая на этом же фрагменте
            if (lastPuzzleName == puzzle || isNewHintAvailable)
                dialogManager.startDialog(activity, hints[usedHintsCount])

            // Показываем предыдущую подсказку
            else
                dialogManager.startDialog(activity, hints[usedHintsCount - 1])

            if (isNewHintAvailable) {
                startHintRecovery(activity, this, usedHintsCount)
                lastPuzzleName = puzzle
            }
        } else if (isNewHintAvailable) {
            dialogManager.startDialog(activity, hints[usedHintsCount])
            startHintRecovery(activity, this, usedHintsCount)
            lastPuzzleName = puzzle
        }
        // else звук или ещё что-то, типа "подсказка не готова" TODO
    }

    fun usedHintsCountIncrease(activity: Activity) {
        val usedHintsCount = loadManager.getPuzzleUsedHintsCount( level, puzzle) + 1
        saveRepo.savePuzzleUsedHintsCount(level, puzzle, usedHintsCount)
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
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
        private var isNewHintAvaliable = true
        private var timer: CountDownTimer? = null
        private val totalTimeToRecover = 20_000L // Пока 10 сек для теста
        private val updateInterval = totalTimeToRecover / 6 // Пока 2 сек
        private var lastPuzzleName = ""

        private fun startHintRecovery(activity: Activity, hintManager: HintManager, usedHintsCount: Int) {
            timer?.cancel()
            isNewHintAvaliable = false

            timer = object : CountDownTimer(totalTimeToRecover, updateInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    ImageUpdateDispatcher.updateHintStateImg(
                        activity,
                        ((totalTimeToRecover - millisUntilFinished) / updateInterval).toInt()
                    )
                }

                override fun onFinish() {
                    ImageUpdateDispatcher.updateHintStateImg(activity, 0)
                    isNewHintAvaliable = true
                    if (usedHintsCount < hintManager.hints.count())
                        hintManager.usedHintsCountIncrease(activity)
                }
            }.start()
        }


        // Для пропуска ожидания с помощью потенциальной рекламы
        fun cancelRecovery() {
            timer?.cancel()
            isNewHintAvaliable = true
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
            if (lastPuzzleName == puzzle || isNewHintAvaliable)
                dialogManager.startDialog(activity, hints[usedHintsCount])

            // Показываем предыдущую подсказку
            else
                dialogManager.startDialog(activity, hints[usedHintsCount - 1])

            if (isNewHintAvaliable) {
                startHintRecovery(activity, this, usedHintsCount)
                lastPuzzleName = puzzle
            }
        } else if (isNewHintAvaliable) {
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
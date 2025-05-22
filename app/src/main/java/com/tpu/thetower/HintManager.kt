package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import androidx.fragment.app.Fragment

class HintManager(
    private val hints: List<String>,
    private var usedHintsCount : Int,
    private val level : Int,
    private val puzzle : String,
) {
    private val saveManager = SaveManager.getInstance()
    companion object {
        private var isNewHintAvaliable = true
        private var timer: CountDownTimer? = null
        private val totalTimeToRecover = 10_000L  // Пока 10 сек для теста
        private val updateInterval = 2_000L  // Пока 2 сек
        private var lastPuzzletName  = ""

        private fun startHintRecovery(activity: Activity, hintManager: HintManager) {
            timer?.cancel()
            isNewHintAvaliable = false

            timer = object : CountDownTimer(totalTimeToRecover, updateInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    FragmentManager.updateHintStateImg(
                        activity,
                        ((totalTimeToRecover - millisUntilFinished) / updateInterval).toString()
                    )
                }

                override fun onFinish() {
                    FragmentManager.updateHintStateImg(activity, "Подсказка готова")
                    isNewHintAvaliable = true
                    if (hintManager.usedHintsCount < hintManager.hints.count())
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
        usedHintsCount = LoadManager.getPuzzleUsedHintsCount(activity, level, puzzle)

        // Если в предыдущий раз подсказка была вызвана на этом фрагменте или это не первая подсказка
        if (lastPuzzletName == puzzle || usedHintsCount>0) {

            // Последняя подсказка - только показываем
            if (usedHintsCount == hints.count()) {
                DialogManager.startDialog(activity, hints[usedHintsCount - 1])
                return
            }

            // Показываем текущую подсказку, если она новая или ещё не восстановилась старая на этом же фрагменте
            if (lastPuzzletName == puzzle || isNewHintAvaliable)
                DialogManager.startDialog(activity, hints[usedHintsCount])

            // Показываем предыдущую подсказку
            else
                DialogManager.startDialog(activity, hints[usedHintsCount-1])

            if (isNewHintAvaliable) {
                startHintRecovery(activity, this)
                lastPuzzletName = puzzle
            }

        }
        else if (isNewHintAvaliable) {
            DialogManager.startDialog(activity, hints[usedHintsCount])
            startHintRecovery(activity, this)
            lastPuzzletName = puzzle
        }
        // else звук или ещё что-то, типа "подсказка не готова" TODO
    }

    fun usedHintsCountIncrease(activity: Activity) {
        usedHintsCount = LoadManager.getPuzzleUsedHintsCount(activity, level, puzzle) + 1
        saveManager.savePuzzleUsedHintsCount(activity, level, puzzle, usedHintsCount)
    }

}
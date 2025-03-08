package com.tpu.thetower

import android.app.Activity
import android.os.CountDownTimer
import androidx.fragment.app.Fragment

class HintManager(
    private val hints: List<String>,
) {

    private var usedHintCount = 0
    private var timer: CountDownTimer? = null
    private val totalTimeToRecover = 10_000L  // Пока 10 сек для теста
    private val updateInterval = 2_000L  // Пока 2 сек
    private var isNewHintAvaliable = true

    fun useHint(fragment: Fragment) {
        DialogManager.startDialog(fragment.requireActivity(),hints[usedHintCount])
        if (isNewHintAvaliable && usedHintCount < hints.count() - 1)
            startHintRecovery(fragment)
    }

    private fun startHintRecovery(fragment: Fragment) {
        timer?.cancel()
        isNewHintAvaliable = false

        timer = object : CountDownTimer(totalTimeToRecover, updateInterval) {
            override fun onTick(millisUntilFinished: Long) {
                FragmentManager.updateHintStateImg( fragment,((totalTimeToRecover - millisUntilFinished) / updateInterval).toString() ) // 1..6
            }

            override fun onFinish() {
                FragmentManager.updateHintStateImg( fragment,"Подсказка готова" )
                isNewHintAvaliable = true
                usedHintCount++
            }
        }.start()
    }


    // Для пропуска ожидания с помощью потенциальной рекламы
    fun cancelRecovery() {
        timer?.cancel()
        isNewHintAvaliable = true
    }
}
package com.tpu.thetower

import androidx.fragment.app.Fragment
import com.tpu.thetower.fragments.ElevatorFragment

class LevelAccessManager {

    companion object {
        private val saveManager = SaveManager.getInstance()
        private val cardImageIds: List<Int> = listOf(
            R.drawable.card, // Костыль, не трогать
            R.drawable.card,
            R.drawable.test_card1,
            R.drawable.test_card2,
            R.drawable.test_card3,
            R.drawable.test_card4,
        )

        var currentAccessLvl = 0
        val topUnlockedLvlsForModules: List<Int> = listOf(0, 2, 3, 5)

        fun getCardImage(): Int {
             return cardImageIds[currentAccessLvl]
        }

        //TODO Разобраться в необходимости кода

        fun unlockModules(fragment: Fragment) {
//            FragmentManager.changeAccessCardImg(fragment, getCardImage())
//            FragmentManager.changeUnlockedModules(fragment, currentAccessLvl)
        }

        fun upgradeAccessLvl(fragment: Fragment) {

            if (currentAccessLvl in 0..< topUnlockedLvlsForModules.size - 1) currentAccessLvl++
            unlockModules(fragment)
            saveManager.saveAccessLevel(fragment.requireContext(), currentAccessLvl)
        }
    }
}
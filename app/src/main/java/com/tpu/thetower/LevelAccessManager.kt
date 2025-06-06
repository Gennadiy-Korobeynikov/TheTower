package com.tpu.thetower

import androidx.fragment.app.Fragment
import com.tpu.thetower.fragments.ElevatorFragment

class LevelAccessManager {

    companion object {
        private val saveManager = SaveManager.getInstance()
        private val cardImageIds: List<Int> = listOf(
            R.drawable.access_card_2, // Костыль, не трогать
            R.drawable.access_card_2,
            R.drawable.access_card_3,
            R.drawable.access_card_4,
            R.drawable.access_card_5,
            R.drawable.access_card_6
        )

        var currentAccessLvl = 0
        val topUnlockedLvlsForModules: List<Int> = listOf(6, 2, 3, 5, 6)

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
package com.tpu.thetower

import androidx.fragment.app.Fragment
import com.tpu.thetower.fragments.ElevatorFragment

class LevelAccessManager {

    companion object {
        private val saveManager = SaveManager.getInstance()
        private val cardImageIds : List<Int> = listOf(
            R.drawable.card,
            R.drawable.test_card1,
            R.drawable.test_card2,
            R.drawable.test_card3,
            R.drawable.test_card4,
        )

        var currentAccessLvl = 0

        val topUnlockedLvlsForModules : List<Int> = listOf(0,2,4)


        fun unlockModules( fragment: Fragment) {
            FragmentManager.changeAccessCardImg( fragment, cardImageIds[currentAccessLvl])
            FragmentManager.changeUnlockedModules(fragment, currentAccessLvl)
        }



        fun upgradeAccessLvl(fragment: Fragment) {

            currentAccessLvl++
            unlockModules(fragment)
            saveManager.saveAccessLevel(fragment.requireContext() , currentAccessLvl)
        }



    }
}
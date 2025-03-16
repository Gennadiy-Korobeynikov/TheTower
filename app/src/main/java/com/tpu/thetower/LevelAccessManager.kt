package com.tpu.thetower

import androidx.fragment.app.Fragment
import com.tpu.thetower.fragments.ElevatorFragment

class LevelAccessManager {

    companion object {
        private val saveManager = SaveManager.getInstance()
        private var currentAccessLvl = 0
        private val cardImageIds : List<Int> = listOf(
            R.drawable.card,
            R.drawable.test_card1,
            R.drawable.test_card2,
            R.drawable.test_card3,
            R.drawable.test_card4,
        )

        val modules : Map<Int, List<Int>> = mapOf(
            // module num to levels
//            1 to listOf(0,1,2),
//            2 to listOf(3,4,5,6,7),
//            3 to listOf(8,9,10)
            // Примерный вид в будущем

            // Пока для тестирования (каждый уровень доступа открывает один этаж)
            1 to listOf(0),
            2 to listOf(1),
            3 to listOf(2),
        )


        var lastUnlockedModule = 0


        fun unlockModule( fragment: Fragment , moduleNum : Int) {
            lastUnlockedModule = moduleNum
            FragmentManager.changeUnlockedModules(fragment,moduleNum)
        }



        fun upgradeAccessLvl(fragment: Fragment) {
            currentAccessLvl++
            FragmentManager.changeAccessCardImg( fragment, cardImageIds[currentAccessLvl])
            unlockModule( fragment, currentAccessLvl)
            saveManager.saveUnlockedModule(fragment.requireContext() ,lastUnlockedModule)
        }



    }
}
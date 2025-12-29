package com.tpu.thetower.managers

import androidx.fragment.app.Fragment
import com.tpu.thetower.R

class LevelAccessManager {

    companion object {
        private val cardImageIds: List<Int> = listOf(
            R.drawable.access_card_2,
            R.drawable.access_card_3,
            R.drawable.access_card_4,
            R.drawable.access_card_5,
            R.drawable.access_card_6
        )

        fun getCardImage(cardNumber : Int): Int {
            return cardImageIds[cardNumber-2] // карты начинаются со 2 уровня
        }

        fun changeAccessCardNumber(saveRepo: SaveRepository, newCardNumber: Int) {
            saveRepo.saveAccessCardNumber(newCardNumber)
        }

        fun updateAccessLvl(saveRepo: SaveRepository, newAccessLvl: Int) : Int {
            saveRepo.saveAccessLevel(newAccessLvl)
            return newAccessLvl
        }
    }
}
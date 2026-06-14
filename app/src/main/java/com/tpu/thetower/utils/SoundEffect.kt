package com.tpu.thetower.utils

import androidx.annotation.RawRes
import com.tpu.thetower.R

enum class SoundEffect(@RawRes val resId: Int) {
    // Common
    ELEVATOR_DOOR(R.raw.sound_of_an_elevator_door_opening),
    STEPS(R.raw.sound_of_steps),
    BUTTON_PRESS(R.raw.sound_of_button_press),
    ACCESS_CARD_INSERT(R.raw.sound_of_access_card_insert),
    DRAWER_CLOSING(R.raw.sound_of_drawer_closing),
    DRAWER_OPENING(R.raw.sound_of_drawer_opening),
    SEGMENTS_ROTATING(R.raw.sound_of_segments_rotating_on_the_safe_lock),
    LOCK_OPENING(R.raw.sound_of_the_lock_opening),

    // Hints
    HINT_CLICK(R.raw.sound_of_hint_click),
    HINT_USED(R.raw.sound_of_hint_used),
    HINT_RESTORED(R.raw.sound_of_hint_restored),

    // 0 Lvl
    FLASHLIGHT(R.raw.sound_of_a_flashlight),
    LIGHT_SWITCH(R.raw.sound_of_light_switch),

    // 1 Lvl
    LUSTRE(R.raw.sound_of_lustre),

    // 2 Lvl
    KEYBOARD_BUTTON_PRESS_1(R.raw.sound_of_keyboard_button_press_1),
    KEYBOARD_BUTTON_PRESS_2(R.raw.sound_of_keyboard_button_press_2),
    KEYBOARD_BUTTON_PRESS_3(R.raw.sound_of_keyboard_button_press_3),
    KEYBOARD_BUTTON_PRESS_4(R.raw.sound_of_keyboard_button_press_4),
    MOUSE_CLICK(R.raw.sound_of_mouse_click),

    // 3 Lvl
    DONUTS_SHAKING(R.raw.sound_of_donuts_shaking),
    GUARD_SNORING(R.raw.sound_of_guard_snoring),
    VACUUM_BUMPING(R.raw.sound_of_vacuum_cleaner_bumping),
    VACUUM_DRIVING_LEFT(R.raw.sound_of_vacuum_cleaner_driving_left),
    VACUUM_DRIVING_RIGHT(R.raw.sound_of_vacuum_cleaner_driving_right),
    VACUUM_DRIVING_STRAIGHT(R.raw.sound_of_vacuum_cleaner_driving_straight),
    WRONG_KEY(R.raw.sound_of_wrong_key),
    PILLS_DROP(R.raw.sound_of_pills_drop),

    // 4 Lvl
    CROW(R.raw.sound_of_crow),
    BUTTON_CHESS(R.raw.sound_of_button_chess),

    PAPER(R.raw.sound_of_paper),

    //5 Lvl
    CHEST_OPENING(R.raw.sound_of_chest_opening),
    FISH(R.raw.sound_of_fish),
    HORNS(R.raw.sound_of_horns),

    // 6 Lvl
    CHAIN_RELEASE(R.raw.sound_of_chain_release),
    DEATH(R.raw.sound_of_death),

    // Final
    JUDGE(R.raw.sound_of_judge),
    TRANSITION(R.raw.transition)


}
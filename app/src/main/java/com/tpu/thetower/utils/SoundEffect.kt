package com.tpu.thetower.utils

import androidx.annotation.RawRes
import com.tpu.thetower.R

enum class SoundEffect(@RawRes val resId: Int) {
    FLASHLIGHT(R.raw.sound_of_a_flashlight),
    ELEVATOR_DOOR(R.raw.sound_of_an_elevator_door_opening),
    BUTTON_PRESS(R.raw.sound_of_button_press),
    CHAIN_RELEASE(R.raw.sound_of_chain_release),
    DONUTS_SHAKING(R.raw.sound_of_donuts_shaking),
    DRAWER_CLOSING(R.raw.sound_of_drawer_closing),
    DRAWER_OPENING(R.raw.sound_of_drawer_opening),
    GUARD_SNORING(R.raw.sound_of_guard_snoring),
    KEYBOARD_BUTTON_PRESS_1(R.raw.sound_of_keyboard_button_press_1),
    KEYBOARD_BUTTON_PRESS_2(R.raw.sound_of_keyboard_button_press_2),
    KEYBOARD_BUTTON_PRESS_3(R.raw.sound_of_keyboard_button_press_3),
    KEYBOARD_BUTTON_PRESS_4(R.raw.sound_of_keyboard_button_press_4),
    LIGHT_SWITCH(R.raw.sound_of_light_switch),
    SEGMENTS_ROTATING(R.raw.sound_of_segments_rotating_on_the_safe_lock),
    LOCK_OPENING(R.raw.sound_of_the_lock_opening),
    VACUUM_BUMPING(R.raw.sound_of_vacuum_cleaner_bumping),
    VACUUM_DRIVING_LEFT(R.raw.sound_of_vacuum_cleaner_driving_left),
    VACUUM_DRIVING_RIGHT(R.raw.sound_of_vacuum_cleaner_driving_right),
    VACUUM_DRIVING_STRAIGHT(R.raw.sound_of_vacuum_cleaner_driving_straight),
}
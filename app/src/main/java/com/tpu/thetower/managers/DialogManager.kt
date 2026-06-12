package com.tpu.thetower.managers

import android.app.Activity
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.tpu.thetower.R
import com.tpu.thetower.fragments.common.DialogFragment
import com.tpu.thetower.models.Character
import com.tpu.thetower.models.Dialog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DialogManager @Inject constructor(
    private val saveRepo: SaveRepository,
    private val loadManager: LoadManager
) {
    private val characters: Map<String, Character> = mapOf(
        "John" to Character("Джон", R.drawable.john_default),
        "John_thinking" to Character("Джон", R.drawable.john_thinking),
        "John_angry" to Character("Джон", R.drawable.john_angry),
        "receptionist" to Character("Администратор", R.drawable.npc_avatar_receptionist),
        "security" to Character("Охранник Пит", R.drawable.npc_avatar_security),
        "fisher" to Character("Рыбак Боб", R.drawable.npc_avatar_fisher),
        "judge" to Character("Судья", R.drawable.npc_avatar_judge),
        "doctor" to Character("Врач", R.drawable.npc_avatar_doctor)
    )

    private data class DialogSpec(
        @StringRes val lines: List<Int>,
        val speakers: List<String>,
        val onFinished: (Activity) -> Unit = {}
    )

    private val dialogSpecs: Map<String, DialogSpec> = mapOf(
        // ==================== Lvl0 ====================

        "lvl0_start" to DialogSpec(
            lines = listOf(R.string.lvl0_start),
            speakers = listOf("John"),
            // окно разрешений
//            onFinished = { act ->
//                UiVisibilityController.show(act, UiVisibilityController.UiContainer.PERMISSION_REQUEST)
//            }
        ),

        "lvl0_dark" to DialogSpec(
            lines = listOf(R.string.lvl0_dark),
            speakers = listOf("John")
        ),

        "lvl0_flashlight_on" to DialogSpec(
            lines = listOf(R.string.lvl0_flashlight_on),
            speakers = listOf("John_thinking")
        ),

        "lvl0_after_light" to DialogSpec(
            lines = listOf(
                R.string.lvl0_after_light_1,
                R.string.lvl0_after_light_2,
                R.string.lvl0_after_light_3,
                R.string.lvl0_after_light_4
            ),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl0_click_not_elevator" to DialogSpec(
            lines = listOf(R.string.lvl0_click_not_elevator),
            speakers = listOf("John_thinking")
        ),

        "lvl0_elevator_other_floor_first" to DialogSpec(
            lines = listOf(
                R.string.lvl0_elevator_other_floor_first_1,
                R.string.lvl0_elevator_other_floor_first_2,
                R.string.lvl0_elevator_other_floor_first_3,
                R.string.lvl0_elevator_other_floor_first_4,
                R.string.lvl0_elevator_other_floor_first_5,
                R.string.lvl0_elevator_other_floor_first_6,
                R.string.lvl0_elevator_other_floor_first_7,
                R.string.lvl0_elevator_other_floor_first_8,
            ),
            speakers = listOf(
                "John_thinking",
                "John_thinking",
                "John_thinking",
                "John",
                "John_angry",
                "John_thinking",
                "John_thinking",
                "John_angry",
            ),
            onFinished = { nextDialog(0, "elevator") }
        ),

        "lvl0_box" to DialogSpec(
            lines = listOf(R.string.lvl0_box),
            speakers = listOf("John_thinking"),
            onFinished = { nextDialog(0, "shapes_paper") }
        ),

        "lvl0_safe" to DialogSpec(
            lines = listOf(R.string.lvl0_safe_1, R.string.lvl0_safe_2),
            speakers = listOf("John_thinking", "John_thinking"),
            onFinished = { nextDialog(0, "safe") }
        ),

        "lvl0_box_opened" to DialogSpec(
            lines = listOf(
                R.string.lvl0_box_opened_1,
                R.string.lvl0_box_opened_2,
                R.string.lvl0_box_opened_3,
                R.string.lvl0_box_opened_4,
                R.string.lvl0_box_opened_5
            ),
            speakers = listOf("John", "John_thinking", "John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl0_access_card_inspect" to DialogSpec(
            lines = listOf(R.string.lvl0_access_card_inspect_1, R.string.lvl0_access_card_inspect_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        // ==================== Общие/подсказки ====================

        "no_hints" to DialogSpec(
            lines = listOf(R.string.no_hints),
            speakers = listOf("John_thinking")
        ),

        "hint_is_not_here" to DialogSpec(
            lines = listOf(R.string.hint_is_not_here),
            speakers = listOf("John_thinking")
        ),

        "lvl0_click_switch" to DialogSpec(
            lines = listOf(R.string.lvl0_click_switch),
            speakers = listOf("John")
        ),

        "lvl0_puzzle0_hint1" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle0_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl0_puzzle0_hint2" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle0_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl0_to_puzzle1_hint" to DialogSpec(
            lines = listOf(R.string.lvl0_to_puzzle1_hint),
            speakers = listOf("John_thinking")
        ),

        "lvl0_puzzle1_hint1" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl0_puzzle1_hint2" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl0_puzzle1_hint3" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl0_puzzle1_hint4" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint4_1, R.string.lvl0_puzzle1_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        // ==================== Lvl1 ====================

        "lvl1_elevator" to DialogSpec(
            lines = listOf(R.string.lvl1_elevator),
            speakers = listOf("John_thinking"),
        ),

        "lvl1_start" to DialogSpec(
            lines = listOf(
                R.string.lvl1_start_1,
                R.string.lvl1_start_2,
                R.string.lvl1_start_3,
                R.string.lvl1_start_4
            ),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking", "John_thinking"),
            onFinished = { nextDialog(1, "start") }
        ),

        // (замена черновика lvl1_npc_receptionist1..12 -> новый диалог lvl1_receptionist_*)
        "lvl1_receptionist" to DialogSpec(
            lines = listOf(
                R.string.lvl1_receptionist_1,
                R.string.lvl1_receptionist_2,
                R.string.lvl1_receptionist_3,
                R.string.lvl1_receptionist_4,
                R.string.lvl1_receptionist_5,
                R.string.lvl1_receptionist_6,
                R.string.lvl1_receptionist_7,
                R.string.lvl1_receptionist_8,
                R.string.lvl1_receptionist_9,
                R.string.lvl1_receptionist_10,
                R.string.lvl1_receptionist_11,
                R.string.lvl1_receptionist_12,
                R.string.lvl1_receptionist_13,
                R.string.lvl1_receptionist_14,
                R.string.lvl1_receptionist_15,
                R.string.lvl1_receptionist_16,
                R.string.lvl1_receptionist_17,
                R.string.lvl1_receptionist_18,
                R.string.lvl1_receptionist_19,
                R.string.lvl1_receptionist_20,
                R.string.lvl1_receptionist_21,
                R.string.lvl1_receptionist_22,
                R.string.lvl1_receptionist_23,
                R.string.lvl1_receptionist_24,
                R.string.lvl1_receptionist_25,
                R.string.lvl1_receptionist_26,
                R.string.lvl1_receptionist_27,
                R.string.lvl1_receptionist_28,
                R.string.lvl1_receptionist_29,
                R.string.lvl1_receptionist_30,
                R.string.lvl1_receptionist_31,
                R.string.lvl1_receptionist_32,
                R.string.lvl1_receptionist_33,
                R.string.lvl1_receptionist_34,
                R.string.lvl1_receptionist_35,
                R.string.lvl1_receptionist_36,
                R.string.lvl1_receptionist_37,
                R.string.lvl1_receptionist_38,
                R.string.lvl1_receptionist_39,
                R.string.lvl1_receptionist_40,
                R.string.lvl1_receptionist_41,
                R.string.lvl1_receptionist_42,
                R.string.lvl1_receptionist_43,
                R.string.lvl1_receptionist_44,
                R.string.lvl1_receptionist_45,
                R.string.lvl1_receptionist_46,
                R.string.lvl1_receptionist_47,
                R.string.lvl1_receptionist_48,
            ),
            speakers = listOf(
                "receptionist",  // 1
                "John",          // 2 (вслух)
                "receptionist",  // 3
                "receptionist",  // 4
                "John",          // 5
                "John_thinking", // 6 (ремарка в скобках)
                "John",          // 7
                "receptionist",  // 8
                "receptionist",  // 9
                "John",          // 10
                "receptionist",  // 11
                "receptionist",  // 12
                "receptionist",  // 13
                "receptionist",  // 14
                "receptionist",  // 15
                "receptionist",  // 16
                "receptionist",  // 17
                "receptionist",  // 18
                "receptionist",  // 19
                "receptionist",  // 20
                "John",          // 21
                "John",          // 22
                "receptionist",  // 23
                "receptionist",  // 24
                "receptionist",  // 25
                "receptionist",  // 26
                "John",          // 27
                "John",          // 28
                "John",          // 29
                "receptionist",  // 30
                "receptionist",  // 31
                "receptionist",  // 32
                "receptionist",  // 33
                "John",          // 34
                "John_thinking", // 35 (ремарка в скобках)
                "John",          // 36
                "John",          // 37
                "receptionist",  // 38
                "receptionist",  // 39
                "John_thinking", // 40 (сарказм/про себя)
                "John",          // 41
                "John",          // 42
                "receptionist",  // 43
                "receptionist",  // 44
                "receptionist",  // 45
                "receptionist",  // 46
                "receptionist",  // 47
                "John",          // 48
            ),
            onFinished = {
                nextDialog(1, "receptionist")
                loadManager.changeAccessCardNumber(2)
            }
        ),

        "lvl1_npc_receptionist_repeat" to DialogSpec(
            lines = listOf(R.string.lvl1_npc_receptionist_repeat_1, R.string.lvl1_npc_receptionist_repeat_2),
            speakers = listOf("receptionist", "John")
        ),

        "lvl1_after_clicks" to DialogSpec(
            lines = listOf(R.string.lvl1_after_clicks),
            speakers = listOf("John")
        ),

        // ==================== Lvl2 (новые реплики) ====================

        "lvl2_jane_absent" to DialogSpec(
            lines = listOf(R.string.lvl2_jane_absent),
            speakers = listOf("John_thinking"),
            onFinished = { nextDialog(2, "start") }
        ),

        "lvl2_lunch" to DialogSpec(
            lines = listOf(R.string.lvl2_lunch_1),
            speakers = listOf("John_thinking"),
            onFinished = { nextDialog(2, "lunch") }
        ),

        "lvl2_computer" to DialogSpec(
            lines = listOf(R.string.lvl2_computer_1, R.string.lvl2_computer_2),
            speakers = listOf("John_thinking", "John_thinking"),
            onFinished = { nextDialog(2, "computer") }
        ),

        "lvl2_card_chain" to DialogSpec(
            lines = listOf(R.string.lvl2_card_chain_1, R.string.lvl2_card_chain_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        // --- Lvl2 hints (как было, строки существуют) ---
        "lvl2_puzzle0_hint" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle0_hint),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle1_hint1" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle1_hint2" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle1_hint3" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle1_hint4" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint4_1, R.string.lvl2_puzzle1_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl2_puzzle1_hint5" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint5),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle2_hint1" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle2_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle2_hint2" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle2_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle2_hint3" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle2_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle3_hint1" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle3_hint2" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle3_hint3" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle3_hint4" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint4_1, R.string.lvl2_puzzle3_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl2_puzzle3_hint5" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint5),
            speakers = listOf("John_thinking")
        ),

        "lvl2_puzzle3_hint6" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint6),
            speakers = listOf("John_thinking")
        ),

        // ==================== Lvl3 ====================

        "lvl3_ventilation" to DialogSpec(
            lines = listOf(R.string.lvl3_ventilation),
            speakers = listOf("John_thinking")
        ),

        "lvl3_security" to DialogSpec(
            lines = listOf(
                R.string.lvl3_security_1,
                R.string.lvl3_security_2,
                R.string.lvl3_security_3,
                R.string.lvl3_security_4,
                R.string.lvl3_security_5,
                R.string.lvl3_security_6,
                R.string.lvl3_security_7,
                R.string.lvl3_security_8,
                R.string.lvl3_security_9,
                R.string.lvl3_security_10,
                R.string.lvl3_security_11,
                R.string.lvl3_security_12,
                R.string.lvl3_security_13,
                R.string.lvl3_security_14,
                R.string.lvl3_security_15,
            ),
            speakers = listOf(
                "security",      // 1
                "security",      // 2
                "John_thinking", // 3
                "John",          // 4
                "security",      // 5
                "John",          // 6
                "John",          // 7
                "security",      // 8
                "security",      // 9
                "John",          // 10
                "John",          // 11
                "security",      // 12
                "security",      // 13
                "security",      // 14
                "security",      // 15
            )
        ),

        "lvl3_security_next" to DialogSpec(
            lines = listOf(R.string.lvl3_security_next),
            speakers = listOf("security")
        ),

        "lvl3_donuts" to DialogSpec(
            lines = listOf(R.string.lvl3_donuts_1, R.string.lvl3_donuts_2),
            speakers = listOf("security", "security")
        ),

        "lvl3_npc_security_final_lock" to DialogSpec(
            lines = listOf(R.string.lvl3_npc_security_final_lock),
            speakers = listOf("security")
        ),

        "lvl3_computer" to DialogSpec(
            lines = listOf(R.string.lvl3_computer),
            speakers = listOf("security")
        ),

        "lvl3_need_key" to DialogSpec(
            lines = listOf(R.string.lvl3_need_key),
            speakers = listOf("John")
        ),

        "lvl3_need_to_use_key" to DialogSpec(
            lines = listOf(R.string.lvl3_need_to_use_key),
            speakers = listOf("John_thinking")
        ),

        "lvl3_wrong_key" to DialogSpec(
            lines = listOf(R.string.lvl3_wrong_key),
            speakers = listOf("John_thinking")
        ),

        "lvl3_to_puzzle0_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_to_puzzle0_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl3_to_puzzle0_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_to_puzzle0_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle0_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint1_1, R.string.lvl3_puzzle0_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle0_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint2_1, R.string.lvl3_puzzle0_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle0_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle0_hint4" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint4),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle0_hint5" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint5),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle0_hint6" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint6_1, R.string.lvl3_puzzle0_hint6_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle0_hint7" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint7_1, R.string.lvl3_puzzle0_hint7_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle1_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle1_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle1_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle1_hint2_1, R.string.lvl3_puzzle1_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_to_coffee_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_to_coffee_hint1_1, R.string.lvl3_to_coffee_hint1_2, R.string.lvl3_to_coffee_hint1_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl3_puzzle2_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle2_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle2_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle2_hint4" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint4),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle3_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint1_1, R.string.lvl3_puzzle3_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle3_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle3_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle3_hint4" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint4_1, R.string.lvl3_puzzle3_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle3_hint5" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint5_1, R.string.lvl3_puzzle3_hint5_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle3_hint6" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint6_1, R.string.lvl3_puzzle3_hint6_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle4_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle4_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl3_puzzle4_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle4_hint2_1, R.string.lvl3_puzzle4_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl3_puzzle4_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle4_hint3_1, R.string.lvl3_puzzle4_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        // ==================== Lvl4 ====================

        "lvl4_start" to DialogSpec(
            lines = listOf(R.string.lvl4_start_1, R.string.lvl4_start_2, R.string.lvl4_start_3),
            speakers = listOf("John", "John", "John_thinking"),
            onFinished = { nextDialog(4, "start") }
        ),

        "lvl4_chess_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_chess_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl4_chess_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl4_chess_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint4),
            speakers = listOf("John_thinking")
        ),

        "lvl4_timeline_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_timeline_hint1_1, R.string.lvl4_timeline_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_timeline_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_timeline_hint2_1, R.string.lvl4_timeline_hint2_2,
                R.string.lvl4_timeline_hint2_3, R.string.lvl4_timeline_hint2_4),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl4_askiiBtn_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_askiiBtn_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_askiiBtn_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_askiiBtn_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl4_sequencepaper_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_sequencepaper_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_sequencepaper_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_sequencepaper_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl4_sequencepaper_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_sequencepaper_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_askii_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_askii_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_babel_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_babel_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint2_1, R.string.lvl4_book_babel_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_book_babel_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_babel_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint4_1, R.string.lvl4_book_babel_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_book_babel_hint5" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint5_1, R.string.lvl4_book_babel_hint5_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_book_qr_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint1_1, R.string.lvl4_book_qr_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_book_qr_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint2_1, R.string.lvl4_book_qr_hint2_2, R.string.lvl4_book_qr_hint2_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl4_book_qr_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint3_1, R.string.lvl4_book_qr_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_book_qr_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint4_1, R.string.lvl4_book_qr_hint4_2, R.string.lvl4_book_qr_hint4_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl4_book_blur_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_blur_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_blur_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_blur_hint2),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_blur_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_blur_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_history_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint1),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_history_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint2_1, R.string.lvl4_book_history_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_book_history_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint3_1, R.string.lvl4_book_history_hint3_2, R.string.lvl4_book_history_hint3_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        ),

        "lvl4_book_history_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint4),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_history_hint5" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint5),
            speakers = listOf("John_thinking")
        ),

        "lvl4_book_history_hint6" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint6_1, R.string.lvl4_book_history_hint6_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl4_puzzle1_askii" to DialogSpec(
            lines = listOf(R.string.lvl4_puzzle1_askii),
            speakers = listOf("John_thinking")
        ),

        "lvl4_puzzle1_normal" to DialogSpec(
            lines = listOf(R.string.lvl4_puzzle1_normal),
            speakers = listOf("John_thinking")
        ),

        // ==================== Lvl5 ====================

        "lvl5_start" to DialogSpec(
            lines = listOf(R.string.lvl5_start_1, R.string.lvl5_start_2, R.string.lvl5_start_3),
            speakers = listOf("John", "John_thinking", "John"),
            onFinished = { nextDialog(5, "start") }
        ),

        "lvl5_npc_fisher" to DialogSpec(
            lines = listOf(
                R.string.lvl5_npc_fisher_1,
                R.string.lvl5_npc_fisher_2,
                R.string.lvl5_npc_fisher_3,
                R.string.lvl5_npc_fisher_4,
                R.string.lvl5_npc_fisher_5,
                R.string.lvl5_npc_fisher_6,
                R.string.lvl5_npc_fisher_7
            ),
            speakers = listOf(
                "John",   // 1
                "fisher", // 2
                "John",   // 3
                "fisher", // 4
                "fisher", // 5
                "John",   // 6
                "fisher", // 7
            ),
            onFinished = { nextDialog(5, "npc_fisher") }
        ),

        "lvl5_npc_fisher_return" to DialogSpec(
            lines = listOf(
                R.string.lvl5_npc_fisher_return_1,
                R.string.lvl5_npc_fisher_return_2,
                R.string.lvl5_npc_fisher_return_3
            ),
            speakers = listOf("John", "fisher", "John")
        ),

        "lvl5_npc_fisher_reward" to DialogSpec(
            lines = listOf(
                R.string.lvl5_npc_fisher_reward_1,
                R.string.lvl5_npc_fisher_reward_2,
                R.string.lvl5_npc_fisher_reward_3,
                R.string.lvl5_npc_fisher_reward_4,
                R.string.lvl5_npc_fisher_reward_5,
                R.string.lvl5_npc_fisher_reward_6,
                R.string.lvl5_npc_fisher_reward_7,
                R.string.lvl5_npc_fisher_reward_8,
                R.string.lvl5_npc_fisher_reward_9,
                R.string.lvl5_npc_fisher_reward_10,
                R.string.lvl5_npc_fisher_reward_11,
                R.string.lvl5_npc_fisher_reward_12,
                R.string.lvl5_npc_fisher_reward_13,
                R.string.lvl5_npc_fisher_reward_14,
                R.string.lvl5_npc_fisher_reward_15,
                R.string.lvl5_npc_fisher_reward_16,
            ),
            speakers = listOf(
                "fisher", "fisher", "fisher", "fisher",
                "fisher", "fisher", "fisher", "fisher",
                "fisher", "fisher", "fisher", "fisher",
                "fisher", "fisher", "fisher", "fisher",
            ),
            onFinished = { nextDialog(5, "npc_fisher_reward") }
        ),

        "lvl5_npc_fisher_reward_repeat" to DialogSpec(
            lines = listOf(
                R.string.lvl5_npc_fisher_reward_repeat,
                R.string.lvl5_npc_fisher_reward_7,
                R.string.lvl5_npc_fisher_reward_8,
                R.string.lvl5_npc_fisher_reward_9,
                R.string.lvl5_npc_fisher_reward_10,
                R.string.lvl5_npc_fisher_reward_11,
                R.string.lvl5_npc_fisher_reward_12,
                R.string.lvl5_npc_fisher_reward_13,
                R.string.lvl5_npc_fisher_reward_14,
                R.string.lvl5_npc_fisher_reward_15,
                R.string.lvl5_npc_fisher_reward_16,
            ),
            speakers = listOf(
                "fisher", "fisher", "fisher", "fisher",
                "fisher", "fisher", "fisher", "fisher",
                "fisher", "fisher", "fisher"
            )
        ),

        "lvl5_chest" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_1, R.string.lvl5_chest_2, R.string.lvl5_chest_3),
            speakers = listOf("John", "John", "John_thinking"),
            onFinished = { nextDialog(5, "chest") }
        ),

        "lvl5_chest_opened" to DialogSpec(
            lines = listOf(
                R.string.lvl5_chest_opened_1,
                R.string.lvl5_chest_opened_2,
                R.string.lvl5_chest_opened_3,
                R.string.lvl5_chest_opened_4,
                R.string.lvl5_chest_opened_5
            ),
            speakers = listOf("John_thinking", "John", "John_thinking", "John", "John_thinking")
        ),

        "lvl5_card_floor6" to DialogSpec(
            lines = listOf(R.string.lvl5_card_floor6),
            speakers = listOf("John")
        ),

        // --- Lvl5 hints ---
        "lvl5_bluetoothOff_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOff_hint1),
            speakers = listOf("John_thinking")
        ),
        "lvl5_bluetoothOff_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOff_hint2),
            speakers = listOf("John_thinking")
        ),
        "lvl5_bluetoothOff_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOff_hint3),
            speakers = listOf("John_thinking")
        ),

        "lvl5_bluetoothOn_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOn_hint1_1, R.string.lvl5_bluetoothOn_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),
        "lvl5_bluetoothOn_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOn_hint2_1, R.string.lvl5_bluetoothOn_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl5_horns_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_horns_hint1_1, R.string.lvl5_horns_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),
        "lvl5_horns_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_horns_hint2_1, R.string.lvl5_horns_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),
        "lvl5_horns_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_horns_hint3_1, R.string.lvl5_horns_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl5_fish_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_fish_hint1_1, R.string.lvl5_fish_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),
        "lvl5_fish_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_fish_hint2),
            speakers = listOf("John_thinking")
        ),
        "lvl5_fish_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_fish_hint3_1, R.string.lvl5_fish_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl5_map_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_map_hint1),
            speakers = listOf("John_thinking")
        ),
        "lvl5_map_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_map_hint2_1, R.string.lvl5_map_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),
        "lvl5_map_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_map_hint3_1, R.string.lvl5_map_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl5_general_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint1),
            speakers = listOf("John_thinking")
        ),
        "lvl5_general_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint2),
            speakers = listOf("John_thinking")
        ),
        "lvl5_general_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint3),
            speakers = listOf("John_thinking")
        ),
        "lvl5_general_hint4" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint4_1, R.string.lvl5_general_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl5_chest_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint1),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint2),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint3),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint4" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint4),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint5" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint5),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint6" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint6),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint7" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint7),
            speakers = listOf("John_thinking")
        ),
        "lvl5_chest_hint8" to DialogSpec(
            lines = listOf(R.string.lvl5_chest_hint8),
            speakers = listOf("John_thinking")
        ),

        // ==================== Lvl6 ====================
        "lvl6_start" to DialogSpec(
            lines = listOf(
                R.string.lvl6_start_1,
                R.string.lvl6_start_2,
                R.string.lvl6_start_3,
                R.string.lvl6_start_4,
                R.string.lvl6_start_5,
                R.string.lvl6_start_6,
                R.string.lvl6_start_7,
                R.string.lvl6_start_8
            ),
            speakers = listOf(
                "John",
                "John",
                "John",
                "John",
                "John_angry",
                "John_angry",
                "John",
                "John"
            ),
            onFinished = { nextDialog(6, "start") }
        ),

        "lvl6_lock_hint1" to DialogSpec(
            lines = listOf(R.string.lvl6_lock_hint1_1, R.string.lvl6_lock_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        ),

        "lvl6_lock_hint2" to DialogSpec(
            lines = listOf(
                R.string.lvl6_lock_hint2_1,
                R.string.lvl6_lock_hint2_2,
                R.string.lvl6_lock_hint2_3
            ),
            speakers = listOf(
                "John_thinking",
                "John_thinking",
                "John_thinking"
            )
        ),

        "lvl6_lock_hint3" to DialogSpec(
            lines = listOf(
                R.string.lvl6_lock_hint3
            ),
            speakers = listOf(
                "John_thinking"
            )
        ),

        "lvl6_lock_hint4" to DialogSpec(
            lines = listOf(
                R.string.lvl6_lock_hint3,
                R.string.lvl6_lock_hint4_1,
                R.string.lvl6_lock_hint4_2
            ),
            speakers = listOf(
                "John_thinking",
                "John_thinking",
                "John_thinking"
            )
        ),

        "lvl6_lock_hint5" to DialogSpec(
            lines = listOf(
                R.string.lvl6_lock_hint3,
                R.string.lvl6_lock_hint4_1,
                R.string.lvl6_lock_hint4_2,
                R.string.lvl6_lock_hint5_1,
                R.string.lvl6_lock_hint5_2
            ),
            speakers = listOf(
                "John_thinking",
                "John_thinking",
                "John_thinking",
                "John_thinking",
                "John_thinking"
            )
        ),

        "lvl6_lock_hint6" to DialogSpec(
            lines = listOf(
                R.string.lvl6_lock_hint3,
                R.string.lvl6_lock_hint4_1,
                R.string.lvl6_lock_hint4_2,
                R.string.lvl6_lock_hint5_1,
                R.string.lvl6_lock_hint5_2,
                R.string.lvl6_lock_hint6,
                R.string.lvl6_lock_hint6_1,
                R.string.lvl6_lock_hint6_2,
                R.string.lvl6_lock_hint6_3,
                R.string.lvl6_lock_hint6_4,
                R.string.lvl6_lock_hint6_5
            ),
            speakers = List(11) { "John_thinking" }
        ),

        "lvl6_lock_hint7" to DialogSpec(
            lines = listOf(
                R.string.lvl6_lock_hint3,
                R.string.lvl6_lock_hint4_1,
                R.string.lvl6_lock_hint4_2,
                R.string.lvl6_lock_hint5_1,
                R.string.lvl6_lock_hint5_2,
                R.string.lvl6_lock_hint6,
                R.string.lvl6_lock_hint6_1,
                R.string.lvl6_lock_hint6_2,
                R.string.lvl6_lock_hint6_3,
                R.string.lvl6_lock_hint6_4,
                R.string.lvl6_lock_hint6_5,
                R.string.lvl6_lock_hint7
            ),
            speakers = List(12) { "John_thinking" }
        ),

        // ==================== Final ====================

        "final_court" to DialogSpec(
            lines = listOf(
                R.string.final_court_1, R.string.final_court_2, R.string.final_court_3,
                R.string.final_court_4, R.string.final_court_5, R.string.final_court_6,
                R.string.final_court_7, R.string.final_court_8, R.string.final_court_9,
                R.string.final_court_10, R.string.final_court_11, R.string.final_court_12,
                R.string.final_court_13, R.string.final_court_14, R.string.final_court_15,
                R.string.final_court_16, R.string.final_court_17
            ),
            speakers = List(17) { "judge" }
        ),

        "final_court_2" to DialogSpec(
            lines = listOf(R.string.final_court_18),
            speakers = listOf("judge")
        ),

        "final_court_3" to DialogSpec(
            lines = listOf(R.string.final_court_19),
            speakers = listOf("judge")
        ),

        "final_hospital" to DialogSpec(
            lines = listOf(
                R.string.final_hospital_1,
                R.string.final_hospital_2,
                R.string.final_hospital_3,
                R.string.final_hospital_4,
                R.string.final_hospital_5,
                R.string.final_hospital_6,
                R.string.final_hospital_7,
                R.string.final_hospital_8,
                R.string.final_hospital_9,
                R.string.final_hospital_10,
                R.string.final_hospital_11,
                R.string.final_hospital_12,
                R.string.final_hospital_13
            ),
            speakers = listOf(
                "doctor",
                "doctor",
                "doctor",
                "doctor",
                "doctor",
                "John",
                "doctor",
                "doctor",
                "John",
                "doctor",
                "John_angry",
                "doctor",
                "John_angry"
            )
        )
    )

    internal fun buildDialog(activity: Activity, dialogKey: String): Dialog {
        val spec = dialogSpecs[dialogKey]
            ?: error("Unknown dialogKey: $dialogKey")

        require(spec.lines.size == spec.speakers.size)
            { "DialogSpec lines and speakers must have same size for key=$dialogKey" }
        require(spec.speakers.all { it in characters })
            { "DialogSpec has unknown speaker key for dialogKey=$dialogKey" }

        return Dialog(
            spec.lines.map(activity::getString),
            spec.speakers.map { key -> characters.getValue(key) }
        ).apply {
            onDialogEnd = { spec.onFinished(activity) }
        }
    }

    fun startDialog(activity: Activity, dialogKey: String) {
        startDialog(activity, dialogKey, onFinished = null)
    }


    fun startDialog(
        activity: Activity,
        dialogKey: String,
        onFinished: (() -> Unit)?
    ) {
        val dialogFragment = DialogFragment.newInstance(dialogKey)
        (activity as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fcv_dialog, dialogFragment, "DialogFragment")
            ?.commitNow()

        val f = (activity as? AppCompatActivity)
            ?.supportFragmentManager
            ?.findFragmentByTag("DialogFragment") as? DialogFragment

        if (onFinished != null && f != null) {
            val dialog = buildDialog(activity, dialogKey)
            val base = dialog.onDialogEnd
            dialog.onDialogEnd = {
                base?.invoke()
                onFinished.invoke()
            }
            f.setDialogOverride(dialog)
        }

        UiVisibilityController.show(activity, UiVisibilityController.UiContainer.DIALOG)
    }

    private fun nextDialog(level: Int, key: String) {
        var currentDialog = loadManager.getCurrentDialogIndex(level, key)
        currentDialog++
        saveRepo.saveCurrentDialogIndex(level, key, currentDialog)
    }
}

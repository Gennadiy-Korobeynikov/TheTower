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
        "receptionist" to Character("Администратор", R.drawable.npc_avatar_receptionist),
        "security" to Character("Охранник Пит", R.drawable.npc_avatar_security)
    )

    private data class DialogSpec(
        @StringRes val lines: List<Int>,
        val speakers: List<String>,
        val onFinished: (Activity) -> Unit = {}
    )

    private val dialogSpecs: Map<String, DialogSpec> = mapOf(

        "lvl0_start" to DialogSpec(
            lines = listOf(R.string.lvl0_start),
            speakers = listOf("John"),
            // окно разрешений
            onFinished = { act ->
                UiVisibilityController.show(act, UiVisibilityController.UiContainer.PERMISSION_REQUEST)
            }
        ),

        "lvl0_dark" to DialogSpec(
            lines = listOf(R.string.lvl0_dark),
            speakers = listOf("John")
        ),

        "lvl0_flashlight_on" to DialogSpec(
            lines = listOf(R.string.lvl0_flashlight_on),
            speakers = listOf("John_thinking")
        ),

        "lvl0_light_on" to DialogSpec(
            lines = listOf(R.string.lvl0_light_on),
            speakers = listOf("John_thinking")
        ),

        "no_hints" to DialogSpec(
            lines = listOf(R.string.no_hints),
            speakers = listOf("John_thinking")
        ),
        "lvl0_click_switch" to DialogSpec(
            lines = listOf(R.string.lvl0_click_switch),
            speakers = listOf("John")
        )
        ,

        "lvl0_puzzle0_hint1" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle0_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_puzzle0_hint2" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle0_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_to_puzzle1_hint" to DialogSpec(
            lines = listOf(R.string.lvl0_to_puzzle1_hint),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_puzzle1_hint1" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_puzzle1_hint2" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_puzzle1_hint3" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1_hint3),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_puzzle1" to DialogSpec(
            lines = listOf(R.string.lvl0_puzzle1),
            speakers = listOf("John_thinking"),
            onFinished = { nextDialog(0, "shapes_paper") }
        )
        ,
        "lvl0_access_card" to DialogSpec(
            lines = listOf(R.string.lvl0_access_card),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_access_card_got" to DialogSpec(
            lines = listOf(R.string.lvl0_access_card_got),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl0_access_card" to DialogSpec(
            lines = listOf(R.string.lvl0_access_card),
            speakers = listOf("John")
        )
        ,
        "lvl0_access_card_got" to DialogSpec(
            lines = listOf(R.string.lvl0_access_card_got),
            speakers = listOf("John")
        )
        ,
        "lvl1_elevator" to DialogSpec(
            lines = listOf(R.string.lvl1_elevator),
            speakers = listOf("John_thinking"),
        ),
        "lvl1_npc_receptionist" to DialogSpec(
            lines = listOf(
                R.string.lvl1_npc_receptionist1,
                R.string.lvl1_npc_receptionist2,
                R.string.lvl1_npc_receptionist3,
                R.string.lvl1_npc_receptionist4,
                R.string.lvl1_npc_receptionist5,
                R.string.lvl1_npc_receptionist6,
                R.string.lvl1_npc_receptionist7,
                R.string.lvl1_npc_receptionist8,
                R.string.lvl1_npc_receptionist9,
                R.string.lvl1_npc_receptionist10,
                R.string.lvl1_npc_receptionist11,
                R.string.lvl1_npc_receptionist12,
            ),
            speakers = listOf(
                "receptionist",
                "John_thinking",
                "receptionist",
                "John_thinking",
                "receptionist",
                "John_thinking",
                "receptionist",
                "receptionist",
                "John_thinking",
                "John_thinking",
                "receptionist",
                "John_thinking",
            ),
            onFinished = { nextDialog(1,"receptionist") }
        ),


        "lvl1_npc_receptionist_2" to DialogSpec(
            lines = listOf(
                R.string.lvl1_npc_receptionist_2_1,
                R.string.lvl1_npc_receptionist_2_2,

                ),
            speakers = listOf(
                "receptionist",
                "John_thinking",
            )
        ),


        "hint_is_not_here" to DialogSpec(
            lines = listOf(R.string.hint_is_not_here),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl2_start" to DialogSpec(
            lines = listOf(R.string.lvl2_start),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl2_computer_lore" to DialogSpec(
            lines = listOf(R.string.lvl2_computer_lore),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl2_puzzle0_hint" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle0_hint),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle1_hint1" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle1_hint2" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle1_hint3" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint3),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle1_hint4" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint4_1, R.string.lvl2_puzzle1_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl2_puzzle1_hint5" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle1_hint5),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl2_puzzle2_hint1" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle2_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle2_hint2" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle2_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle2_hint3" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle2_hint3),
            speakers = listOf("John_thinking")
        )
        ,



        "lvl2_puzzle3_hint1" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle3_hint2" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle3_hint3" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint3),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl2_puzzle3_hint4" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint4_1, R.string.lvl2_puzzle3_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl2_puzzle3_hint5" to DialogSpec(
            lines = listOf(R.string.lvl2_puzzle3_hint5),
            speakers = listOf("John_thinking")
        )
        ,



// Lvl 3 ---------------------------------------

        "lvl3_ventilation" to DialogSpec(
            lines = listOf(R.string.lvl3_ventilation),
            speakers = listOf("John_thinking")
        ),

        "lvl3_npc_security" to DialogSpec(
            lines = listOf(
                R.string.lvl3_npc_security1,
                R.string.lvl3_npc_security2,
                R.string.lvl3_npc_security3,
                R.string.lvl3_npc_security4,
                R.string.lvl3_npc_security5
            ),
            speakers = listOf(
                "John_thinking",
                "security",
                "John_thinking",
                "security",
                "John_thinking"
            )
        )
        ,

        "lvl3_npc_security_final_lock" to DialogSpec(
            lines = listOf(R.string.lvl3_npc_security_final_lock),
            speakers = listOf("security")
        )
        ,

        "lvl3_computer" to DialogSpec(
            lines = listOf(R.string.lvl3_computer),
            speakers = listOf("security")
        )
        ,


        "lvl3_donuts" to DialogSpec(
            lines = listOf(R.string.lvl3_donuts),
            speakers = listOf("security")
        )
        ,

        "lvl3_need_key" to DialogSpec(
            lines = listOf(R.string.lvl3_need_key),
            speakers = listOf("John")
        )
        ,

        "lvl3_need_to_use_key" to DialogSpec(
            lines = listOf(R.string.lvl3_need_to_use_key),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_to_puzzle0_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_to_puzzle0_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_to_puzzle0_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_to_puzzle0_hint2),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_puzzle0_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint1_1, R.string.lvl3_puzzle0_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl3_puzzle0_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint2_1, R.string.lvl3_puzzle0_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl3_puzzle0_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint3),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_puzzle0_hint4" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint4),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_puzzle0_hint5" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint5),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_puzzle0_hint6" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint6_1, R.string.lvl3_puzzle0_hint6_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl3_puzzle0_hint7" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle0_hint7_1, R.string.lvl3_puzzle0_hint7_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,




        "lvl3_puzzle1_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle1_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_puzzle1_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle1_hint2_1, R.string.lvl3_puzzle1_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,



        "lvl3_to_coffee_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_to_coffee_hint1_1, R.string.lvl3_to_coffee_hint1_2, R.string.lvl3_to_coffee_hint1_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        )
        ,



        "lvl3_puzzle2_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_puzzle2_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_puzzle2_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint3),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_puzzle2_hint4" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle2_hint4),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl3_puzzle3_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint1_1, R.string.lvl3_puzzle3_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl3_puzzle3_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_puzzle3_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint3),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_puzzle3_hint4" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint4_1, R.string.lvl3_puzzle3_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl3_puzzle3_hint5" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint5_1, R.string.lvl3_puzzle3_hint5_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl3_puzzle3_hint6" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle3_hint6_1, R.string.lvl3_puzzle3_hint6_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,





        "lvl3_puzzle4_hint1" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle4_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl3_puzzle4_hint2" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle4_hint2_1, R.string.lvl3_puzzle4_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl3_puzzle4_hint3" to DialogSpec(
            lines = listOf(R.string.lvl3_puzzle4_hint3_1, R.string.lvl3_puzzle4_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,




        "lvl4_chess_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_chess_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint2),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_chess_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint3),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_chess_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_chess_hint4),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_timeline_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_timeline_hint1_1, R.string.lvl4_timeline_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl4_timeline_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_timeline_hint2_1, R.string.lvl4_timeline_hint2_2, R.string.lvl4_timeline_hint2_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        )
        ,


        "lvl4_askiiBtn_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_askiiBtn_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_askiiBtn_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_askiiBtn_hint2),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_sequencepaper_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_sequencepaper_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_sequencepaper_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_sequencepaper_hint2),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_sequencepaper_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_sequencepaper_hint3),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_book_askii_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_askii_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_book_babel_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_book_babel_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint2_1, R.string.lvl4_book_babel_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_babel_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint3),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl4_book_babel_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint4_1, R.string.lvl4_book_babel_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_babel_hint5" to DialogSpec(
            lines = listOf(R.string.lvl4_book_babel_hint5_1, R.string.lvl4_book_babel_hint5_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl4_book_qr_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint1_1, R.string.lvl4_book_qr_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_qr_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint2_1, R.string.lvl4_book_qr_hint2_2, R.string.lvl4_book_qr_hint2_3),

            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_qr_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint3_1, R.string.lvl4_book_qr_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_qr_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_book_qr_hint4_1, R.string.lvl4_book_qr_hint4_2, R.string.lvl4_book_qr_hint4_3),

            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        )
        ,


        "lvl4_book_blur_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_blur_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl4_book_blur_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_blur_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl4_book_blur_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_blur_hint3),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_book_history_hint1" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl4_book_history_hint2" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint2_1, R.string.lvl4_book_history_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_history_hint3" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint3_1, R.string.lvl4_book_history_hint3_2, R.string.lvl4_book_history_hint3_3),
            speakers = listOf("John_thinking", "John_thinking", "John_thinking")
        )
        ,
        "lvl4_book_history_hint4" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint4),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl4_book_history_hint5" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint5),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl4_book_history_hint6" to DialogSpec(
            lines = listOf(R.string.lvl4_book_history_hint6_1, R.string.lvl4_book_history_hint6_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl5_moose_paper" to DialogSpec(
            lines = listOf(R.string.lvl5_moose_paper),
            speakers = listOf("John_thinking")
        )
        ,



        "lvl5_bluetoothOff_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOff_hint1),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl5_bluetoothOff_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOff_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl5_bluetoothOff_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOff_hint3),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl5_bluetoothOn_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOn_hint1_1, R.string.lvl5_bluetoothOn_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl5_bluetoothOn_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_bluetoothOn_hint2_1, R.string.lvl5_bluetoothOn_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl5_horns_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_horns_hint1_1, R.string.lvl5_horns_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl5_horns_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_horns_hint2_1, R.string.lvl5_horns_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl5_horns_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_horns_hint3_1, R.string.lvl5_horns_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl5_fish_rack_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_fish_hint1_1, R.string.lvl5_fish_hint1_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,
        "lvl5_fish_rack_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_fish_hint2),
            speakers = listOf("John_thinking")
        )
        ,
        "lvl5_fish_rack_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_fish_hint3_1, R.string.lvl5_fish_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,

        "lvl5_fisher_rack_completed" to DialogSpec(
            lines = listOf(R.string.lvl5_fisher_rack_completed_1, R.string.lvl5_fisher_rack_completed_2),
            speakers = listOf("John_thinking", "John_thinking"),
            onFinished = { nextDialog(5, "lvl5_fisher_rack_completed") }
        )
        ,


        "lvl5_map_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_map_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl5_map_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_map_hint2_1, R.string.lvl5_map_hint2_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl5_map_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_map_hint3_1, R.string.lvl5_map_hint3_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl5_general_hint1" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl5_general_hint2" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint2),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl5_general_hint3" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint3),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl5_general_hint4" to DialogSpec(
            lines = listOf(R.string.lvl5_general_hint4_1, R.string.lvl5_general_hint4_2),
            speakers = listOf("John_thinking", "John_thinking")
        )
        ,


        "lvl6_lock_hint1" to DialogSpec(
            lines = listOf(R.string.lvl6_lock_hint1),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_puzzle1_askii" to DialogSpec(
            lines = listOf(R.string.lvl4_puzzle1_askii),
            speakers = listOf("John_thinking")
        )
        ,


        "lvl4_puzzle1_normal" to DialogSpec(
            lines = listOf(R.string.lvl4_puzzle1_normal),
            speakers = listOf("John_thinking")
        )
        ,

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
        val dialogFragment = DialogFragment.newInstance(dialogKey)
        (activity as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fcv_dialog, dialogFragment, "DialogFragment")
            ?.commitNow()

        UiVisibilityController.show(activity, UiVisibilityController.UiContainer.DIALOG)
    }

    private fun nextDialog(level: Int, key: String) {
        var currentDialog = loadManager.getCurrentDialogIndex(level, key)
        currentDialog++
        saveRepo.saveCurrentDialogIndex(level, key, currentDialog)
    }
}
package com.tpu.thetower

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getString
import com.tpu.thetower.fragments.DialogFragment
import com.tpu.thetower.models.Character
import com.tpu.thetower.models.Dialog

class DialogManager {


    companion object {
        private lateinit var dialogs : Map<String, Dialog>
        private lateinit var characters : Map<String, Character>


        fun startDialog(activity: Activity , dialogKey : String, ) {
            val dialog = dialogs[dialogKey] ?: return

            val dialogFragment = DialogFragment(dialog)
            (activity as? AppCompatActivity)?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fcv_dialog, dialogFragment, "DialogFragment")
                ?.commitNow()
            FragmentManager.showDialog(activity)
        }


        fun loadDialogs(activity: Activity) {
            dialogs = mapOf(
                "lvl0_start" to
                        Dialog(
                            listOf( getString(activity , R.string.lvl0_start),),
                            listOfNotNull( characters["John"],)
                        )
                        // Запрос разрешений после окончания диалога
                        { FragmentManager.showPermissionRequestFragment(activity) },



                "lvl0_dark" to
                        Dialog(
                            listOf(getString(activity , R.string.lvl0_dark),),
                            listOfNotNull(characters["John"],))
                        {} ,


                "lvl0_flashlight_on" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_flashlight_on),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,


                "lvl0_light_on" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_light_on),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "no_hints" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.no_hints),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl0_puzzle0_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_puzzle0_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl0_puzzle0_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_puzzle0_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl0_to_puzzle1_hint" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_to_puzzle1_hint),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl0_puzzle1_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_puzzle1_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl0_puzzle1_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_puzzle1_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl0_puzzle1_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_puzzle1_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl0_puzzle1" to
                        Dialog(
                            listOf(
                                getString(activity, R.string.lvl0_puzzle1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {},

                "lvl1_npc_receptionist" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl1_npc_receptionist1),
                                getString(activity , R.string.lvl1_npc_receptionist2),
                                getString(activity , R.string.lvl1_npc_receptionist3),
                                getString(activity , R.string.lvl1_npc_receptionist4),
                                getString(activity , R.string.lvl1_npc_receptionist5),
                                getString(activity , R.string.lvl1_npc_receptionist6),
                                getString(activity , R.string.lvl1_npc_receptionist7),
                                getString(activity , R.string.lvl1_npc_receptionist8),
                                getString(activity , R.string.lvl1_npc_receptionist9),
                                getString(activity , R.string.lvl1_npc_receptionist10),
                                getString(activity , R.string.lvl1_npc_receptionist11),
                                getString(activity , R.string.lvl1_npc_receptionist12),

                            ),
                            listOfNotNull(
                                characters["receptionist"],
                                characters["John"],
                                characters["receptionist"],
                                characters["John"],
                                characters["receptionist"],
                                characters["John"],
                                characters["receptionist"],
                                characters["receptionist"],
                                characters["John"],
                                characters["John"],
                                characters["receptionist"],
                                characters["John"],
                            )
                        )
                        { nextDialog(activity, 1, 0)}  ,


                "lvl1_npc_receptionist_2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl1_npc_receptionist_2_1),
                                getString(activity , R.string.lvl1_npc_receptionist_2_2),

                                ),
                            listOfNotNull(
                                characters["receptionist"],
                                characters["John"],
                            )
                        )
                        {}  ,

                "hint_is_not_here" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.hint_is_not_here),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle0_hint" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle0_hint),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle1_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle1_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle1_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle1_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle1_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle1_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle1_hint4" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle1_hint4),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle2_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle2_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle2_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle2_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl2_puzzle2_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle2_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,



                "lvl2_puzzle3_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle3_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle3_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle3_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle3_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle3_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle3_hint4" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle3_hint4_1),
                                getString(activity , R.string.lvl2_puzzle3_hint4_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl2_puzzle3_hint5" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl2_puzzle3_hint5),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,



// Lvl 3 ---------------------------------------

                "lvl3_to_puzzle0_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_to_puzzle0_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_to_puzzle0_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_to_puzzle0_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle0_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint1_1),
                                getString(activity , R.string.lvl3_puzzle0_hint1_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle0_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint2_1),
                                getString(activity , R.string.lvl3_puzzle0_hint2_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle0_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle0_hint4" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint4),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle0_hint5" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint5),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle0_hint6" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint6_1),
                                getString(activity , R.string.lvl3_puzzle0_hint6_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle0_hint7" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle0_hint7_1),
                                getString(activity , R.string.lvl3_puzzle0_hint7_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,




                "lvl3_puzzle1_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle1_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle1_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle1_hint2_1),
                                getString(activity , R.string.lvl3_puzzle1_hint2_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,



                "lvl3_to_coffee_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_to_coffee_hint1_1),
                                getString(activity , R.string.lvl3_to_coffee_hint1_2),
                                getString(activity , R.string.lvl3_to_coffee_hint1_3),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,



                "lvl3_puzzle2_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle2_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle2_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle2_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle2_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle2_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle2_hint4" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle2_hint4),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,


                "lvl3_puzzle3_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle3_hint1_1),
                                getString(activity , R.string.lvl3_puzzle3_hint1_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle3_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle3_hint2),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle3_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle3_hint3),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl3_puzzle3_hint4" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle3_hint4_1),
                                getString(activity , R.string.lvl3_puzzle3_hint4_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle3_hint5" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle3_hint5_1),
                                getString(activity , R.string.lvl3_puzzle3_hint5_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle3_hint6" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle3_hint6_1),
                                getString(activity , R.string.lvl3_puzzle3_hint6_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,





                "lvl3_puzzle4_hint1" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle4_hint1),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle4_hint2" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle4_hint2_1),
                                getString(activity , R.string.lvl3_puzzle4_hint2_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,
                "lvl3_puzzle4_hint3" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl3_puzzle4_hint3_1),
                                getString(activity , R.string.lvl3_puzzle4_hint3_2),
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["John"],
                            )
                        )
                        {} ,















                "lvl4_puzzle1_askii" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl4_puzzle1_askii),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,

                "lvl4_puzzle1_normal" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl4_puzzle1_normal),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        {} ,



                )

        }


        fun loadCharacters() {
            characters = mapOf(
                "John" to Character("Джон", R.drawable.character_avatar_test),
                "receptionist" to Character("Администратор", R.drawable.npc_avatar_receptionist)
            )
        }

        private fun nextDialog(activity: Activity, level: Int, npc: Int) {
            var currentDialog = LoadManager.getCurrentDialog(activity, level, npc)
            val saveManager = SaveManager.getInstance()
            currentDialog++
            saveManager.saveCurrentDialog(activity, level, npc, currentDialog)
        }

    }
}





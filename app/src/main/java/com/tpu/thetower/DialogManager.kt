package com.tpu.thetower

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getString
import androidx.core.os.bundleOf
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
                        {/*Тут надо вызвать сохранение заврешения диалога*/}  ,


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

            )

        }


        fun loadCharacters() {
            characters = mapOf(
                "John" to Character("Джон", R.drawable.character_avatar_test),
                "receptionist" to Character("Администратор", R.drawable.npc_avatar_receptionist)
            )
        }

    }
}





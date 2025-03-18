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
                ?.commit()
            FragmentManager.showDialog(activity)
        }


        fun loadDialogs(activity: Activity) {
            dialogs = mapOf(
                "test" to
                        Dialog(
                            listOf(
                                "Помогите!",
                                "Сам думай, шизанутый долбаёб",
                                "Шла нахуй отседова, лампа ебаная!",
                                "Сосал?",
                                "[Съебалась в ужасе*]"
                            ),
                            listOfNotNull(
                                characters["John"],
                                characters["Bulb"],
                                characters["John"],
                                characters["Bulb"],
                                characters["Bulb"]
                            )
                        )
                        { Log.d("test", "test") },


                "lvl0_start" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_start),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
                        // Запрос разрешений после окончания диалога
                        { FragmentManager.showPermissionRequestFragment(activity) },



                "lvl0_dark" to
                        Dialog(
                            listOf(
                                getString(activity , R.string.lvl0_dark),
                            ),
                            listOfNotNull(
                                characters["John"],
                            )
                        )
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
            )

        }


        fun loadCharacters() {
            characters = mapOf(
                "John" to Character("Джон", R.drawable.character_avatar_test),
                "Bulb" to Character("Лампочка", R.drawable.hint)
            )
        }

    }
}





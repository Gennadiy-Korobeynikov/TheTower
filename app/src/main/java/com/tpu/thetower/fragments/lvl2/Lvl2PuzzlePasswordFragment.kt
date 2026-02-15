package com.tpu.thetower.fragments.lvl2

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.Puzzle
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl2PuzzlePasswordBinding
import com.tpu.thetower.managers.DialogManager
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.MusicManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.models.PuzzleStatus
import com.tpu.thetower.puzzles.Lvl2PuzzlePassword
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Lvl2PuzzlePasswordFragment : Fragment(R.layout.fragment_lvl2_puzzle_password), Hintable {

    private lateinit var binding: FragmentLvl2PuzzlePasswordBinding

    private val pinCells = mutableListOf<android.widget.TextView>()
    private lateinit var keyboardSounds: List<SoundEffect>
    private var currentSoundIndex = 0
    private lateinit var puzzle: Puzzle

    private lateinit var hintManager: HintManager

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var dialogManager: DialogManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var musicManager: MusicManager // оставлен, если используется/будет использоваться
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl2PuzzlePasswordBinding.bind(view)

        puzzle = Lvl2PuzzlePassword(2, "password")

        setListeners()

        keyboardSounds = listOf(
            SoundEffect.KEYBOARD_BUTTON_PRESS_1,
            SoundEffect.KEYBOARD_BUTTON_PRESS_2,
            SoundEffect.KEYBOARD_BUTTON_PRESS_3,
            SoundEffect.KEYBOARD_BUTTON_PRESS_4
        )

        pinCells.addAll(
            listOf(
                binding.tvPin1,
                binding.tvPin2,
                binding.tvPin3,
                binding.tvPin4,
                binding.tvPin5,
                binding.tvPin6,
                binding.tvPin7
            )
        )

        when (loadManager.getPuzzleStatus(2, "password")) {
            PuzzleStatus.LOCKED.value -> {
                showKeyboard()
                hintManager = hintManagerFactory.create(
                    hints = listOf("lvl2_puzzle2_hint1", "lvl2_puzzle2_hint2", "lvl2_puzzle2_hint3"),
                    level = 2,
                    puzzle = "password"
                )
            }

            PuzzleStatus.COMPLETED.value -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf(
                        "lvl2_puzzle3_hint1",
                        "lvl2_puzzle3_hint2",
                        "lvl2_puzzle3_hint3",
                        "lvl2_puzzle3_hint4",
                        "lvl2_puzzle3_hint5"
                    ),
                    level = 2,
                    puzzle = "chat"
                )
                completed()
            }

            else -> {
                hintManager = hintManagerFactory.create(
                    hints = listOf("hint_is_not_here"),
                    level = 2,
                    puzzle = "password"
                )
            }
        }
    }

    private fun setListeners() = with(binding) {
        btnToJames.setOnClickListener {
            clChatAmanda.visibility = View.GONE
            clChatJames.visibility = View.VISIBLE
            ivDialog.setImageResource(R.drawable.lvl2_puzzle1_chat1)
        }
        btnToAmanda.setOnClickListener {
            clChatAmanda.visibility = View.VISIBLE
            clChatJames.visibility = View.GONE
            ivDialog.setImageResource(R.drawable.lvl2_puzzle1_chat2)
        }
        pinContainer.setOnClickListener { showKeyboard() }

        hiddenInput.requestFocus()
        hiddenInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s?.toString() ?: ""
                pinCells.forEachIndexed { index, textView ->
                    textView.text = if (index < input.length) input[index].toString() else ""
                }

                if (puzzle.checkSolution(requireActivity(), saveRepo, input)) {
                    dialogManager.startDialog(requireActivity(), "lvl2_computer_lore")
                    completed()
                    hideKeyboard()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                soundManager.playSound(keyboardSounds[currentSoundIndex])
                currentSoundIndex = (currentSoundIndex + 1) % keyboardSounds.size
            }
        })
    }

    private fun showKeyboard() {
        binding.hiddenInput.apply {
            requestFocus()
            post {
                context?.let {
                    val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun completed() = with(binding) {
        // переключаем hintManager на чат после завершения пароля
        hintManager = hintManagerFactory.create(
            hints = listOf(
                "lvl2_puzzle3_hint1",
                "lvl2_puzzle3_hint2",
                "lvl2_puzzle3_hint3",
                "lvl2_puzzle3_hint4",
                "lvl2_puzzle3_hint5"
            ),
            level = 2,
            puzzle = "chat"
        )

        hiddenInput.visibility = View.GONE
        pinContainer.visibility = View.GONE
        tvPassword.visibility = View.GONE
        ivDialog.visibility = View.VISIBLE
        clChatNames.visibility = View.VISIBLE
        clChatJames.visibility = View.VISIBLE
    }


    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun useHint() {
        if (loadManager.getPuzzleStatus(2, "lock") == PuzzleStatus.COMPLETED.value) {
            hintManager.useHint(requireActivity())
        } else {
            dialogManager.startDialog(requireActivity(), "hint_is_not_here")
        }
    }

    override fun skipPuzzle() {
        puzzle.complete(saveRepo)
        completed()
    }
}
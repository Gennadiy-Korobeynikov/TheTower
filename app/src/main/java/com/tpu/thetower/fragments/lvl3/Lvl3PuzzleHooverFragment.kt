package com.tpu.thetower.fragments.lvl3

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.tpu.thetower.Hintable
import com.tpu.thetower.R
import com.tpu.thetower.databinding.FragmentLvl3PuzzleHooverBinding
import com.tpu.thetower.managers.FragmentNavigation
import com.tpu.thetower.managers.HintManager
import com.tpu.thetower.managers.LoadManager
import com.tpu.thetower.managers.SaveRepository
import com.tpu.thetower.managers.SoundManager
import com.tpu.thetower.puzzles.Direction
import com.tpu.thetower.puzzles.Lvl3PuzzleHoover
import com.tpu.thetower.utils.SoundEffect
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val ANIM_DURATION = 800L
const val ANIM_DURATION_UP = 2000L

@AndroidEntryPoint
class Lvl3PuzzleHooverFragment : Fragment(R.layout.fragment_lvl3_puzzle_hoover), Hintable {

    private var _binding: FragmentLvl3PuzzleHooverBinding? = null
    private val binding get() = _binding!!

    private val puzzleHoover = Lvl3PuzzleHoover(3, "vacuum cleaner")
    private lateinit var hintManager: HintManager

    @Inject lateinit var saveRepo: SaveRepository
    @Inject lateinit var loadManager: LoadManager
    @Inject lateinit var soundManager: SoundManager
    @Inject lateinit var hintManagerFactory: HintManager.Factory

    private var restart: Boolean = false
    private var onStartPosition: Boolean = true
    private var win: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLvl3PuzzleHooverBinding.bind(view)

        setListeners()
        test()

        hintManager = hintManagerFactory.create(
            hints = listOf("lvl3_puzzle2_hint1", "lvl3_puzzle2_hint2", "lvl3_puzzle2_hint3", "lvl3_puzzle2_hint4"),
            level = 3,
            puzzle = "vacuum cleaner"
        )
    }

    private fun rotateHooverAnim(clockwise: Boolean) {
        changeButtonsState(false)
        puzzleHoover.changeDirection(clockwise = clockwise)
        val value = if (clockwise) 90f else -90f
        binding.ivHoover.animate().rotationBy(value).setDuration(300).withEndAction {
            test()
            changeButtonsState(true)
        }.start()
    }

    private fun setListeners() {
        binding.btnLeft.setOnClickListener {
            rotateHooverAnim(false)
            soundManager.playSound(SoundEffect.VACUUM_DRIVING_LEFT)
        }
        binding.btnRight.setOnClickListener {
            rotateHooverAnim(true)
            soundManager.playSound(SoundEffect.VACUUM_DRIVING_RIGHT)
        }

        binding.btnForward.setOnClickListener {
            binding.tvRestart.text = ""
            binding.tvWin.text = ""
            changeButtonsState(false)

            if (onStartPosition) { //В начале
                soundManager.playSound(SoundEffect.VACUUM_DRIVING_STRAIGHT)
                moveHooverAnim(puzzleHoover.currDirection, binding.mainScreen)
            } else if (puzzleHoover.currPositionY == 12 && puzzleHoover.currPositionX == 6 && puzzleHoover.currDirection == Direction.Down) {
                // Вернулись назад (небольшой костыль, ни на что не влияет, просто тут уже дело времени, которого мало
                soundManager.playSound(SoundEffect.VACUUM_DRIVING_STRAIGHT)
                moveHooverToCenter(binding.mainScreen, back = true)
            } else { // Двигаемся внутри вентиляции
                binding.btnForward.postDelayed({
                    restart = !puzzleHoover.moveForward()
                    if (!restart) soundManager.playSound(SoundEffect.VACUUM_DRIVING_STRAIGHT)
                    win = puzzleHoover.checkSolution(requireActivity(), saveRepo)
                    test()
                    changeButtonsState(true)
                }, 1000)
            }
        }
    }

    private fun changeButtonsState(state: Boolean) {
        val alpha = if (state) 1f else 0.5f
        binding.btnLeft.isEnabled = state
        binding.btnRight.isEnabled = state
        binding.btnForward.isEnabled = state
        binding.btnLeft.alpha = alpha
        binding.btnRight.alpha = alpha
        binding.btnForward.alpha = alpha
    }

    private fun moveHooverAnim(direction: Direction, mainLayout: ConstraintLayout) {
        val constraintSet = ConstraintSet().apply { clone(mainLayout) }

        // Сброс всех ограничений для iv_hoover
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.START)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.END)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.BOTTOM)

        when (direction) {
            Direction.Left -> {
                // Привязываем к  (слева)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.START, R.id.guideV_anim_left, ConstraintSet.START)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            Direction.Right -> {
                // Привязываем к  (справа)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.END, R.id.guideV_anim_right, ConstraintSet.END)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }
            Direction.Up -> {
                // Привязываем к (выход за экран вверх)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.TOP, R.id.guideH_anim_up, ConstraintSet.TOP)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            }
            Direction.Down -> {
                // Привязываем к (снизу)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.BOTTOM, R.id.guideH_anim_down, ConstraintSet.BOTTOM)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(R.id.iv_hoover, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            }
        }

        val transition = ChangeBounds().apply {
            duration = if (direction == Direction.Up) ANIM_DURATION_UP else ANIM_DURATION
            addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition?) {
                    transition?.removeListener(this)
                    if (puzzleHoover.currDirection != Direction.Up) {
                        // Возврат на исходную позицию (центр)
                        binding.ivHoover.postDelayed({ moveHooverToCenter(mainLayout) }, 750)
                    } else {
                        onStartPosition = false
                        puzzleHoover.moveForward()
                        //                    restart = puzzleHoover.isWall()
//                    win = puzzleHoover.checkSolution(requireContext())
                        test()
                        changeButtonsState(true)
                    }
                }
                override fun onTransitionStart(transition: Transition?) {}
                override fun onTransitionCancel(transition: Transition?) {}
                override fun onTransitionPause(transition: Transition?) {}
                override fun onTransitionResume(transition: Transition?) {}
            })
        }

        TransitionManager.beginDelayedTransition(mainLayout, transition)
        constraintSet.applyTo(mainLayout)
    }

    private fun moveHooverToCenter(mainLayout: ConstraintLayout, back: Boolean = false) {
        val constraintSet = ConstraintSet().apply { clone(mainLayout) }
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.START)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.END)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.BOTTOM)
        // Центрируем по всем сторонам
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        val transition = ChangeBounds().apply {
            duration = if (back) ANIM_DURATION_UP else ANIM_DURATION
            addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition?) {
                    transition?.removeListener(this)
                    test()
                    onStartPosition = true
                    changeButtonsState(true)
                }
                override fun onTransitionStart(transition: Transition?) {}
                override fun onTransitionCancel(transition: Transition?) {}
                override fun onTransitionPause(transition: Transition?) {}
                override fun onTransitionResume(transition: Transition?) {}
            })
        }

        TransitionManager.beginDelayedTransition(mainLayout, transition)
        constraintSet.applyTo(mainLayout)
    }

// Временно для тестирования
    private fun test() {
        if (restart) {
            soundManager.playSound(SoundEffect.VACUUM_BUMPING)
            binding.ivHoover.animate().rotation(0f).setDuration(300).start()
            moveHooverToCenter(binding.mainScreen)
            restart = false
        }

        if (win) {
            soundManager.playSound(SoundEffect.VACUUM_DRIVING_RIGHT)
            FragmentNavigation.changeBG(this, R.id.elevatorFragment) // Надо так , иначе кнопка назад не сработает
            FragmentNavigation.changeBG(this, R.id.lvl3Fragment)
        }
    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }


    override fun skipPuzzle() {
        puzzleHoover.complete(saveRepo)
        win = true
        test()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.tpu.thetower.fragments

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.tpu.thetower.HintManager
import com.tpu.thetower.Hintable
import com.tpu.thetower.LoadManager
import com.tpu.thetower.MusicManager
import com.tpu.thetower.R
import com.tpu.thetower.SoundManager
import com.tpu.thetower.databinding.FragmentLvl3PuzzleHooverBinding
import com.tpu.thetower.puzzles.Direction
import com.tpu.thetower.puzzles.Lvl3PuzzleHoover

const val ANIM_DURATION = 800L
const val ANIM_DURATION_UP = 2000L

class Lvl3PuzzleHooverFragment : Fragment(R.layout.fragment_lvl3_puzzle_hoover), Hintable {

    private lateinit var binding: FragmentLvl3PuzzleHooverBinding

    private lateinit var tvCoordinates : TextView
    private lateinit var tvDirection: TextView
    private lateinit var tvRestart: TextView
    private lateinit var tvWin : TextView

    private lateinit var ivBg : ImageView
    private lateinit var ivHoover : ImageView

    private lateinit var btnRight: ImageButton
    private lateinit var btnLeft: ImageButton
    private lateinit var btnForward: ImageButton

    private lateinit var mainLayout: ConstraintLayout

    private val puzzleHoover = Lvl3PuzzleHoover(3, "vacuum cleaner")
    private lateinit var hintManager: HintManager
    private lateinit var soundManager: SoundManager
    private lateinit var musicManager: MusicManager

    private var restart : Boolean = false
    private var onStartPosition : Boolean = true
    private var win : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLvl3PuzzleHooverBinding.bind(view)
        bindView()
        setListeners()
        test()
        handleSounds()

//        soundManager = SoundManager.getInstance()
//        soundManager.loadSound(requireContext(), listOf(
//            R.raw.sound_of_the_lock_opening,
//            R.raw.sound_of_segments_rotating_on_the_safe_lock
//        ))

        hintManager = HintManager(
            listOf(
                "lvl3_puzzle2_hint1",
                "lvl3_puzzle2_hint2",
                "lvl3_puzzle2_hint3",
                "lvl3_puzzle2_hint4"
            ),
            LoadManager.getPuzzleUsedHintsCount(requireActivity(), 3, "vacuum cleaner"),
            3, "vacuum cleaner"
        )

    }

    private fun bindView() {
        tvCoordinates = binding.tvCoordinates
        tvDirection = binding.tvDirection
        tvRestart = binding.tvRestart
        tvWin = binding.tvWin
        btnRight = binding.btnRight
        btnLeft = binding.btnLeft
        btnForward = binding.btnForward
        ivBg = binding.ivMonitorImage
        ivHoover = binding.ivHoover
        mainLayout = binding.mainScreen
    }


    private fun rotateHooverAnim(clockwise : Boolean) {
        changeButtonsState(false)
        puzzleHoover.changeDirection(clockwise = clockwise)
        val value = if (clockwise) 90f else -90f
        ivHoover.animate().rotationBy(value).setDuration(300).withEndAction {
            test()
            changeButtonsState(true)
        }.start()
    }

    private fun setListeners() {
        btnLeft.setOnClickListener {
            rotateHooverAnim(false)
            soundManager.playSound(R.raw.sound_of_vacuum_cleaner_driving_left)
        }
        btnRight.setOnClickListener {
            rotateHooverAnim(true)
            soundManager.playSound(R.raw.sound_of_vacuum_cleaner_driving_right)
        }

        btnForward.setOnClickListener {
            tvRestart.text = ""
            tvWin.text = ""
            changeButtonsState(false)
            if (onStartPosition) { //В начале
                soundManager.playSound(R.raw.sound_of_vacuum_cleaner_driving_straight)
                moveHooverAnim(puzzleHoover.currDirection)
            }
            else if (puzzleHoover.currPositionY == 12 && puzzleHoover.currDirection == Direction.Down )
            { // Вернулись назад (небольшой костыль, ни на что не влияет, просто тут уже дело времени, которого мало
                soundManager.playSound(R.raw.sound_of_vacuum_cleaner_driving_straight)
                moveHooverToCenter(back = true)
            }
            else { // Двигаемся внутри вентиляции
                btnForward.postDelayed({
                    restart = !puzzleHoover.moveForward()
                    if (!restart) soundManager.playSound(R.raw.sound_of_vacuum_cleaner_driving_straight)
                    win = puzzleHoover.checkSolution(requireActivity())
                    test()
                    changeButtonsState(true)
                }, 1000)
            }
        }
    }

    private fun changeButtonsState(state : Boolean) {
        val alpha = if (state) 1f else 0.5f
        btnLeft.isEnabled = state
        btnRight.isEnabled = state
        btnForward.isEnabled = state

        btnLeft.alpha = alpha
        btnRight.alpha = alpha
        btnForward.alpha = alpha
    }

    private fun moveHooverAnim(direction: Direction) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainLayout)

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

        val transition = ChangeBounds()
        transition.duration = if (direction == Direction.Up) ANIM_DURATION_UP else ANIM_DURATION

        transition.addListener(object : android.transition.Transition.TransitionListener {
            override fun onTransitionEnd(transition: android.transition.Transition?) {
                transition?.removeListener(this)
                if (puzzleHoover.currDirection != Direction.Up) {
                    // Возврат на исходную позицию (центр)
                    ivHoover.postDelayed({
                        moveHooverToCenter()
                    }, 750)
                } else {
                    onStartPosition = false
                    puzzleHoover.moveForward()
//                    restart = puzzleHoover.isWall()
//                    win = puzzleHoover.checkSolution(requireContext())
                    test()
                    changeButtonsState(true)
                }
            }
            override fun onTransitionStart(transition: android.transition.Transition?) {}
            override fun onTransitionCancel(transition: android.transition.Transition?) {}
            override fun onTransitionPause(transition: android.transition.Transition?) {}
            override fun onTransitionResume(transition: android.transition.Transition?) {}
        })

        TransitionManager.beginDelayedTransition(mainLayout, transition)
        constraintSet.applyTo(mainLayout)
    }

    private fun moveHooverToCenter(back : Boolean = false) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(mainLayout)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.START)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.END)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_hoover, ConstraintSet.BOTTOM)
        // Центрируем по всем сторонам
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(R.id.iv_hoover, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        val transition = ChangeBounds()
        transition.duration = if (back) ANIM_DURATION_UP else ANIM_DURATION

        transition.addListener(object : android.transition.Transition.TransitionListener {
            override fun onTransitionEnd(transition: android.transition.Transition?) {
                transition?.removeListener(this)
                test()
                onStartPosition = true
                changeButtonsState(true)
            }
            override fun onTransitionStart(transition: android.transition.Transition?) {}
            override fun onTransitionCancel(transition: android.transition.Transition?) {}
            override fun onTransitionPause(transition: android.transition.Transition?) {}
            override fun onTransitionResume(transition: android.transition.Transition?) {}
        })

        TransitionManager.beginDelayedTransition(mainLayout, transition)
        constraintSet.applyTo(mainLayout)
    }

// Временно для тестирования
    private fun test() {
        if (restart) {
            soundManager.playSound(R.raw.sound_of_vacuum_cleaner_bumping)
            tvRestart.text = "*Звук стука об стенку*\nВозврат на исходное положение"
            ivHoover.animate().rotation(0f).setDuration(300).start()
            moveHooverToCenter()
            restart = false
        }

        if (win) {
            tvWin.text = "Победил!"
            win = false
        }

        tvDirection.text =
            when (puzzleHoover.currDirection) {
                Direction.Right -> "Вправо"
                Direction.Left -> "Влево"
                Direction.Down -> "Вниз"
                Direction.Up -> "Вверх"
            }
        tvCoordinates.text = "${puzzleHoover.currPositionX} ${puzzleHoover.currPositionY}"

    }

    override fun useHint() {
        hintManager.useHint(requireActivity())
    }

    private fun handleSounds() {
        musicManager = MusicManager.getInstance()
        soundManager = SoundManager.getInstance()
        soundManager.init()
        soundManager.loadSound(
            requireContext(), listOf(
                R.raw.sound_of_vacuum_cleaner_bumping,
                R.raw.sound_of_vacuum_cleaner_driving_right,
                R.raw.sound_of_vacuum_cleaner_driving_left,
                R.raw.sound_of_vacuum_cleaner_driving_straight
                )
        )
    }
}